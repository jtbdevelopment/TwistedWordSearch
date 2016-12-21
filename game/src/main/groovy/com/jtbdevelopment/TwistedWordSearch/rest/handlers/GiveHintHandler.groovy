package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoHintsRemainException
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 12/20/16
 * Time: 6:34 PM
 */
@CompileStatic
@Component
class GiveHintHandler extends AbstractGameActionHandler<Integer, TWSGame> {
    private Random random = new Random()

    //  will be ignoring param
    protected TWSGame handleActionInternal(final Player player, final TWSGame game, final Integer param) {
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
        return game
    }

    private GridCoordinate generateCoordinate(TWSGame game, String word) {
        GridCoordinate hintCoordinate = new GridCoordinate(game.wordStarts[word])

        int adjustColumn = random.nextInt(3) - 1
        int adjustRow = random.nextInt(3) - 1
        if (hintCoordinate.row == 0) {
            adjustRow += 1
        }
        if (hintCoordinate.row == game.grid.rows - 1) {
            adjustRow -= 1
        }
        if (hintCoordinate.column == 0) {
            adjustColumn += 1
        }
        if (hintCoordinate.column == game.grid.columns - 1) {
            adjustColumn -= 1
        }

        hintCoordinate.column += adjustColumn
        hintCoordinate.row += adjustRow

        hintCoordinate
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

