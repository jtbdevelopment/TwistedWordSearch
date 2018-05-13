package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Test;

/**
 * Date: 8/29/16 Time: 6:30 PM
 */
public class RandomFillInitializerTest {

  private RandomFillInitializer initializer = new RandomFillInitializer();

  private static Map<Character, Integer> initialCounts() {
    Map<Character, Integer> map = new HashMap<>();
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ".chars().forEach(letter -> map.put((char) letter, 0));
    return map;
  }

  private static <K, V, Value extends V> Value putAt0(Map<K, V> propOwner, K key, Value value) {
    propOwner.put(key, value);
    return value;
  }

  @Test
  public void testFillDoesNotFillNonQuestionMarks() {
    TWSGame game = new TWSGame();

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.RandomFill)));
    game.setGrid(new Grid(10, 10));
    game.setWords(new HashSet<>(new ArrayList<>()));

    game.getGrid().setGridCell(0, 0, Grid.SPACE);
    game.getGrid().setGridCell(5, 5, Grid.SPACE);
    game.getGrid().setGridCell(1, 1, 'X');
    game.getGrid().setGridCell(1, 2, 'Y');
    game.getGrid().setGridCell(1, 3, 'Z');

    initializer.initializeGame(game);

    for (int row = 0; row < game.getGrid().getRows(); ++row) {
      for (int col = 0; col < game.getGrid().getColumns(); ++col) {
        char letter = game.getGrid().getGridCell(row, col);
        if ((row == 0 && col == 0) || (row == 5 && col == 5)) {
          assertEquals(Grid.SPACE, letter);
        } else if (row == 1 && (col >= 1 && col <= 3)) {
          assertEquals("XYZ".toCharArray()[col - 1], letter);
        } else {
          assertTrue(letter >= 'A');
          assertTrue(letter <= 'Z');
        }

      }
    }
  }

  @Test
  public void testInitializeGameFullRandom() {
    TWSGame game = new TWSGame();

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.RandomFill)));
    final Iterator<Object> iterator = testGame(game).iterator();
    Set<Character> wordLetters = ((Set<Character>) (iterator.hasNext() ? iterator.next() : null));
    Map<Character, Integer> counts = ((Map<Character, Integer>) (iterator.hasNext() ? iterator
        .next() : null));

    // average shold be around 384
    counts.entrySet().stream()
        .filter(entry -> !wordLetters.contains(entry.getKey()))
        .mapToInt(Entry::getValue)
        .forEach(count -> assertTrue(count < 460));
  }

  @Test
  public void testInitializeGameWordChunks() {
    //  Same as full random - assumes chunks have been placed already
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.WordChunks)));
    final Iterator<Object> iterator = testGame(game).iterator();
    Set<Character> wordLetters = ((Set<Character>) (iterator.hasNext() ? iterator.next() : null));
    Map<Character, Integer> counts = ((Map<Character, Integer>) (iterator.hasNext() ? iterator
        .next() : null));

    // average shold be around 384
    counts.entrySet().stream()
        .mapToInt(Entry::getValue)
        .forEach(count -> assertTrue(count < 460));
  }

  @Test
  public void testInitializeGameSomeOverlap() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.SomeOverlap)));
    final Iterator<Object> iterator = testGame(game).iterator();
    Set<Character> wordLetters = ((Set<Character>) (iterator.hasNext() ? iterator.next() : null));
    Map<Character, Integer> counts = ((Map<Character, Integer>) (iterator.hasNext() ? iterator
        .next() : null));

    //2500 should go to non-random + 288 (1100) to random
    int nonRandom = counts.entrySet().stream()
        .filter(entry -> wordLetters.contains(entry.getKey()))
        .mapToInt(Entry::getValue).sum();
    assertTrue(3300 < nonRandom);
    counts.entrySet().stream()
        .filter(entry -> !wordLetters.contains(entry.getKey()))
        .mapToInt(Entry::getValue)
        .forEach(count -> assertTrue(count < 345));
  }

  @Test
  public void testInitializeGameStrongOverlap() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.StrongOverlap)));
    final Iterator<Object> iterator = testGame(game).iterator();
    Set<Character> wordLetters = ((Set<Character>) (iterator.hasNext() ? iterator.next() : null));
    Map<Character, Integer> counts = ((Map<Character, Integer>) (iterator.hasNext() ? iterator
        .next() : null));

    //5000 should go to non-random + 192 (769) to random
    int nonRandom = counts.entrySet().stream()
        .filter(entry -> wordLetters.contains(entry.getKey()))
        .mapToInt(Entry::getValue).sum();
    assertTrue(5500 < nonRandom);
    counts.entrySet().stream()
        .filter(entry -> !wordLetters.contains(entry.getKey()))
        .mapToInt(Entry::getValue)
        .forEach(count -> assertTrue(count < 250));
  }

  private List<Object> testGame(final TWSGame game) {
    game.setGrid(new Grid(100, 100));
    game.setWords(new HashSet<>(Arrays.asList("TEXT", "EXIT")));

    Set<Character> wordLetters = new HashSet<>();
    game.getWords().forEach(word -> word.chars().forEach(letter -> wordLetters.add((char) letter)));

    initializer.initializeGame(game);
    final Map<Character, Integer> counts = initialCounts();

    for (int row = 0; row < game.getGrid().getRows(); ++row) {
      for (int col = 0; col < game.getGrid().getColumns(); ++col) {
        char letter = game.getGrid().getGridCell(row, col);
        counts.put(letter, counts.get(letter) + 1);
      }
    }
    return Arrays.asList(wordLetters, counts);
  }

  @Test
  public void testGetOrder() {
    assertEquals(GameInitializer.DEFAULT_ORDER + 30, initializer.getOrder());
  }
}
