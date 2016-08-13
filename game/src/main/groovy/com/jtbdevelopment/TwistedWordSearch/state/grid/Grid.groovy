package com.jtbdevelopment.TwistedWordSearch.state.grid

/**
 * Date: 8/12/16
 * Time: 5:00 PM
 */
class Grid {
    int rows
    int columns
    final char[][] gridCells

    public Grid(int rows, int columns) {
        gridCells = new char[rows][columns]
        this.rows = rows
        this.columns = columns
    }

}
