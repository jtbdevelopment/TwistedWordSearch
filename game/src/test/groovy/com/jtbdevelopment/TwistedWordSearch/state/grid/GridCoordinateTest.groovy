package com.jtbdevelopment.TwistedWordSearch.state.grid

/**
 * Date: 8/23/16
 * Time: 6:58 AM
 */
class GridCoordinateTest extends GroovyTestCase {
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
