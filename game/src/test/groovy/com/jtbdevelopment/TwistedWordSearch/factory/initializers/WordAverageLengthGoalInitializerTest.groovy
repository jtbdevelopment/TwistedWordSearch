package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/16/16
 * Time: 6:59 AM
 */
class WordAverageLengthGoalInitializerTest extends GroovyTestCase {
    WordAverageLengthGoalInitializer initializer = new WordAverageLengthGoalInitializer()

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER - 20 == initializer.order
    }

    void testBeginnerWordLength() {
        TWSGame game = new TWSGame(features: [GameFeature.EasiestDifficulty] as Set)
        initializer.initializeGame(game)
        assert 10 == game.wordAverageLengthGoal
    }

    void testExperiencedWordLength() {
        TWSGame game = new TWSGame(features: [GameFeature.StandardDifficulty] as Set)
        initializer.initializeGame(game)
        assert 8 == game.wordAverageLengthGoal
    }

    void testExpertWordLength() {
        TWSGame game = new TWSGame(features: [GameFeature.HarderDifficulty] as Set)
        initializer.initializeGame(game)
        assert 7 == game.wordAverageLengthGoal
    }

    void testProfessionalWordLength() {
        TWSGame game = new TWSGame(features: [GameFeature.FiendishDifficulty] as Set)
        initializer.initializeGame(game)
        assert 6 == game.wordAverageLengthGoal
    }
}
