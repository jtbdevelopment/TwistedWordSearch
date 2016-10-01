package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/16/16
 * Time: 6:56 AM
 */
@Component
@CompileStatic
class WordAverageLengthGoalInitializer implements GameInitializer<TWSGame> {
    private static final Map<GameFeature, Integer> WORD_LENGTHS = [
            (GameFeature.EasiestDifficulty) : 10,
            (GameFeature.StandardDifficulty): 8,
            (GameFeature.HarderDifficulty)  : 7,
            (GameFeature.FiendishDifficulty): 6,
    ]

    void initializeGame(final TWSGame game) {
        GameFeature difficulty = game.features.find { it.group == GameFeature.WordSpotting }
        game.wordAverageLengthGoal = WORD_LENGTHS[difficulty]
    }

    int getOrder() {
        return DEFAULT_ORDER - 20
    }
}
