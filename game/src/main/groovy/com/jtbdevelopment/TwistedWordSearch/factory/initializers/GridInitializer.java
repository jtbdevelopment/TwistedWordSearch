package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Date: 7/18/16 Time: 12:15 PM
 */
@Component
public class GridInitializer implements GameInitializer<TWSGame> {

  private static final Map<GameFeature, Integer> GRID_ROWS = new HashMap<GameFeature, Integer>() {
    {
      put(GameFeature.Grid20X20, 20);
      put(GameFeature.Grid30X30, 30);
      put(GameFeature.Grid40X40, 40);
      put(GameFeature.Grid50X50, 50);
      put(GameFeature.CircleX31, 31);
      put(GameFeature.CircleX41, 41);
      put(GameFeature.CircleX51, 51);
      put(GameFeature.PyramidX40, 20);
      put(GameFeature.PyramidX50, 25);
      put(GameFeature.Diamond30X30, 30);
      put(GameFeature.Diamond40X40, 40);
      put(GameFeature.Diamond50X50, 50);

    }
  };
  private static final Map<GameFeature, Integer> GRID_COLS = new HashMap<GameFeature, Integer>() {
    {
      put(GameFeature.Grid20X20, 20);
      put(GameFeature.Grid30X30, 30);
      put(GameFeature.Grid40X40, 40);
      put(GameFeature.Grid50X50, 50);
      put(GameFeature.CircleX31, 31);
      put(GameFeature.CircleX41, 41);
      put(GameFeature.CircleX51, 51);
      put(GameFeature.PyramidX40, 40);
      put(GameFeature.PyramidX50, 50);
      put(GameFeature.Diamond30X30, 30);
      put(GameFeature.Diamond40X40, 40);
      put(GameFeature.Diamond50X50, 50);

    }
  };

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature gridType = game.getFeatures().stream()
        .filter(f -> GameFeature.Grid.equals(f.getGroup())).findFirst().get();
    game.setGrid(new Grid(GRID_ROWS.get(gridType), GRID_COLS.get(gridType)));
  }

  public int getOrder() {
    return EARLY_ORDER;
  }

}
