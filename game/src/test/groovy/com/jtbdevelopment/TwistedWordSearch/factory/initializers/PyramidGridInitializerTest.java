package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertArrayEquals;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/15/16 Time: 6:35 PM
 */
public class PyramidGridInitializerTest {

  private PyramidGridInitializer initializer = new PyramidGridInitializer();

  @Test
  public void testInitializeGame() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.PyramidX40)));
    game.setGrid(new Grid(10, 20));
    initializer.initializeGame(game);

    assertArrayEquals("         ??         ".toCharArray(), game.getGrid().getGridCells()[0]);
    assertArrayEquals("        ????        ".toCharArray(), game.getGrid().getGridCells()[1]);
    assertArrayEquals("       ??????       ".toCharArray(), game.getGrid().getGridCells()[2]);
    assertArrayEquals("      ????????      ".toCharArray(), game.getGrid().getGridCells()[3]);
    assertArrayEquals("     ??????????     ".toCharArray(), game.getGrid().getGridCells()[4]);
    assertArrayEquals("    ????????????    ".toCharArray(), game.getGrid().getGridCells()[5]);
    assertArrayEquals("   ??????????????   ".toCharArray(), game.getGrid().getGridCells()[6]);
    assertArrayEquals("  ????????????????  ".toCharArray(), game.getGrid().getGridCells()[7]);
    assertArrayEquals(" ?????????????????? ".toCharArray(), game.getGrid().getGridCells()[8]);
    assertArrayEquals("????????????????????".toCharArray(), game.getGrid().getGridCells()[9]);
  }

  @Test
  public void testIgnoresNonPyramids() {
    Arrays.stream(GameFeature.values())
        .filter(p -> GameFeature.Grid.equals(p.getGroup()))
        .filter(p -> !p.toString().startsWith("Pyramid"))
        .forEach(it -> {
          TWSGame game = new TWSGame();
          game.setGrid(new Grid(40, 40));
          game.setFeatures(new HashSet<>(Collections.singletonList(it)));
          initializer.initializeGame(game);
          for (int row = 0; row < game.getGrid().getRows(); ++row) {
            for (int col = 0; col < game.getGrid().getColumns(); ++col) {
              Assert.assertEquals('?', game.getGrid().getGridCell(row, col));
            }
          }
        });
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(GameInitializer.EARLY_ORDER + 10, initializer.getOrder());
  }
}
