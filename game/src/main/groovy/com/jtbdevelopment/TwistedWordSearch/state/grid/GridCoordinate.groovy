package com.jtbdevelopment.TwistedWordSearch.state.grid

import groovy.transform.CompileStatic

/**
 * Date: 7/18/16
 * Time: 3:40 PM
 */
@CompileStatic
class GridCoordinate {
    int row
    int column

    public GridCoordinate(final int row, final int column) {
        this.row = row
        this.column = column
    }

    boolean equals(final o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        final GridCoordinate gridCell = (GridCoordinate) o

        if (column != gridCell.column) return false
        if (row != gridCell.row) return false

        return true
    }

    int hashCode() {
        int result
        result = row
        result = 31 * result + column
        return result
    }
}
