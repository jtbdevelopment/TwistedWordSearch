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
public class DiamondGridInitializerTest {

  private DiamondGridInitializer initializer = new DiamondGridInitializer();

  @Test
  public void testInitializeGame() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Diamond50X50)));
    game.setGrid(new Grid(20, 20));
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
    assertArrayEquals("????????????????????".toCharArray(), game.getGrid().getGridCells()[10]);
    assertArrayEquals(" ?????????????????? ".toCharArray(), game.getGrid().getGridCells()[11]);
    assertArrayEquals("  ????????????????  ".toCharArray(), game.getGrid().getGridCells()[12]);
    assertArrayEquals("   ??????????????   ".toCharArray(), game.getGrid().getGridCells()[13]);
    assertArrayEquals("    ????????????    ".toCharArray(), game.getGrid().getGridCells()[14]);
    assertArrayEquals("     ??????????     ".toCharArray(), game.getGrid().getGridCells()[15]);
    assertArrayEquals("      ????????      ".toCharArray(), game.getGrid().getGridCells()[16]);
    assertArrayEquals("       ??????       ".toCharArray(), game.getGrid().getGridCells()[17]);
    assertArrayEquals("        ????        ".toCharArray(), game.getGrid().getGridCells()[18]);
    assertArrayEquals("         ??         ".toCharArray(), game.getGrid().getGridCells()[19]);
  }

  @Test
  public void testIgnoresNonDiamond() {
    Arrays.stream(GameFeature.values())
        .filter(p -> GameFeature.Grid.equals(p.getGroup()))
        .filter(p -> !p.toString().startsWith("Diamond"))
        .forEach(it -> {
          TWSGame game = new TWSGame();
          game.setGrid(new Grid(32, 32));
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
