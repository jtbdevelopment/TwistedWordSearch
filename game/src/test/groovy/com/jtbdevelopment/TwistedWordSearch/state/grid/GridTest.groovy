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
        assert new char[9][11] == grid.gridCells
    }
}
