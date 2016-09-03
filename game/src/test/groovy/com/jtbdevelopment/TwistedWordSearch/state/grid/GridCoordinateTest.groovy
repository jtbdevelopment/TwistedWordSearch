package com.jtbdevelopment.TwistedWordSearch.state.grid

/**
 * Date: 8/23/16
 * Time: 6:58 AM
 */
class GridCoordinateTest extends GroovyTestCase {

    void testGetSetCoordinates() {
        GridCoordinate coordinate = new GridCoordinate(30, 17)
        assert 30 == coordinate.row
        assert 17 == coordinate.column

        coordinate.row = 25
        assert 25 == coordinate.row
        assert 17 == coordinate.column

        coordinate.column = 5
        assert 25 == coordinate.row
        assert 5 == coordinate.column
    }

    void testCopyConstructor() {
        GridCoordinate original = new GridCoordinate(10, 11)
        GridCoordinate copy = new GridCoordinate(original)
        assert 10 == copy.row
        assert 11 == copy.column
        assert 10 == original.row
        assert 11 == original.column
    }

    void testEquals() {
        assert new GridCoordinate(15, 22) == new GridCoordinate(15, 22)
        assert new GridCoordinate(17, 4) == new GridCoordinate(17, 4)
    }

    void testNotEquals() {
        assertFalse new GridCoordinate(15, 22) == new GridCoordinate(16, 22)
        assertFalse new GridCoordinate(17, 4) == new GridCoordinate(17, 5)
    }


    void testHashCode() {
        GridCoordinate coordinate = new GridCoordinate(10, 15)
        assert 325 == coordinate.hashCode()
    }
}
