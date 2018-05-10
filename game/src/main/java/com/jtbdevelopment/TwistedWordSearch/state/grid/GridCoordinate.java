package com.jtbdevelopment.TwistedWordSearch.state.grid;

import java.io.Serializable;

/**
 * Date: 7/18/16 Time: 3:40 PM
 */
public class GridCoordinate implements Serializable {

  private int row;
  private int column;

  public GridCoordinate() {

  }

  public GridCoordinate(final int row, final int column) {
    this.row = row;
    this.column = column;
  }

  public GridCoordinate(final GridCoordinate copy) {
    this.row = copy.row;
    this.column = copy.column;
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!getClass().equals(o.getClass())) {
      return false;
    }

    final GridCoordinate gridCell = (GridCoordinate) o;

    return column == gridCell.column && row == gridCell.row;
  }

  public int hashCode() {
    int result;
    result = row;
    result = 31 * result + column;
    return result;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }
}
