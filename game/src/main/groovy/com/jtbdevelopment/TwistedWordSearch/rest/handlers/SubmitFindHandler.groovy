package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 9/3/2016
 * Time: 3:37 PM
 */
@CompileStatic
@Component
class SubmitFindHandler extends AbstractGameActionHandler<List<GridCoordinate>, TWSGame>{
    protected TWSGame handleActionInternal(final Player player, final TWSGame game, final List<GridCoordinate> coordinates) {
        validateCoordinates(game, coordinates)

        return null
    }

    private static void validateCoordinates(final TWSGame game,final List<GridCoordinate> coordinates) {
        if (coordinates == null || coordinates.size() < 2) {
            throw new InvalidWordFindCoordinatesException()
        }

        GridCoordinate start = coordinates[0]

        if (start.column < 0 || start.row < 0 || start.column > game.grid.columnUpperBound || start.row > game.grid.rowUpperBound) {
            throw new InvalidStartCoordinateException()
        }
        if (game.grid.getGridCell(start) == Grid.SPACE) {
            throw new InvalidStartCoordinateException()
        }

        GridCoordinate next = coordinates[1]
        if ((next.column == 0 && next.column == 0) || next.column < -1 || next.column >= 1 || next.row < -1 || next.row > 1) {
            throw new InvalidWordFindCoordinatesException();
        }
        List<GridCoordinate> remaining = new ArrayList<>(coordinates)
        remaining.remove(0)
        remaining.remove(0)

        if (remaining.find { it.row != next.row || it.column != next.column } != null) {
            throw new InvalidWordFindCoordinatesException()
        }
    }
}

