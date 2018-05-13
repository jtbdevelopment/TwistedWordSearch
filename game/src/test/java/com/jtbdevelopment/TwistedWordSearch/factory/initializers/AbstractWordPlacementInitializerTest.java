package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * Date: 8/24/16 Time: 6:46 PM
 */
public class AbstractWordPlacementInitializerTest {

  private static Set<List<String>> wrapPossibilities;
  private static Set<List<String>> noWrapPossibilities;
  private TestWordPlacementInitializer initializer = new TestWordPlacementInitializer();

  @BeforeClass
  public static void readWords() throws IOException {
    wrapPossibilities = new HashSet<>();
    noWrapPossibilities = new HashSet<>();
    ClassPathResource wrapped = new ClassPathResource("wrappedwords.txt");
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(wrapped.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (StringUtils.isNotBlank(line)) {
          wrapPossibilities
              .add(Arrays.asList(StringUtils.split(StringUtils.replace(line, "\"", ""), ',')));
        }
      }
    }

    ClassPathResource nonwrapped = new ClassPathResource("nowrapwords.txt");
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(nonwrapped.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (StringUtils.isNotBlank(line)) {
          noWrapPossibilities
              .add(Arrays.asList(StringUtils.split(StringUtils.replace(line, "\"", ""), ',')));
        }
      }
    }
  }

  private static List<String> getGridAsStrings(final TWSGame game) {
    List<String> rows = new LinkedList<>();
    for (int row = 0; row < game.getGrid().getRows(); ++row) {
      rows.add(new String(game.getGrid().getGridRow(row)));
    }
    return rows;
  }

  @Test
  public void testInitializeGameNoWordWrap() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.WordWrapNo)));
    game.setGrid(new Grid(3, 3));

    game.getGrid().setGridCell(0, 0, Grid.SPACE);
    game.getGrid()
        .setGridCell(game.getGrid().getRowUpperBound(), game.getGrid().getColumnUpperBound(),
            Grid.SPACE);

    assertEquals(Arrays.asList(" ??", "???", "?? "), getGridAsStrings(game));

    final Set<List<String>> used = new HashSet<>();
    final char[][] initialGrid = game.getGrid().backupGridLetters();
    for (int i = 0; i < 500; ++i) {
      game.getGrid().restoreGridLetters(initialGrid);
      initializer.initializeGame(game);

      List<String> strings = getGridAsStrings(game);

      assertTrue(noWrapPossibilities.contains(strings));
      used.add(strings);
    }
    assertTrue(used.size() > 30);
    assertTrue(used.size() < 500);
    Collection<String> wordsToPlace = initializer.getWordsToPlace(null);
    assertEquals(initializer.getStarts().keySet(), new HashSet<>(wordsToPlace));
    assertEquals(initializer.getEnds().keySet(), new HashSet<>(wordsToPlace));
    wordsToPlace.forEach(word -> {
      assertEquals(word.toCharArray()[0],
          game.getGrid().getGridCell(initializer.getStarts().get(word)));
      assertEquals(word.toCharArray()[word.length() - 1],
          game.getGrid().getGridCell(initializer.getEnds().get(word)));
    });
  }

  @Test
  public void testInitializeGameWordWrap() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.WordWrapYes)));
    game.setGrid(new Grid(3, 3));

    game.getGrid().setGridCell(0, 0, Grid.SPACE);
    game.getGrid()
        .setGridCell(game.getGrid().getRowUpperBound(), game.getGrid().getColumnUpperBound(),
            Grid.SPACE);

    assertEquals(Arrays.asList(" ??", "???", "?? "), getGridAsStrings(game));

    final int[] wrapCount = new int[]{0};
    final char[][] initialGrid = game.getGrid().backupGridLetters();
    final Set<List<String>> used = new HashSet<>();
    for (int i = 0; i < 500; ++i) {
      game.getGrid().restoreGridLetters(initialGrid);
      initializer.initializeGame(game);

      List<String> strings = getGridAsStrings(game);

      if (wrapPossibilities.contains(strings)) {
        wrapCount[0]++;
      }
      assertTrue(
          noWrapPossibilities.contains(strings) || wrapPossibilities.contains(strings));
      used.add(strings);
    }
    assertTrue(wrapCount[0] > 0);// at least some should be word wrapped examples
    assertTrue(used.size() > 50);
    assertTrue(used.size() < 500);
    Collection<String> wordsToPlace = initializer.getWordsToPlace(null);
    assertEquals(initializer.getStarts().keySet(), new HashSet<>(wordsToPlace));
    assertEquals(initializer.getEnds().keySet(), new HashSet<>(wordsToPlace));
    wordsToPlace.forEach(word -> {
      assertEquals(word.toCharArray()[0],
          game.getGrid().getGridCell(initializer.getStarts().get(word)));
      assertEquals(word.toCharArray()[word.length() - 1],
          game.getGrid().getGridCell(initializer.getEnds().get(word)));
    });
  }

  private static class TestWordPlacementInitializer extends AbstractWordPlacementInitializer {

    private Map<String, GridCoordinate> starts = new LinkedHashMap<>();
    private Map<String, GridCoordinate> ends = new LinkedHashMap<>();

    TestWordPlacementInitializer() {
      super(new RandomLayoutPicker());
    }

    protected Collection<String> getWordsToPlace(final TWSGame game) {
      return Arrays.asList("YOU", "OF", "TON");
    }

    protected void wordPlacedAt(final TWSGame game, final String word, final GridCoordinate start,
        final GridCoordinate end) {
      starts.put(word, start);
      ends.put(word, end);
    }

    public int getOrder() {
      return 0;
    }

    Map<String, GridCoordinate> getStarts() {
      return starts;
    }

    Map<String, GridCoordinate> getEnds() {
      return ends;
    }
  }
}
