package com.jtbdevelopment.TwistedWordSearch.state.grid;

import java.io.Serializable;

/**
 * Date: 8/12/16 Time: 5:00 PM
 */
public class Grid implements Serializable {

  public static char SPACE = ' ';
  public static char QUESTION_MARK = '?';
  private final int rows;
  private final int columns;
  private final char[][] gridCells;

  public Grid(int rows, int columns) {
    gridCells = new char[rows][columns];
    this.rows = rows;
    this.columns = columns;
    for (int row = 0; row < gridCells.length; ++row) {
      for (int col = 0; col < gridCells[row].length; ++col) {
        gridCells[row][col] = QUESTION_MARK;
      }
    }
  }

  public int getRowUpperBound() {
    return rows - 1;
  }

  public int getColumnUpperBound() {
    return columns - 1;
  }

  public char[][] backupGridLetters() {
    final char[][] backup = new char[rows][columns];
    for (int row = 0; row < gridCells.length; ++row) {
      System.arraycopy(gridCells[row], 0, backup[row], 0, gridCells[row].length);
    }
    return backup;
  }

  public void restoreGridLetters(final char[][] original) {
    for (int row = 0; row < original.length; ++row) {
      System.arraycopy(original[row], 0, gridCells[row], 0, original[row].length);
    }
  }

  public char[] getGridRow(final int row) {
    return gridCells[row];
  }

  public char setGridCell(final GridCoordinate coordinate, char letter) {
    return gridCells[coordinate.getRow()][coordinate.getColumn()] = letter;
  }

  public char setGridCell(final int row, final int column, char letter) {
    return gridCells[row][column] = letter;
  }

  public char getGridCell(final int row, final int column) {
    return gridCells[row][column];
  }

  public char getGridCell(final GridCoordinate coordinate) {
    return gridCells[coordinate.getRow()][coordinate.getColumn()];
  }

  public int getUsableSquaresCount() {
    int sum = 0;
    for (char[] gridCell : gridCells) {
      for (char aGridCell : gridCell) {
        if (SPACE != aGridCell) {
          ++sum;
        }
      }
    }
    return sum;
  }

  public int getQuestionMarkSquaresCount() {
    int sum = 0;
    for (char[] gridCell : gridCells) {
      for (char aGridCell : gridCell) {
        if (QUESTION_MARK == aGridCell) {
          ++sum;
        }
      }
    }
    return sum;
  }

  public final int getRows() {
    return rows;
  }

  public final int getColumns() {
    return columns;
  }

  public final char[][] getGridCells() {
    return gridCells;
  }
}
