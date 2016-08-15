package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/15/16
 * Time: 6:29 PM
 */
@Component
@CompileStatic
class PyramidGridInitializer implements GameInitializer<TWSGame> {
//  Pryamid x 20 - too small 40 min
//    '         XX         '
//    '        XXXX        '
//    '       XXXXXX       '
//    '      XXXXXXXX      '
//    '     XXXXXXXXXX     '
//    '    XXXXXXXXXXXX    '
//    '   XXXXXXXXXXXXXX   '
//    '  XXXXXXXXXXXXXXXX  '
//    ' XXXXXXXXXXXXXXXXXX '
//    'XXXXXXXXXXXXXXXXXXXX'
    void initializeGame(final TWSGame game) {
        GameFeature gridType = game.features.find { it.group == GameFeature.Grid }
        if (gridType.toString().startsWith("Pyramid")) {
            int halfColumns = (int) (game.grid.columns / 2)
            (0..(game.grid.rows - 1)).each {
                int row ->
                    int splits = halfColumns - row - 1
                    if (splits > 0) {
                        (0..(splits - 1)).each {
                            int split ->
                                def mirrorColumn = (game.grid.columns - 1) - split
                                game.grid.gridCells[row][mirrorColumn] = ' ' as char
                                game.grid.gridCells[row][split] = ' ' as char
                        }
                    }
            }
        }
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
