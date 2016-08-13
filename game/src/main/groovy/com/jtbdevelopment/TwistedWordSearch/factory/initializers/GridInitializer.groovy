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
            (GameFeature.Grid30X30) : 10,
            (GameFeature.Grid40X40) : 20,
            (GameFeature.Grid50X50) : 40,
            (GameFeature.CircleX40) : 20,
            (GameFeature.CircleX50) : 40,
            (GameFeature.PyramidX40): 20,
            (GameFeature.PyramidX50): 40,
    ]

    private final static Map<GameFeature, Integer> GRID_COLS = [
            (GameFeature.Grid30X30) : 10,
            (GameFeature.Grid40X40) : 20,
            (GameFeature.Grid40X40) : 40,
            (GameFeature.CircleX40) : 20,
            (GameFeature.CircleX50) : 40,
            (GameFeature.PyramidX40): 20,
            (GameFeature.PyramidX50): 40,
    ]

    void initializeGame(final TWSGame game) {
        GameFeature gridType = game.features.find { it.group == GameFeature.Grid }
        Grid grid = new Grid(GRID_ROWS[gridType], GRID_COLS[gridType])

/*
        //  Pryamid x 20 - too small 40 min
        '         XX         '
        '        XXXX        '
        '       XXXXXX       '
        '      XXXXXXXX      '
        '     XXXXXXXXXX     '
        '    XXXXXXXXXXXX    '
        '   XXXXXXXXXXXXXX   '
        '  XXXXXXXXXXXXXXXX  '
        ' XXXXXXXXXXXXXXXXXX '
        'XXXXXXXXXXXXXXXXXXXX'

        //  Diamond 20x20 - 30x30 probably
        '         XX         '
        '        XXXX        '
        '       XXXXXX       '
        '      XXXXXXXX      '
        '     XXXXXXXXXX     '
        '    XXXXXXXXXXXX    '
        '   XXXXXXXXXXXXXX   '
        '  XXXXXXXXXXXXXXXX  '
        ' XXXXXXXXXXXXXXXXXX '
        'XXXXXXXXXXXXXXXXXXXX'
        'XXXXXXXXXXXXXXXXXXXX'
        ' XXXXXXXXXXXXXXXXXX '
        '  XXXXXXXXXXXXXXXX  '
        '   XXXXXXXXXXXXXX   '
        '    XXXXXXXXXXXX    '
        '     XXXXXXXXXX     '
        '      XXXXXXXX      '
        '       XXXXXX       '
        '        XXXX        '
        '         XX         '

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
