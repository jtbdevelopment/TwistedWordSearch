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
        (1..grid.rows).each {
            int row ->
                char[] cells = grid.gridCells[row - 1]
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
        (1..grid.rows).each {
            int row ->
                (1..grid.columns).each {
                    int col ->
                        grid.gridCells[row - 1][col - 1] = ' ' as char;
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
        while (x >= y) {
            setCell(grid, centerX + x, centerY + y)
            setCell(grid, centerX + y, centerY + x)
            setCell(grid, centerX - y, centerY + x)
            setCell(grid, centerX - x, centerY + y)
            setCell(grid, centerX - x, centerY - y)
            setCell(grid, centerX - y, centerY - x)
            setCell(grid, centerX + y, centerY - x)
            setCell(grid, centerX + x, centerY - y)

            y += 1
            err += (1 + 2 * y)
            if ((2 * (err - x) + 1) > 0) {
                x -= 1
                err += (1 - 2 * x)
            }
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private void setCell(final Grid grid, final int x, final int y) {
        grid.gridCells[y][x] = '?' as char
    }

    int getOrder() {
        return EARLY_ORDER + 10
    }
}
