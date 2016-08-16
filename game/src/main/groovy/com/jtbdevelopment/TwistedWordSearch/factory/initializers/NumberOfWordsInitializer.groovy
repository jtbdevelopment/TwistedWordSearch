package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/16/16
 * Time: 6:36 AM
 */
@Component
@CompileStatic
class NumberOfWordsInitializer implements GameInitializer<TWSGame> {
    private static final Map<GameFeature, Integer> FILL_PERCENTAGE = [
            (GameFeature.BeginnerDifficulty)    : 20,
            (GameFeature.ExperiencedDifficulty) : 25,
            (GameFeature.ExpertDifficulty)      : 30,
            (GameFeature.ProfessionalDifficulty): 35,
    ]

    void initializeGame(final TWSGame game) {
        GameFeature difficulty = game.features.find { it.group == GameFeature.WordDifficulty }
        game.numberOfWords = (int) (
                (game.usableSquares * FILL_PERCENTAGE[difficulty] / 100) /
                        game.wordAverageLengthGoal
        )
    }

    int getOrder() {
        return DEFAULT_ORDER - 10
    }
}
