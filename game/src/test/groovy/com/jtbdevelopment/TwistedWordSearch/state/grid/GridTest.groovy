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
        assert 8 == grid.rowUpperBound
        assert 10 == grid.columnUpperBound
        char[][] expected = new char[9][11]
        (0..8).each {
            int row ->
                (0..10).each {
                    int col ->
                        expected[row][col] = '?' as char
                }
        }

        (0..grid.rowUpperBound).each {
            int row ->
                assert '???????????' == grid.getGridRow(row).toString()
        }
        assert (9 * 11) == grid.usableSquares
        assert '?' as char == grid.getGridCell(1, 1)
        assert '?' as char == grid.getGridCell(new GridCoordinate(1, 3))
    }

    void testGetUsableCellsAfterSettingSomeToSpace() {
        Grid grid = new Grid(10, 12)
        assert (10 * 12) == grid.usableSquares
        grid.setGridCell(0, 1, Grid.SPACE)
        assert Grid.SPACE == grid.getGridCell(0, 1)
        grid.setGridCell(new GridCoordinate(5, 6), Grid.SPACE)
        assert Grid.SPACE == grid.getGridCell(5, 6)
        grid.setGridCell(7, 0, Grid.SPACE)
        assert (10 * 12) - 3 == grid.usableSquares
    }

    void testResetGridLetters() {
        Grid grid = new Grid(10, 12)
        grid.setGridCell(7, 0, Grid.SPACE)
        grid.setGridCell(0, 1, Grid.SPACE)
        grid.setGridCell(3, 3, 'X' as char)
        grid.setGridCell(4, 4, 'X' as char)

        assert Grid.SPACE == grid.getGridCell(0, 1)
        assert Grid.SPACE == grid.getGridCell(7, 0)
        assert 'X' as char == grid.getGridCell(3, 3)
        assert 'X' as char == grid.getGridCell(4, 4)

        grid.resetGridLetters()
        assert Grid.SPACE == grid.getGridCell(0, 1)
        assert Grid.SPACE == grid.getGridCell(7, 0)
        assert Grid.QUESTION_MARK == grid.getGridCell(3, 3)
        assert Grid.QUESTION_MARK == grid.getGridCell(4, 4)
    }
}
