package com.jtbdevelopment.TwistedWordSearch.factory.validators

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameValidator
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 4/20/15
 * Time: 6:48 PM
 */
@Component
@CompileStatic
class OptionsValidator implements GameValidator<TWSGame> {
    @Override
    boolean validateGame(final TWSGame game) {
        GameFeature invalidFeature = game.features.find { GameFeature it -> it.group == it }
        if (invalidFeature) {
            return false
        }
        Map<GameFeature, GameFeature> groupToFeature = game.features.collectEntries { GameFeature it -> [(it.group): it] }
        return groupToFeature.size() == GameFeature.groupedFeatures.size() &&
                groupToFeature.size() == game.features.size()
    }

    @Override
    String errorMessage() {
        return "Invalid combination of options!"
    }
}
