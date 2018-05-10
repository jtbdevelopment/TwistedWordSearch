package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Date: 8/16/16 Time: 6:56 AM
 */
@Component
public class WordAverageLengthGoalInitializer implements GameInitializer<TWSGame> {

  private static final Map<GameFeature, Integer> WORD_LENGTHS = new HashMap<GameFeature, Integer>() {{
    put(GameFeature.EasiestDifficulty, 10);
    put(GameFeature.StandardDifficulty, 8);
    put(GameFeature.HarderDifficulty, 7);
    put(GameFeature.FiendishDifficulty, 6);
  }};

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature difficulty = game.getFeatures().stream()
        .filter(x -> GameFeature.WordSpotting.equals(x.getGroup()))
        .findFirst()
        .get();
    game.setWordAverageLengthGoal(WORD_LENGTHS.get(difficulty));
  }

  public int getOrder() {
    return DEFAULT_ORDER - 20;
  }
}
