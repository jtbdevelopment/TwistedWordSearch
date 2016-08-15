package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 7/18/16
 * Time: 12:15 PM
 */
@Component
@CompileStatic
class GridInitializer implements GameInitializer<TWSGame> {
    private final static Map<GameFeature, Integer> GRID_ROWS = [
            (GameFeature.Grid30X30)   : 30,
            (GameFeature.Grid40X40)   : 40,
            (GameFeature.Grid50X50)   : 50,
            (GameFeature.CircleX40)   : 40,
            (GameFeature.CircleX50)   : 50,
            (GameFeature.PyramidX40)  : 20,
            (GameFeature.PyramidX50)  : 25,
            (GameFeature.Diamond40x40): 40,
            (GameFeature.Diamond50x50): 50,
    ]

    private final static Map<GameFeature, Integer> GRID_COLS = [
            (GameFeature.Grid30X30)   : 30,
            (GameFeature.Grid40X40)   : 40,
            (GameFeature.Grid50X50)   : 50,
            (GameFeature.CircleX40)   : 40,
            (GameFeature.CircleX50)   : 50,
            (GameFeature.PyramidX40)  : 40,
            (GameFeature.PyramidX50)  : 50,
            (GameFeature.Diamond40x40): 40,
            (GameFeature.Diamond50x50): 50,
    ]

    void initializeGame(final TWSGame game) {
        GameFeature gridType = game.features.find { it.group == GameFeature.Grid }
        game.grid = new Grid(GRID_ROWS[gridType], GRID_COLS[gridType])

/*

        '       XXXXXX       '
        '     XXXXXXXXXX     '
        '   XXXXXXXXXXXXXX   '
        '  XXXXXXXXXXXXXXXX  '
        '  XXXXXXXXXXXXXXXX  '
        ' XXXXXXXXXXXXXXXXXX '
        ' XXXXXXXXXXXXXXXXXX '
        'XXXXXXXXXXXXXXXXXXXX'
        'XXXXXXXXXXXXXXXXXXXX'
        'XXXXXXXXXXXXXXXXXXXX'
        'XXXXXXXXXXXXXXXXXXXX'
        'XXXXXXXXXXXXXXXXXXXX'
        'XXXXXXXXXXXXXXXXXXXX'
        ' XXXXXXXXXXXXXXXXXX '
        ' XXXXXXXXXXXXXXXXXX '
        '  XXXXXXXXXXXXXXXX  '
        '  XXXXXXXXXXXXXXXX  '
        '   XXXXXXXXXXXXXX   '
        '     XXXXXXXXXX     '
        '       XXXXXX       '
*/
    }

    int getOrder() {
        return EARLY_ORDER
    }
}
