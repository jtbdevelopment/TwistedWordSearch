package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/15/16
 * Time: 6:35 PM
 */
class DiamondGridInitializerTest extends GroovyTestCase {
    DiamondGridInitializer initializer = new DiamondGridInitializer()

    void testInitializeGame() {
        TWSGame game = new TWSGame()
        game.features = [GameFeature.Diamond50x50] as Set
        game.grid = new Grid(20, 20)
        initializer.initializeGame(game)

        assert '         ??         ' == game.grid.gridCells[0].toString()
        assert '        ????        ' == game.grid.gridCells[1].toString()
        assert '       ??????       ' == game.grid.gridCells[2].toString()
        assert '      ????????      ' == game.grid.gridCells[3].toString()
        assert '     ??????????     ' == game.grid.gridCells[4].toString()
        assert '    ????????????    ' == game.grid.gridCells[5].toString()
        assert '   ??????????????   ' == game.grid.gridCells[6].toString()
        assert '  ????????????????  ' == game.grid.gridCells[7].toString()
        assert ' ?????????????????? ' == game.grid.gridCells[8].toString()
        assert '????????????????????' == game.grid.gridCells[9].toString()
        assert '????????????????????' == game.grid.gridCells[10].toString()
        assert ' ?????????????????? ' == game.grid.gridCells[11].toString()
        assert '  ????????????????  ' == game.grid.gridCells[12].toString()
        assert '   ??????????????   ' == game.grid.gridCells[13].toString()
        assert '    ????????????    ' == game.grid.gridCells[14].toString()
        assert '     ??????????     ' == game.grid.gridCells[15].toString()
        assert '      ????????      ' == game.grid.gridCells[16].toString()
        assert '       ??????       ' == game.grid.gridCells[17].toString()
        assert '        ????        ' == game.grid.gridCells[18].toString()
        assert '         ??         ' == game.grid.gridCells[19].toString()
    }

    void testIgnoresNonDiamond() {
        GameFeature.values().findAll {
            it.group == GameFeature.Grid && !it.toString().startsWith("Diamond")
        }.each {
            TWSGame game = new TWSGame()
            game.grid = new Grid(40, 40)
            game.features = [it] as Set
            initializer.initializeGame(game)
            game.grid.gridCells.each {
                it.each {
                    assert '?' as char == it
                }
            }
        }
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }
}
