package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import org.springframework.stereotype.Component;

/**
 * Date: 12/20/16 Time: 6:40 PM
 */
@Component
public class HintsRemainingInitializer implements GameInitializer<TWSGame> {

  public void initializeGame(final TWSGame game) {
    game.setHintsRemaining((game.getNumberOfWords() / 4));
  }

  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
