package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.mongo.state.masking.AbstractMongoMultiPlayerGameMasker
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 7/13/16
 * Time: 7:07 PM
 */
@Component
@CompileStatic
class GameMasker extends AbstractMongoMultiPlayerGameMasker<GameFeature, TWSGame, MaskedGame> {
    protected MaskedGame newMaskedGame() {
        return new MaskedGame()
    }

    @Override
    protected void copyUnmaskedData(final TWSGame twsGame, final MaskedGame playerMaskedGame) {
        super.copyUnmaskedData(twsGame, playerMaskedGame)
        MaskedGame twsMaskedGame = (MaskedGame) playerMaskedGame
        if (twsGame.gamePhase != GamePhase.Challenged && twsGame.gamePhase != GamePhase.Setup) {
            twsMaskedGame.grid = twsGame.grid.gridCells
            twsMaskedGame.wordsToFind = new TreeSet<>(twsGame.wordsToFind)
            twsMaskedGame.foundWordLocations = twsGame.foundWordLocations
        }
        twsMaskedGame.hints = twsGame.hintsGiven.values() as Set
        twsMaskedGame.hintsRemaining = twsGame.hintsRemaining
    }

    @Override
    protected void copyMaskedData(
            final TWSGame twsGame,
            final Player<ObjectId> player,
            final MaskedGame playerMaskedGame, final Map<ObjectId, Player<ObjectId>> idMap) {
        super.copyMaskedData(twsGame, player, playerMaskedGame, idMap)

        MaskedGame twsMaskedGame = (MaskedGame) playerMaskedGame
        twsMaskedGame.scores = twsGame.players.collectEntries {
            [(it.md5): twsGame.scores[it.id]]
        }

        twsMaskedGame.hintsTaken = twsGame.players.collectEntries {
            [(it.md5): twsGame.hintsTaken[it.id]]
        }

        if (twsGame.gamePhase != GamePhase.Challenged && twsGame.gamePhase != GamePhase.Setup) {
            twsMaskedGame.wordsFoundByPlayer = twsGame.players.collectEntries {
                [(it.md5): new TreeSet(twsGame.wordsFoundByPlayer[it.id])]
            }
        }
    }
}
