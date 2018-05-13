package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/30/16 Time: 6:47 AM
 */
public class WordChunkFillInitializerTest {

  private static Set<String> ACCEPTABLE_CHUNKS = new HashSet<>(
      Arrays.asList(
          "A",
          "AS",
          "AST",
          "ASTO",
          "ASTON",
          "ASTONI",
          "ASTONIS",
          "D",
          "E",
          "G",
          "GE",
          "GL",
          "H",
          "HI",
          "HIN",
          "HING",
          "HINGL",
          "I",
          "IN",
          "ING",
          "INGL",
          "IS",
          "ISH",
          "ISHI",
          "ISHIN",
          "ISHING",
          "ISHINGL",
          "L",
          "LO",
          "LON",
          "M",
          "N",
          "NG",
          "NGE",
          "NGL",
          "NI",
          "NIS",
          "NISH",
          "NISHI",
          "NISHIN",
          "NISHING",
          "O",
          "OM",
          "ON",
          "ONG",
          "ONI",
          "ONIS",
          "ONISH",
          "ONISHI",
          "ONISHIN",
          "OR",
          "ORD",
          "R",
          "RD",
          "S",
          "SH",
          "SHI",
          "SHIN",
          "SHING",
          "SHINGL",
          "SO",
          "ST",
          "STO",
          "STON",
          "STONI",
          "STONIS",
          "STONISH",
          "T",
          "TO",
          "TON",
          "TONI",
          "TONIS",
          "TONISH",
          "TONISHI",
          "W",
          "WO",
          "WOR"));
  private RandomLayoutPicker randomLayoutPicker = new RandomLayoutPicker();
  private WordChunkFillInitializer initializer = new WordChunkFillInitializer(randomLayoutPicker);

  @Test
  public void testGetOrder() {
    Assert.assertEquals(GameInitializer.DEFAULT_ORDER + 20, initializer.getOrder());
  }

  @Test
  public void testReturnsEmptyListForNonWordChunkFillTypes() {
    Arrays.stream(GameFeature.values())
        .filter(f -> !GameFeature.WordChunks.equals(f))
        .filter(f -> GameFeature.FillDifficulty.equals(f.getGroup()))
        .forEach(it -> {

          TWSGame game = new TWSGame();
          game.setFeatures(new HashSet<>(Collections.singletonList(it)));
          game.setGrid(new Grid(20, 20));
          game.setWords(new HashSet<>(Arrays.asList("SOME", "WORDS", "ARE", "HERE")));

          Assert.assertEquals(new ArrayList(), initializer.getWordsToPlace(game));

        });
  }

  @Test
  public void testWordsForWordChunks() {
    TWSGame game = new TWSGame();
    game.setFeatures((new HashSet<>(Collections.singletonList(GameFeature.WordChunks))));
    game.setGrid(new Grid(50, 50));
    game.setWords(new HashSet<>(Arrays.asList("SOME", "WORDS", "ARE", "ASTONISHINGLY", "LONGER")));

    Collection<String> wordChunks = initializer.getWordsToPlace(game);
    Assert.assertNotEquals(new ArrayList(), wordChunks);
    assertTrue(1874 <= wordChunks.stream().mapToInt(String::length).sum());
    assertTrue(wordChunks.stream().allMatch(word -> ACCEPTABLE_CHUNKS.contains(word)));
  }
}
