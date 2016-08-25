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
class DiamondGridInitializer implements GameInitializer<TWSGame> {
//    Diamond 20x20 - 30x30 probably
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
//    'XXXXXXXXXXXXXXXXXXXX'
//    ' XXXXXXXXXXXXXXXXXX '
//    '  XXXXXXXXXXXXXXXX  '
//    '   XXXXXXXXXXXXXX   '
//    '    XXXXXXXXXXXX    '
//    '     XXXXXXXXXX     '
//    '      XXXXXXXX      '
//    '       XXXXXX       '
//    '        XXXX        '
//    '         XX         '
    void initializeGame(final TWSGame game) {
        GameFeature gridType = game.features.find { it.group == GameFeature.Grid }
        if (gridType.toString().startsWith("Diamond")) {
            int halfColumns = (int) (game.grid.columns / 2)
            int halfRows = (int) (game.grid.columns / 2)
            (0..(halfRows - 1)).each {
                int row ->
                    int splits = halfColumns - row - 1
                    int mirrorRow = game.grid.rowUpperBound - row
                    if (splits > 0) {
                        (0..(splits - 1)).each {
                            int split ->
                                def mirrorColumn = game.grid.columnUpperBound - split
                                game.grid.setGridCell(row, mirrorColumn, Grid.SPACE)
                                game.grid.setGridCell(row, split, Grid.SPACE)
                                game.grid.setGridCell(mirrorRow, mirrorColumn, Grid.SPACE)
                                game.grid.setGridCell(mirrorRow, split, Grid.SPACE)
                        }
                    }
            }
        }
    }

    int getOrder() {
        return EARLY_ORDER + 10
    }
}
