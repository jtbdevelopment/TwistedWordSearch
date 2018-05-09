package com.jtbdevelopment.TwistedWordSearch.factory.validators;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameValidator;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Date: 4/20/15 Time: 6:48 PM
 */
@Component
public class OptionsValidator implements GameValidator<TWSGame> {

  @Override
  public boolean validateGame(final TWSGame game) {
    if (game.getFeatures().stream().anyMatch(f -> f.equals(f.getGroup()))) {
      return false;
    }

    Map<GameFeature, GameFeature> groupToFeatures = game.getFeatures().stream()
        .collect(Collectors.toMap(
            GameFeature::getGroup,
            f -> f,
            (a, b) -> a));
    return groupToFeatures.size() == GameFeature.getGroupedFeatures().size()
        && groupToFeatures.size() == game.getFeatures().size();
  }

  @Override
  public String errorMessage() {
    return "Invalid combination of options!";
  }

}
