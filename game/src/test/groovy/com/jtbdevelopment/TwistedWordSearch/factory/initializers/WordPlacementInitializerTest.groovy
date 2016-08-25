package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayout
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/24/16
 * Time: 6:46 PM
 */
class WordPlacementInitializerTest extends GroovyTestCase {
    WordPlacementInitializer initializer = new WordPlacementInitializer()

    void testInitializeGame() {
        TWSGame game = new TWSGame(features: [GameFeature.WordWrapYes])
        game.grid = new Grid(3, 3)
        game.words = ['ONE']

        initializer.randomLayoutPicker = [
                getRandomLayout: {
                    return WordLayout.VerticalDown
                }
        ] as RandomLayoutPicker
        initializer.initializeGame(game)

        (0..game.grid.rowUpperBound).each {
            int row ->
                println game.grid.getGridRow(row).toString()
        }
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER + 10 == initializer.order
    }
}
