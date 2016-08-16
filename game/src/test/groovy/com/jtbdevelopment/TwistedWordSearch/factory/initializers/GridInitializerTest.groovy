package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/15/16
 * Time: 6:55 PM
 */
class GridInitializerTest extends GroovyTestCase {
    GridInitializer initializer = new GridInitializer()

    void testInitializeGameForSquares() {
        TWSGame game = new TWSGame()

        game.features = [GameFeature.Grid30X30] as Set
        initializer.initializeGame(game)
        assert 30 == game.grid.rows
        assert 30 == game.grid.columns

        game.features = [GameFeature.Grid40X40] as Set
        initializer.initializeGame(game)
        assert 40 == game.grid.rows
        assert 40 == game.grid.columns

        game.features = [GameFeature.Grid50X50] as Set
        initializer.initializeGame(game)
        assert 50 == game.grid.rows
        assert 50 == game.grid.columns
    }

    void testInitializeGameForDiamonds() {
        TWSGame game = new TWSGame()

        game.features = [GameFeature.Diamond40x40] as Set
        initializer.initializeGame(game)
        assert 40 == game.grid.rows
        assert 40 == game.grid.columns

        game.features = [GameFeature.Diamond50x50] as Set
        initializer.initializeGame(game)
        assert 50 == game.grid.rows
        assert 50 == game.grid.columns
    }

    void testInitializeGameForCircles() {
        TWSGame game = new TWSGame()

        game.features = [GameFeature.CircleX40] as Set
        initializer.initializeGame(game)
        assert 41 == game.grid.rows
        assert 41 == game.grid.columns

        game.features = [GameFeature.CircleX50] as Set
        initializer.initializeGame(game)
        assert 51 == game.grid.rows
        assert 51 == game.grid.columns
    }


    void testInitializeGameForPyramids() {
        TWSGame game = new TWSGame()

        game.features = [GameFeature.PyramidX40] as Set
        initializer.initializeGame(game)
        assert 20 == game.grid.rows
        assert 40 == game.grid.columns

        game.features = [GameFeature.PyramidX50] as Set
        initializer.initializeGame(game)
        assert 25 == game.grid.rows
        assert 50 == game.grid.columns
    }

    void testGetOrder() {
        assert GameInitializer.EARLY_ORDER == initializer.order
    }
}
