package com.jtbdevelopment.TwistedWordSearch.state.masking

/**
 * Date: 7/13/16
 * Time: 7:11 PM
 */
class MaskedGameTest extends GroovyTestCase {
    MaskedGame game = new MaskedGame()

    void testInitialize() {
        assertNull game.grid
        assertNull game.wordsToFind
        assertNull game.wordsFoundByPlayer
    }
}
