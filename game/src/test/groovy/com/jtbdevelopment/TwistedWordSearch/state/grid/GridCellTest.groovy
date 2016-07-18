package com.jtbdevelopment.TwistedWordSearch.state.grid

/**
 * Date: 7/18/16
 * Time: 3:47 PM
 */
class GridCellTest extends GroovyTestCase {
    GridCell testCell1 = new GridCell(true, 'A' as char)
    GridCell testCell2 = new GridCell(false, 'Z' as char)

    void testGetActive() {
        assert testCell1.isActive()
        assertFalse testCell2.isActive()
    }

    void testGetLetter() {
        assert 'A' as char == testCell1.letter
        assert 'Z' as char == testCell2.letter
    }
}
