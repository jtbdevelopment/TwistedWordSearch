package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase

/**
 * Date: 9/23/16
 * Time: 10:32 PM
 */
class ScoreInitializerTest extends MongoGameCoreTestCase {
    ScoreInitializer initializer = new ScoreInitializer()

    void testInitializeGame() {
        TWSGame game = new TWSGame(players: [PFOUR, PTHREE])
        assertNull game.scores

        initializer.initializeGame(game)
        assert [(PFOUR.id): 0, (PTHREE.id): 0] == game.scores
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }
}
