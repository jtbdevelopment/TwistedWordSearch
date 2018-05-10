package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/24/16
 * Time: 6:46 PM
 */
class WordPlacementInitializerTest extends GroovyTestCase {
    WordPlacementInitializer initializer = new WordPlacementInitializer(new RandomLayoutPicker())

    void testWordsToPlace() {
        TWSGame game = new TWSGame(features: [GameFeature.WordWrapNo])
        game.grid = new Grid(3, 3)
        game.words = ['YOU', 'OF', 'TON']

        assert game.words == initializer.getWordsToPlace(game)
        initializer.initializeGame(game)
        game.words.each {
            assert it.toCharArray()[0] == game.grid.getGridCell(game.wordStarts[it])
            assert it.toCharArray()[-1] == game.grid.getGridCell(game.wordEnds[it])
        }
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER + 10 == initializer.order
    }


}
