package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.junit.Test;

/**
 * Date: 8/16/16 Time: 6:52 AM
 */
public class UsableSquaresInitializerTest {

  private UsableSquaresInitializer initializer = new UsableSquaresInitializer();

  @Test
  public void testInitializeGame() {
    TWSGame game = new TWSGame();
    game.setGrid(new Grid(10, 10));
    game.getGrid().setGridCell(0, 0, Grid.SPACE);
    game.getGrid().setGridCell(7, 5, Grid.SPACE);
    assertEquals(98, game.getGrid().getUsableSquaresCount());
    assertEquals(0, game.getUsableSquares());

    initializer.initializeGame(game);
    assertEquals(98, game.getUsableSquares());
    }

  @Test
  public void testGetOrder() {
    assertEquals(GameInitializer.EARLY_ORDER + 20, initializer.getOrder());
    }
}
