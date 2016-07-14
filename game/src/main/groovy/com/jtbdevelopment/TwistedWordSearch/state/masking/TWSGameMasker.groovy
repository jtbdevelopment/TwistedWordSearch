package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.TWSGameFeature
import com.jtbdevelopment.games.state.masking.AbstractMultiPlayerGameMasker
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 7:07 PM
 */
@Component
@CompileStatic
class TWSGameMasker extends AbstractMultiPlayerGameMasker<ObjectId, TWSGameFeature, TWSGame, TWSMaskedGame> {
    protected TWSMaskedGame newMaskedGame() {
        return new TWSMaskedGame()
    }

    Class<ObjectId> getIDClass() {
        return ObjectId.class
    }
}
