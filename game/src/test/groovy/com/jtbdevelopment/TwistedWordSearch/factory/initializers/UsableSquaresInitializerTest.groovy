package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/16/16
 * Time: 6:52 AM
 */
class UsableSquaresInitializerTest extends GroovyTestCase {
    UsableSquaresInitializer initializer = new UsableSquaresInitializer()

    void testInitializeGame() {
        TWSGame game = new TWSGame()
        game.grid = new Grid(10, 10)
        game.grid.gridCells[0][0] = ' ' as char
        game.grid.gridCells[7][5] = ' ' as char
        assert 98 == game.grid.usableSquares
        assert 0 == game.usableSquares

        initializer.initializeGame(game)
        assert 98 == game.usableSquares
    }

    void testGetOrder() {
        assert GameInitializer.EARLY_ORDER + 20 == initializer.order
    }
}
