package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 8/15/16 Time: 6:29 PM
 */
@Component
public class DiamondGridInitializer implements GameInitializer<TWSGame> {
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

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature gridType = game.getFeatures()
        .stream()
        .filter(f -> GameFeature.Grid.equals(f.getGroup()))
        .findFirst()
        .get();

    if (gridType.toString().startsWith("Diamond")) {
      final int halfColumns = game.getGrid().getColumns() / 2;
      int halfRows = game.getGrid().getColumns() / 2;
      for (int row = 0; row < halfRows; ++row) {
        int splits = halfColumns - row - 1;
        final int mirrorRow = game.getGrid().getRowUpperBound() - row;
        if (splits > 0) {
          for (int split = 0; split < splits; ++split) {
            Integer mirrorColumn = game.getGrid().getColumnUpperBound() - split;
            game.getGrid().setGridCell(row, mirrorColumn, Grid.SPACE);
            game.getGrid().setGridCell(row, split, Grid.SPACE);
            game.getGrid().setGridCell(mirrorRow, mirrorColumn, Grid.SPACE);
            game.getGrid().setGridCell(mirrorRow, split, Grid.SPACE);
          }
        }
      }
    }

  }

  public int getOrder() {
    return EARLY_ORDER + 10;
  }

}
