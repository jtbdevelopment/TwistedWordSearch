package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Date: 8/16/16 Time: 6:36 AM
 */
@Component
public class NumberOfWordsInitializer implements GameInitializer<TWSGame> {

  private static final Map<GameFeature, Integer> FILL_PERCENTAGE = new HashMap<GameFeature, Integer>() {
    {
      put(GameFeature.EasiestDifficulty, 20);
      put(GameFeature.StandardDifficulty, 25);
      put(GameFeature.HarderDifficulty, 30);
      put(GameFeature.FiendishDifficulty, 35);
    }
  };

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature difficulty = game.getFeatures().stream()
        .filter(f -> GameFeature.WordSpotting.equals(f.getGroup())).findFirst().get();
    game.setNumberOfWords(
        ((game.getUsableSquares() * FILL_PERCENTAGE.get(difficulty) / 100) / game
            .getWordAverageLengthGoal()));
  }

  public int getOrder() {
    return DEFAULT_ORDER - 10;
  }
}
