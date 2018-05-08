package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import org.junit.Test

/**
 * Date: 8/30/16
 * Time: 3:28 PM
 */
class WordsFoundByPlayerInitializerTest extends MongoGameCoreTestCase {
    WordsFoundByPlayerInitializer initializer = new WordsFoundByPlayerInitializer()

    @Test
    void testInitializeGame() {
        TWSGame game = new TWSGame(players: [PONE, PTHREE])
        initializer.initializeGame(game)

        assert [(PONE.id): [] as Set, (PTHREE.id): [] as Set] == game.wordsFoundByPlayer
        assert [:] == game.foundWordLocations
    }

    @Test
    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }
}
