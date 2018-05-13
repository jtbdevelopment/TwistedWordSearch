package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Date: 8/16/16 Time: 7:11 AM
 */
public class NumberOfWordsInitializerTest {

  private NumberOfWordsInitializer initializer = new NumberOfWordsInitializer();
  private TWSGame game;

  @Before
  public void setUp() throws Exception {
    game = new TWSGame();
    game.setUsableSquares(100);
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(GameInitializer.DEFAULT_ORDER - 10, initializer.getOrder());
  }

  @Test
  public void testInitializeBeginnerGame() {
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.EasiestDifficulty)));
    game.setWordAverageLengthGoal(5);
    initializer.initializeGame(game);

    Assert.assertEquals(4, game.getNumberOfWords());
  }

  @Test
  public void testInitializeExperiencedGame() {
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.StandardDifficulty)));
    game.setWordAverageLengthGoal(5);
    initializer.initializeGame(game);

    Assert.assertEquals(5, game.getNumberOfWords());
  }

  @Test
  public void testInitializeExpertGame() {
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.HarderDifficulty)));
    game.setWordAverageLengthGoal(5);
    initializer.initializeGame(game);

    Assert.assertEquals(6, game.getNumberOfWords());
  }

  @Test
  public void testInitializeProfessionalGame() {
    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.FiendishDifficulty)));
    game.setWordAverageLengthGoal(5);
    initializer.initializeGame(game);

    Assert.assertEquals(7, game.getNumberOfWords());
  }
}
