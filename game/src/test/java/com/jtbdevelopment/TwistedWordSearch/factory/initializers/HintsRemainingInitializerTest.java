package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Random;
import org.junit.Test;

/**
 * Date: 12/20/16 Time: 6:41 PM
 */
public class HintsRemainingInitializerTest {

  private HintsRemainingInitializer initializer = new HintsRemainingInitializer();

  @Test
  public void testInitializeGame() {
    TWSGame game = new TWSGame();
    game.setNumberOfWords(new Random().nextInt(30) + 10);
    initializer.initializeGame(game);
    assertEquals((int) (game.getNumberOfWords() * 0.25), game.getHintsRemaining());
  }

  @Test
  public void testGetOrder() {
    assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
  }
}
