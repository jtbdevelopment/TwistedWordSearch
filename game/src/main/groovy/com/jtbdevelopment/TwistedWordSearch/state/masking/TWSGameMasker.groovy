package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.state.masking.AbstractMultiPlayerGameMasker
import org.bson.types.ObjectId

/**
 * Date: 7/13/16
 * Time: 7:07 PM
 */
class TWSGameMasker extends AbstractMultiPlayerGameMasker<ObjectId, GameFeature, TWSGame, TWSMaskedGame> {
    protected TWSMaskedGame newMaskedGame() {
        return new TWSMaskedGame()
    }

    Class<ObjectId> getIDClass() {
        return ObjectId.class
    }
}
