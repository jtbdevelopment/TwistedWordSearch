package com.jtbdevelopment.TwistedWordSearch.state.grid;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Date: 8/12/16 Time: 5:02 PM
 */
public class GridTest {

  @Test
  public void testConstructor() {
    final Grid grid = new Grid(9, 11);
    assertEquals(9, grid.getRows());
    assertEquals(11, grid.getColumns());

    for (int row = 0; row < grid.getRows(); ++row) {
      assertArrayEquals("???????????".toCharArray(), grid.getGridRow(row));
    }
    assertEquals((9 * 11), grid.getUsableSquaresCount());
    assertEquals('?', grid.getGridCell(1, 1));
    assertEquals('?', grid.getGridCell(new GridCoordinate(1, 3)));
  }

  @Test
  public void testGetUsableCellsAfterSettingSomeToSpace() {
    Grid grid = new Grid(10, 12);
    assertEquals((10 * 12), grid.getUsableSquaresCount());
    grid.setGridCell(0, 1, Grid.SPACE);
    assertEquals(Grid.SPACE, grid.getGridCell(0, 1));
    grid.setGridCell(new GridCoordinate(5, 6), Grid.SPACE);
    assertEquals(Grid.SPACE, grid.getGridCell(5, 6));
    grid.setGridCell(7, 0, Grid.SPACE);
    assertEquals((10 * 12) - 3, grid.getUsableSquaresCount());
  }

  @Test
  public void testRestoreGridLetters() {
    Grid grid = new Grid(10, 12);
    grid.setGridCell(7, 0, Grid.SPACE);
    grid.setGridCell(0, 1, Grid.SPACE);

    char[][] backup = grid.backupGridLetters();
    grid.setGridCell(3, 3, 'X');
    grid.setGridCell(4, 4, 'X');

    assertEquals(Grid.SPACE, grid.getGridCell(0, 1));
    assertEquals(Grid.SPACE, grid.getGridCell(7, 0));
    assertEquals('X', grid.getGridCell(3, 3));
    assertEquals('X', grid.getGridCell(4, 4));

    grid.restoreGridLetters(backup);

    assertEquals(Grid.SPACE, grid.getGridCell(0, 1));
    assertEquals(Grid.SPACE, grid.getGridCell(7, 0));
    assertEquals(Grid.QUESTION_MARK, grid.getGridCell(3, 3));
    assertEquals(Grid.QUESTION_MARK, grid.getGridCell(4, 4));
  }

}
