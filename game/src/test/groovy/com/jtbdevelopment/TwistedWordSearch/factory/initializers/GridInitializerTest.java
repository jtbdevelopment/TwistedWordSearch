package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Collections;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/15/16 Time: 6:55 PM
 */
public class GridInitializerTest {

  private GridInitializer initializer = new GridInitializer();

  @Test
  public void testInitializeGameForSquares() {
    TWSGame game = new TWSGame();

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Grid20X20)));
    initializer.initializeGame(game);
    Assert.assertEquals(20, game.getGrid().getRows());
    Assert.assertEquals(20, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Grid30X30)));
    initializer.initializeGame(game);
    Assert.assertEquals(30, game.getGrid().getRows());
    Assert.assertEquals(30, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Grid40X40)));
    initializer.initializeGame(game);
    Assert.assertEquals(40, game.getGrid().getRows());
    Assert.assertEquals(40, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Grid50X50)));
    initializer.initializeGame(game);
    Assert.assertEquals(50, game.getGrid().getRows());
    Assert.assertEquals(50, game.getGrid().getColumns());
  }

  @Test
  public void testInitializeGameForDiamonds() {
    TWSGame game = new TWSGame();

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Diamond30X30)));
    initializer.initializeGame(game);
    Assert.assertEquals(30, game.getGrid().getRows());
    Assert.assertEquals(30, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Diamond40X40)));
    initializer.initializeGame(game);
    Assert.assertEquals(40, game.getGrid().getRows());
    Assert.assertEquals(40, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.Diamond50X50)));
    initializer.initializeGame(game);
    Assert.assertEquals(50, game.getGrid().getRows());
    Assert.assertEquals(50, game.getGrid().getColumns());
  }

  @Test
  public void testInitializeGameForCircles() {
    TWSGame game = new TWSGame();

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.CircleX31)));
    initializer.initializeGame(game);
    Assert.assertEquals(31, game.getGrid().getRows());
    Assert.assertEquals(31, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.CircleX41)));
    initializer.initializeGame(game);
    Assert.assertEquals(41, game.getGrid().getRows());
    Assert.assertEquals(41, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.CircleX51)));
    initializer.initializeGame(game);
    Assert.assertEquals(51, game.getGrid().getRows());
    Assert.assertEquals(51, game.getGrid().getColumns());
  }

  @Test
  public void testInitializeGameForPyramids() {
    TWSGame game = new TWSGame();

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.PyramidX40)));
    initializer.initializeGame(game);
    Assert.assertEquals(20, game.getGrid().getRows());
    Assert.assertEquals(40, game.getGrid().getColumns());

    game.setFeatures(new HashSet<>(Collections.singletonList(GameFeature.PyramidX50)));
    initializer.initializeGame(game);
    Assert.assertEquals(25, game.getGrid().getRows());
    Assert.assertEquals(50, game.getGrid().getColumns());
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(GameInitializer.EARLY_ORDER, initializer.getOrder());
  }
}
