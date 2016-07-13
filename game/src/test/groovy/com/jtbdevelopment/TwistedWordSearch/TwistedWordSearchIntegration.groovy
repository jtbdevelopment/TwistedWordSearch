package com.jtbdevelopment.TwistedWordSearch

import com.jtbdevelopment.TwistedWordSearch.state.masking.TWSMaskedGame
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration

/**
 * Date: 7/13/16
 * Time: 6:57 PM
 */
class TwistedWordSearchIntegration extends AbstractGameIntegration<TWSMaskedGame> {
    Class<TWSMaskedGame> returnedGameClass() {
        return TWSMaskedGame.class
    }
}
