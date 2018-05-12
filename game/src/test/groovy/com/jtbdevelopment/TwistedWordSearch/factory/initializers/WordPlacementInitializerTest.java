package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Test;

/**
 * Date: 8/24/16 Time: 6:46 PM
 */
public class WordPlacementInitializerTest {

  private WordPlacementInitializer initializer = new WordPlacementInitializer(
      new RandomLayoutPicker());

  @Test
  public void testWordsToPlace() {
    final TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.WordWrapNo)));
    game.setGrid(new Grid(3, 3));
    game.setWords(new HashSet<>(Arrays.asList("YOU", "OF", "TON")));

    assert game.getWords().equals(initializer.getWordsToPlace(game));
    initializer.initializeGame(game);
    game.getWords().forEach(it -> {
      assertEquals(it.toCharArray()[0],
          game.getGrid().getGridCell(game.getWordStarts().get(it)));
      assertEquals(it.toCharArray()[it.length() - 1],
          game.getGrid().getGridCell(game.getWordEnds().get(it)));
    });
  }

  @Test
  public void testGetOrder() {
    assertEquals(GameInitializer.DEFAULT_ORDER + 10, initializer.getOrder());
  }
}
