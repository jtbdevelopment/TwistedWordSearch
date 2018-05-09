package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 8/16/16 Time: 6:49 AM
 */
@Component
public class UsableSquaresInitializer implements GameInitializer<TWSGame> {

  public void initializeGame(final TWSGame game) {
    game.setUsableSquares(game.getGrid().getUsableSquaresCount());
  }

  public int getOrder() {
    return EARLY_ORDER + 20;
  }

}
