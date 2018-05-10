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
public class CircleGridInitializer implements GameInitializer<TWSGame> {

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature gridType = game.getFeatures()
        .stream()
        .filter(f -> GameFeature.Grid.equals(f.getGroup()))
        .findFirst()
        .get();

    if (gridType.toString().startsWith("Circle")) {
      clearGrid(game.getGrid());
      drawPerimeter(game.getGrid());
      fillCircle(game.getGrid());
    }

  }

  private void fillCircle(final Grid grid) {
    for (int row = 0; row < grid.getRows(); ++row) {
      char[] cells = grid.getGridRow(row);
      int first = 0;
      while (cells[first] != '?') {
        first++;
      }
      int last = cells.length - 1;
      while (cells[last] != '?') {
        last--;
      }
      for (int col = first; col < last; ++col) {
        grid.setGridCell(row, col, Grid.QUESTION_MARK);
      }
    }
  }

  private void clearGrid(final Grid grid) {
    for (int row = 0; row < grid.getRows(); ++row) {
      for (int col = 0; col < grid.getColumns(); ++col) {
        grid.setGridCell(row, col, Grid.SPACE);
      }
    }
  }

  private void drawPerimeter(final Grid grid) {
//  https://en.wikipedia.org/wiki/Midpoint_circle_algorithm
    int radius = grid.getColumns() / 2;
    int centerX = grid.getColumns() / 2;
    int centerY = grid.getRows() / 2;

    int x = radius;
    int y = 0;
    int err = 0;
    char q = Grid.QUESTION_MARK;
    while (x >= y) {
      grid.setGridCell(centerX + x, centerY + y, q);
      grid.setGridCell(centerX + y, centerY + x, q);
      grid.setGridCell(centerX - y, centerY + x, q);
      grid.setGridCell(centerX - x, centerY + y, q);
      grid.setGridCell(centerX - x, centerY - y, q);
      grid.setGridCell(centerX - y, centerY - x, q);
      grid.setGridCell(centerX + y, centerY - x, q);
      grid.setGridCell(centerX + x, centerY - y, q);

      y += 1;
      err += (1 + 2 * y);
      if ((2 * (err - x) + 1) > 0) {
        x -= 1;
        err += (1 - 2 * x);
      }

    }

  }

  public int getOrder() {
    return EARLY_ORDER + 10;
  }

}
