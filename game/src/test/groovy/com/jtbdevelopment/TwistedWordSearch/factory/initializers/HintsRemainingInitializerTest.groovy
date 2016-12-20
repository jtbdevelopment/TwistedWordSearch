package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 12/20/16
 * Time: 6:41 PM
 */
class HintsRemainingInitializerTest extends GroovyTestCase {
    HintsRemainingInitializer initializer = new HintsRemainingInitializer()

    void testInitializeGame() {
        TWSGame game = new TWSGame(numberOfWords: new Random().nextInt(30) + 10)
        initializer.initializeGame(game)
        assert ((int) game.numberOfWords * 0.25).intValue() == game.hintsRemaining
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }
}
