package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoHintsRemainException
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.events.GamePublisher
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.masking.GameMasker
import com.jtbdevelopment.games.state.transition.GameTransitionEngine
import com.jtbdevelopment.games.tracking.GameEligibilityTracker
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 12/20/16
 * Time: 6:34 PM
 */
@CompileStatic
@Component
class GiveHintHandler extends AbstractGameActionHandler<Integer, ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {
    private final Random random = new Random()

    GiveHintHandler(
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
            final AbstractGameRepository<ObjectId, GameFeature, TWSGame> gameRepository,
            final GameTransitionEngine<TWSGame> transitionEngine,
            final GamePublisher<TWSGame, MongoPlayer> gamePublisher,
            final GameEligibilityTracker gameTracker,
            final GameMasker<ObjectId, TWSGame, MaskedGame> gameMasker) {
        super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker, gameMasker)
    }

//  will be ignoring param
    protected TWSGame handleActionInternal(final MongoPlayer player, final TWSGame game, final Integer param) {
        if (game.gamePhase != GamePhase.Playing) {
            throw new GameIsNotInPlayModeException()
        }

        if (game.hintsRemaining <= 0) {
            throw new NoHintsRemainException()
        }

        String word = pickWord(game)
        GridCoordinate hintCoordinate = generateCoordinate(game, word)
        game.hintsGiven[word] = hintCoordinate
        game.scores[(ObjectId) player.id] -= (word.size() / 2).intValue()
        game.hintsRemaining -= 1
        game.hintsTaken[(ObjectId) player.id] += 1
        return game
    }

    private GridCoordinate generateCoordinate(TWSGame game, String word) {
        GridCoordinate wordStart = game.wordStarts[word]

        int adjustColumn = random.nextInt(3) - 1
        int adjustRow = random.nextInt(3) - 1
        if (wordStart.row == 0) {
            adjustRow += 1
        }
        if (wordStart.row == (game.grid.rows - 1)) {
            adjustRow -= 1
        }
        if (wordStart.column == 0) {
            adjustColumn += 1
        }
        if (wordStart.column == (game.grid.columns - 1)) {
            adjustColumn -= 1
        }

        return new GridCoordinate(wordStart.row + adjustRow, wordStart.column + adjustColumn)
    }

    private String pickWord(TWSGame game) {
        int wordToHint
        wordToHint = random.nextInt(game.wordsToFind.size())
        String word = game.wordsToFind.toList()[wordToHint]
        if (game.hintsGiven.containsKey(word)) {
            return pickWord(game)
        }
        word
    }
}

