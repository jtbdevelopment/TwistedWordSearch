package com.jtbdevelopment.TwistedWordSearch.state.grid

import groovy.transform.CompileStatic

/**
 * Date: 8/12/16
 * Time: 5:00 PM
 */
@CompileStatic
class Grid {
    int rows
    int columns
    final char[][] gridCells

    public Grid(int rows, int columns) {
        gridCells = new char[rows][columns]
        this.rows = rows
        this.columns = columns
        (0..(rows - 1)).each {
            int row ->
                (0..(columns - 1)).each {
                    int col ->
                        gridCells[row][col] = '?' as char
                }
        }
    }

    public int getUsableSquares() {
        (int) gridCells.collect {
            row ->
                row.findAll {
                    cell ->
                        cell != ' ' as char
                }.size()
        }.sum()
    }
}
