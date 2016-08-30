package com.jtbdevelopment.TwistedWordSearch.factory.initializers

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

    void testWordsToPlace() {
        TWSGame game = new TWSGame(features: [GameFeature.WordWrapNo])
        game.grid = new Grid(3, 3)
        game.words = ['YOU', 'OF', 'TON']

        assert game.words == initializer.getWordsToPlace(game)
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER + 10 == initializer.order
    }


}
