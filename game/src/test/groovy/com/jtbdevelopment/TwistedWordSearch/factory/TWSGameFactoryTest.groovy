package com.jtbdevelopment.TwistedWordSearch.factory

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame

/**
 * Date: 7/13/16
 * Time: 9:32 PM
 */
class TWSGameFactoryTest extends GroovyTestCase {
    TWSGameFactory factory = new TWSGameFactory(Collections.emptyList(), Collections.emptyList())

    void testCreatesNewGame() {
        TWSGame game1 = factory.newGame()
        TWSGame game2 = factory.newGame()

        assertNotNull game1
        assertNotNull game2
        assertFalse game1.is(game2)
    }
}
