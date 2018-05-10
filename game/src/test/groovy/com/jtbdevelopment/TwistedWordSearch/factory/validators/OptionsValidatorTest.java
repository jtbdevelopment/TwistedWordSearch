package com.jtbdevelopment.TwistedWordSearch.factory.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 4/20/15 Time: 6:49 PM
 */
public class OptionsValidatorTest {

  private OptionsValidator validator = new OptionsValidator();

  @Test
  public void testGameFailsIfMissingAnOptionFromAGroup() {
    GameFeature.getGroupedFeatures().keySet().forEach(ignoredOptions -> {
      Set<GameFeature> options = GameFeature.getGroupedFeatures().entrySet()
          .stream()
          .filter(g -> ignoredOptions != g.getKey())
          .map(g -> g.getValue().get(0))
          .collect(Collectors.toSet());
      TWSGame game = new TWSGame();
      game.setFeatures(options);
      assertFalse(validator.validateGame(game));
    });
  }

  @Test
  public void testGameFailsIfUsingGroupingOptions() {
    GameFeature.getGroupedFeatures().keySet().forEach(groupToMisUse -> {
      Set<GameFeature> options = GameFeature.getGroupedFeatures().entrySet()
          .stream()
          .filter(g -> groupToMisUse != g.getKey())
          .map(g -> g.getValue().get(0))
          .collect(Collectors.toSet());
      options.add(groupToMisUse);
      TWSGame game = new TWSGame();
      game.setFeatures(options);
      assertFalse(validator.validateGame(game));
    });
  }

  @Test
  public void testGameFailsIfOneGroupHasMultipleOptionsFromOneGroup() {
    GameFeature.getGroupedFeatures().keySet().forEach(groupToMisUse -> {
      Set<GameFeature> options = GameFeature.getGroupedFeatures().entrySet()
          .stream()
          .filter(g -> groupToMisUse != g.getKey())
          .map(g -> g.getValue().get(0))
          .collect(Collectors.toSet());
      options.add(GameFeature.getGroupedFeatures().get(groupToMisUse).get(0));
      options.add(GameFeature.getGroupedFeatures().get(groupToMisUse).get(1));
      TWSGame game = new TWSGame();
      game.setFeatures(options);
      assertFalse(validator.validateGame(game));
    });
  }

  @Test
  public void testAValidGame() {
    Set<GameFeature> options = GameFeature.getGroupedFeatures().entrySet()
        .stream()
        .map(g -> g.getValue().get(0))
        .collect(Collectors.toSet());
    TWSGame game = new TWSGame();
    game.setFeatures(options);
    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testErrorMessage() {
    Assert.assertEquals("Invalid combination of options!", validator.errorMessage());
  }
}
