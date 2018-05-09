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
public class PyramidGridInitializer implements GameInitializer<TWSGame> {
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

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature gridType = game.getFeatures()
        .stream()
        .filter(f -> GameFeature.Grid.equals(f.getGroup()))
        .findFirst()
        .get();

    if (gridType.toString().startsWith("Pyramid")) {
      final int halfColumns = game.getGrid().getColumns() / 2;
      for (int row = 0; row < game.getGrid().getRows(); ++row) {
        int splits = halfColumns - row - 1;
        if (splits > 0) {
          for (int split = 0; split < splits; ++split) {
            Integer mirrorColumn = game.getGrid().getColumnUpperBound() - split;
            game.getGrid().setGridCell(row, mirrorColumn, Grid.SPACE);
            game.getGrid().setGridCell(row, split, Grid.SPACE);

          }
        }
      }
    }
  }

  public int getOrder() {
    return EARLY_ORDER + 10;
  }

}
