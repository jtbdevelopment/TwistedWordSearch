package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/16/16 Time: 6:59 AM
 */
public class WordAverageLengthGoalInitializerTest {

  private WordAverageLengthGoalInitializer initializer = new WordAverageLengthGoalInitializer();

  @Test
  public void testGetOrder() {
    Assert.assertEquals(GameInitializer.DEFAULT_ORDER - 20, initializer.getOrder());
  }

  @Test
  public void testBeginnerWordLength() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.EasiestDifficulty)));
    initializer.initializeGame(game);
    Assert.assertEquals(10, game.getWordAverageLengthGoal());
  }

  @Test
  public void testExperiencedWordLength() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.StandardDifficulty)));
    initializer.initializeGame(game);
    Assert.assertEquals(8, game.getWordAverageLengthGoal());
  }

  @Test
  public void testExpertWordLength() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.HarderDifficulty)));
    initializer.initializeGame(game);
    Assert.assertEquals(7, game.getWordAverageLengthGoal());
  }

  @Test
  public void testProfessionalWordLength() {
    TWSGame game = new TWSGame();
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.FiendishDifficulty)));
    initializer.initializeGame(game);
    Assert.assertEquals(6, game.getWordAverageLengthGoal());
  }
}
