package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.state.MultiPlayerGame
import com.jtbdevelopment.games.state.masking.AbstractMultiPlayerGameMasker
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

import java.time.ZonedDateTime

/**
 * Date: 7/13/16
 * Time: 7:07 PM
 */
@Component
@CompileStatic
class GameMasker extends AbstractMultiPlayerGameMasker<ObjectId, GameFeature, TWSGame, MaskedGame> {
    protected MaskedGame newMaskedGame() {
        return new MaskedGame()
    }

    Class<ObjectId> getIDClass() {
        return ObjectId.class
    }

    @Override
    protected void copyUnmaskedData(
            final MultiPlayerGame<ObjectId, ZonedDateTime, GameFeature> game,
            final MaskedMultiPlayerGame<GameFeature> playerMaskedGame) {
        super.copyUnmaskedData(game, playerMaskedGame)

        TWSGame twsGame = (TWSGame) game
        MaskedGame twsMaskedGame = (MaskedGame) playerMaskedGame
        twsMaskedGame.grid = twsGame.grid.gridCells
        twsMaskedGame.wordsToFind = new TreeSet<>(twsGame.wordsToFind)
        twsMaskedGame.foundWordLocations = twsGame.foundWordLocations
    }

    @Override
    protected void copyMaskedData(
            final MultiPlayerGame<ObjectId, ZonedDateTime, GameFeature> game,
            final Player<ObjectId> player,
            final MaskedMultiPlayerGame<GameFeature> playerMaskedGame,
            final Map<ObjectId, Player<ObjectId>> idMap) {
        super.copyMaskedData(game, player, playerMaskedGame, idMap)

        TWSGame twsGame = (TWSGame) game
        MaskedGame twsMaskedGame = (MaskedGame) playerMaskedGame
        twsMaskedGame.wordsFoundByPlayer = game.players.collectEntries {
            [(it.md5): new TreeSet(twsGame.wordsFoundByPlayer[it.id])]
        }
    }
}
