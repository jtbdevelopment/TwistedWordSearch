package com.jtbdevelopment.TwistedWordSearch.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import org.junit.Test;

/**
 * Date: 7/13/16 Time: 7:11 PM
 */
public class TWSGameTest {

  private TWSGame game = new TWSGame();

  @Test
  public void testInitialize() {
    assertNull(game.getGrid());
    assertEquals(0, game.getWordAverageLengthGoal());
    assertEquals(0, game.getNumberOfWords());
    assertEquals(0, game.getUsableSquares());
    assertEquals(0, game.getHintsRemaining());
    assertEquals(Collections.emptyMap(), game.getHintsGiven());
    assertEquals(Collections.emptyMap(), game.getWordEnds());
    assertEquals(Collections.emptyMap(), game.getWordStarts());
    assertEquals(Collections.emptyMap(), game.getHintsTaken());
    assertNull(game.getWords());
    assertNull(game.getWordsToFind());
    assertNull(game.getWordsFoundByPlayer());
    assertNull(game.getFoundWordLocations());
  }
}
