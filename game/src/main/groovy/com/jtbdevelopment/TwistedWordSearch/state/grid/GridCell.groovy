package com.jtbdevelopment.TwistedWordSearch.state.grid

import groovy.transform.CompileStatic

/**
 * Date: 7/18/16
 * Time: 3:40 PM
 */
@CompileStatic
class GridCell {
    final boolean active
    final char letter

    GridCell(final boolean active, final char letter) {
        this.active = active
        this.letter = letter
    }

}
