package com.jtbdevelopment.TwistedWordSearch.state.masking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Date: 7/13/16 Time: 7:11 PM
 */
public class MaskedGameTest {

  private MaskedGame game = new MaskedGame();

  @Test
  public void testInitialize() {
    assertNull(game.getGrid());
    assertNull(game.getWordsToFind());
    assertNull(game.getWordsFoundByPlayer());
    assertNull(game.getHints());
    assertNull(game.getHintsTaken());
    assertEquals(0, game.getHintsRemaining());
  }
}
