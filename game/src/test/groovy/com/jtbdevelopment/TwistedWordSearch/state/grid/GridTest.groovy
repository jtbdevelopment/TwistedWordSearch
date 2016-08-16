package com.jtbdevelopment.TwistedWordSearch.state.grid

/**
 * Date: 8/12/16
 * Time: 5:02 PM
 */
class GridTest extends GroovyTestCase {
    void testConstructor() {
        Grid grid = new Grid(9, 11)
        assert 9 == grid.rows
        assert 11 == grid.columns
        char[][] expected = new char[9][11]
        (0..8).each {
            int row ->
                (0..10).each {
                    int col ->
                        expected[row][col] = '?' as char
                }
        }
        assert expected == grid.gridCells
        assert (9 * 11) == grid.usableSquares
    }

    void testGetUsableCellsAfterSettingSomeToSpace() {
        Grid grid = new Grid(10, 12)
        assert (10 * 12) == grid.usableSquares
        grid.gridCells[0][1] = ' ' as char
        grid.gridCells[5][6] = ' ' as char
        grid.gridCells[7][0] = ' ' as char
        assert (10 * 12) - 3 == grid.usableSquares
    }
}
