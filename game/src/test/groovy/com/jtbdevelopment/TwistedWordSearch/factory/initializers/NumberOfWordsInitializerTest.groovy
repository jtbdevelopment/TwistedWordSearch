package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/16/16
 * Time: 7:11 AM
 */
class NumberOfWordsInitializerTest extends GroovyTestCase {

    NumberOfWordsInitializer initializer = new NumberOfWordsInitializer()
    TWSGame game

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        game = new TWSGame()
        game.usableSquares = 100
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER - 10 == initializer.order
    }

    void testInitializeBeginnerGame() {
        game.features = [GameFeature.EasiestDifficulty] as Set
        game.wordAverageLengthGoal = 5
        initializer.initializeGame(game)

        assert 4 == game.numberOfWords
    }

    void testInitializeExperiencedGame() {
        game.features = [GameFeature.StandardDifficulty] as Set
        game.wordAverageLengthGoal = 5
        initializer.initializeGame(game)

        assert 5 == game.numberOfWords
    }

    void testInitializeExpertGame() {
        game.features = [GameFeature.HarderDifficulty] as Set
        game.wordAverageLengthGoal = 5
        initializer.initializeGame(game)

        assert 6 == game.numberOfWords
    }

    void testInitializeProfessionalGame() {
        game.features = [GameFeature.FiendishDifficulty] as Set
        game.wordAverageLengthGoal = 5
        initializer.initializeGame(game)

        assert 7 == game.numberOfWords
    }
}
