package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/15/16
 * Time: 6:29 PM
 */
@Component
@CompileStatic
class CircleGridInitializer implements GameInitializer<TWSGame> {
    void initializeGame(final TWSGame game) {
        GameFeature gridType = game.features.find { it.group == GameFeature.Grid }

        if (gridType.toString().startsWith("Circle")) {
            clearGrid(game.grid)
            drawPerimeter(game.grid)
            fillCircle(game.grid)
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void fillCircle(final Grid grid) {
        (0..grid.rowUpperBound).each {
            int row ->
                char[] cells = grid.getGridRow(row)
                int first = cells.findIndexOf {
                    it == '?' as char
                }
                int last = cells.findLastIndexOf {
                    it == '?' as char
                }
                (first..last).each {
                    cells[it] = '?' as char
                }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void clearGrid(final Grid grid) {
        (0..(grid.rowUpperBound)).each {
            int row ->
                (0..grid.columnUpperBound).each {
                    int col ->
                        grid.setGridCell(row, col, ' ' as char);
                }
        }
    }

    private void drawPerimeter(final Grid grid) {
//  https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
        int radius = (int) (grid.columns / 2);
        int centerX = (int) (grid.columns / 2)
        int centerY = (int) (grid.rows / 2)

        int x = radius
        int y = 0
        int err = 0
        char q = '?' as char
        while (x >= y) {
            grid.setGridCell(centerX + x, centerY + y, q)
            grid.setGridCell(centerX + y, centerY + x, q)
            grid.setGridCell(centerX - y, centerY + x, q)
            grid.setGridCell(centerX - x, centerY + y, q)
            grid.setGridCell(centerX - x, centerY - y, q)
            grid.setGridCell(centerX - y, centerY - x, q)
            grid.setGridCell(centerX + y, centerY - x, q)
            grid.setGridCell(centerX + x, centerY - y, q)

            y += 1
            err += (1 + 2 * y)
            if ((2 * (err - x) + 1) > 0) {
                x -= 1
                err += (1 - 2 * x)
            }
        }
    }

    int getOrder() {
        return EARLY_ORDER + 10
    }
}
