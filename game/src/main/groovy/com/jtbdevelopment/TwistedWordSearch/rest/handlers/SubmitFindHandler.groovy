package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.exceptions.NoWordToFindAtCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 9/3/2016
 * Time: 3:37 PM
 */
@CompileStatic
@Component
class SubmitFindHandler extends AbstractGameActionHandler<List<GridCoordinate>, TWSGame> {
    protected TWSGame handleActionInternal(
            final Player player, final TWSGame game, final List<GridCoordinate> relativeCoordinates) {
        validateCoordinates(game, relativeCoordinates)

        List<GridCoordinate> absoluteCoordinates = collectCoordinates(game, relativeCoordinates)
        String word = collectWord(game, absoluteCoordinates)
        if (game.wordsToFind.contains(word)) {
            updateGameForFoundWord(game, player, word, absoluteCoordinates)
        } else {
            String reverse = word.reverse()
            if (game.wordsToFind.contains(reverse)) {
                updateGameForFoundWord(game, player, reverse, absoluteCoordinates)
            } else {
                throw new NoWordToFindAtCoordinatesException()
            }
        }
        return game
    }

    protected static void updateGameForFoundWord(
            final TWSGame game,
            final Player player, final String word, final List<GridCoordinate> absoluteCoordiantes) {
        game.wordsToFind.remove(word)
        game.wordsFoundByPlayer[(ObjectId) player.id].add(word)
        game.foundWordLocations[word] = absoluteCoordiantes as Set
    }

    private static List<GridCoordinate> collectCoordinates(
            final TWSGame game, final List<GridCoordinate> relativeCoordinates) {
        List<GridCoordinate> absolute = [new GridCoordinate(relativeCoordinates[0])];
        GridCoordinate coordinate = new GridCoordinate(relativeCoordinates[0])
        (1..(relativeCoordinates.size() - 1)).each {
            int index ->
                GridCoordinate adjust = relativeCoordinates[index]
                coordinate.row += adjust.row
                coordinate.column += adjust.column
                if (coordinate.row < 0) {
                    coordinate.row = game.grid.rowUpperBound
                }
                if (coordinate.column < 0) {
                    coordinate.column = game.grid.columnUpperBound
                }
                if (coordinate.row > game.grid.rowUpperBound) {
                    coordinate.row = 0
                }
                if (coordinate.column > game.grid.columnUpperBound) {
                    coordinate.column = 0
                }
                absolute.add(new GridCoordinate(coordinate))
        }
        absolute
    }

    private static String collectWord(final TWSGame game, final List<GridCoordinate> absoluteCoordinates) {
        char[] letters = absoluteCoordinates.collect {
            game.grid.getGridCell(it)
        }
        return new String(letters)
    }

    private static void validateCoordinates(final TWSGame game, final List<GridCoordinate> relativeCoordinates) {
        if (relativeCoordinates == null || relativeCoordinates.size() < 2) {
            throw new InvalidWordFindCoordinatesException()
        }

        if (relativeCoordinates.size() > Math.max(game.grid.rows, game.grid.columns)) {
            throw new InvalidWordFindCoordinatesException()
        }

        GridCoordinate start = relativeCoordinates[0]

        if (start.column < 0 || start.row < 0 || start.column > game.grid.columnUpperBound || start.row > game.grid.rowUpperBound) {
            throw new InvalidStartCoordinateException()
        }
        if (game.grid.getGridCell(start) == Grid.SPACE) {
            throw new InvalidStartCoordinateException()
        }

        GridCoordinate next = relativeCoordinates[1]
        if ((next.column == 0 && next.column == 0) || next.column < -1 || next.column > 1 || next.row < -1 || next.row > 1) {
            throw new InvalidWordFindCoordinatesException();
        }
        List<GridCoordinate> remaining = new ArrayList<>(relativeCoordinates)
        remaining.remove(0)
        remaining.remove(0)

        if (remaining.find { it.row != next.row || it.column != next.column } != null) {
            throw new InvalidWordFindCoordinatesException()
        }
    }
}

