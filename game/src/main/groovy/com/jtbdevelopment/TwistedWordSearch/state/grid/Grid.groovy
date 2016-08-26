package com.jtbdevelopment.TwistedWordSearch.state.grid

import groovy.transform.CompileStatic

/**
 * Date: 8/12/16
 * Time: 5:00 PM
 */
@CompileStatic
class Grid implements Serializable {
    public static char SPACE = ' ' as char;
    public static char QUESTION_MARK = '?' as char

    final int rows
    final int columns
    private final char[][] gridCells

    public Grid(int rows, int columns) {
        gridCells = new char[rows][columns]
        this.rows = rows
        this.columns = columns
        (0..rowUpperBound).each {
            int row ->
                (0..columnUpperBound).each {
                    int col ->
                        gridCells[row][col] = QUESTION_MARK
                }
        }
    }

    public int getRowUpperBound() {
        return rows - 1
    }

    public int getColumnUpperBound() {
        return columns - 1
    }

    public void resetGridLetters() {
        (0..rowUpperBound).each {
            int row ->
                (0..columnUpperBound).each {
                    int col ->
                        if (gridCells[row][col] != SPACE) {
                            gridCells[row][col] = QUESTION_MARK
                        }
                }
        }
    }

    public char[] getGridRow(final int row) {
        return gridCells[row]
    }

    public char setGridCell(final GridCoordinate coordinate, char letter) {
        gridCells[coordinate.row][coordinate.column] = letter
    }

    public char setGridCell(final int row, final int column, char letter) {
        gridCells[row][column] = letter
    }

    public char getGridCell(final int row, final int column) {
        return gridCells[row][column]
    }

    public char getGridCell(final GridCoordinate coordinate) {
        return gridCells[coordinate.row][coordinate.column]
    }

    public int getUsableSquares() {
        (int) gridCells.collect {
            row ->
                row.findAll {
                    cell ->
                        cell != SPACE
                }.size()
        }.sum()
    }
}
