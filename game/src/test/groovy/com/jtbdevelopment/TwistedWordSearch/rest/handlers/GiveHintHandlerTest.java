package com.jtbdevelopment.TwistedWordSearch.rest.handlers;

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoHintsRemainException;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 12/20/16 Time: 9:21 PM
 */
public class GiveHintHandlerTest extends MongoGameCoreTestCase {

  private GiveHintHandler handler = new GiveHintHandler(null, null, null, null, null, null);

  @Test
  public void testIgnoresNonPlayingGames() {
    final TWSGame game = new TWSGame();
    game.setWordsToFind(new HashSet<>(Collections.singletonList("FIND")));
    game.setGrid(new Grid(10, 10));
    LinkedHashMap<String, GridCoordinate> map = new LinkedHashMap<>(1);
    map.put("FIND", new GridCoordinate(3, 3));
    game.setWordStarts(map);
    LinkedHashMap<ObjectId, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(PONE.getId(), 10);
    game.setScores(map1);

    Arrays.stream(GamePhase.values()).filter(p -> !GamePhase.Playing.equals(p)).forEach(it -> {
      try {
        handler.handleActionInternal(PONE, game, null);
        Assert.fail("Should have exceptioned");
      } catch (GameIsNotInPlayModeException ignore) {
//
      }

    });
  }

  @Test
  public void testFailsIfNotHintsRemain() {
    TWSGame game = new TWSGame();
    game.setWordsToFind(new HashSet<>(Collections.singletonList("FIND")));
    game.setGrid(new Grid(10, 10));
    LinkedHashMap<String, GridCoordinate> map = new LinkedHashMap<>(1);
    map.put("FIND", new GridCoordinate(3, 3));
    game.setWordStarts(map);
    LinkedHashMap<ObjectId, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(PONE.getId(), 10);
    game.setScores(map1);
    game.setGamePhase(GamePhase.Playing);
    game.setHintsRemaining(0);

    try {
      handler.handleActionInternal(PONE, game, null);
    } catch (NoHintsRemainException ignore) {
      return;

    }

    Assert.fail("should have exceptioned");
  }

  @Test
  public void testGivesAHintForSingleWordPuzzle() {
    TWSGame game = new TWSGame();
    game.setWordsToFind(new HashSet<>(Collections.singletonList("FIND")));
    game.setGrid(new Grid(10, 10));
    game.setGamePhase(GamePhase.Playing);
    game.setHintsRemaining(2);
    LinkedHashMap<ObjectId, Integer> map = new LinkedHashMap<>(1);
    map.put(PONE.getId(), 4);
    game.setHintsTaken(map);
    LinkedHashMap<String, GridCoordinate> map1 = new LinkedHashMap<>(1);
    map1.put("FIND", new GridCoordinate(3, 3));
    game.setWordStarts(map1);
    LinkedHashMap<ObjectId, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(PONE.getId(), 10);
    game.setScores(map2);

    TWSGame r = handler.handleActionInternal(PONE, game, null);
    Assert.assertSame(game, r);

    Assert.assertEquals(8, (int) game.getScores().get(PONE.getId()));
    Assert.assertEquals(1, game.getHintsGiven().size());
    Assert.assertEquals(1, game.getHintsRemaining());
    Assert.assertTrue(game.getHintsGiven().containsKey("FIND"));
    GridCoordinate c = game.getHintsGiven().get("FIND");
    Assert.assertTrue(2 <= c.getColumn());
    Assert.assertTrue(4 >= c.getColumn());
    Assert.assertTrue(2 <= c.getRow());
    Assert.assertTrue(4 >= c.getRow());
    Assert.assertEquals(5, (int) game.getHintsTaken().get(PONE.getId()));
  }

  @Test
  public void testGivesAHintForTwoWordPuzzleEventuallyReturnsBoth() {
    TWSGame game = new TWSGame();
    game.setWordsToFind(new HashSet<>(Arrays.asList("FIND", "ME")));
    game.setGrid(new Grid(10, 10));
    game.setGamePhase(GamePhase.Playing);
    LinkedHashMap<String, GridCoordinate> map = new LinkedHashMap<>(2);
    map.put("FIND", new GridCoordinate(3, 3));
    map.put("ME", new GridCoordinate(0, 9));
    game.setWordStarts(map);
    LinkedHashMap<ObjectId, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(PONE.getId(), 10);
    game.setScores(map1);
    game.setHintsRemaining(2);
    LinkedHashMap<ObjectId, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(PONE.getId(), 0);
    game.setHintsTaken(map2);

    TWSGame r = handler.handleActionInternal(PONE, game, null);
    Assert.assertSame(game, r);
    Assert.assertEquals(1, (int) game.getHintsTaken().get(PONE.getId()));
    r = handler.handleActionInternal(PONE, game, null);
    Assert.assertSame(game, r);

    Assert.assertEquals(7, (int) game.getScores().get(PONE.getId()));
    Assert.assertEquals(2, game.getHintsGiven().size());
    Assert.assertEquals(0, game.getHintsRemaining());
    Assert.assertEquals(2, (int) game.getHintsTaken().get(PONE.getId()));
    Assert.assertTrue(game.getHintsGiven().containsKey("FIND"));
    Assert.assertTrue(game.getHintsGiven().containsKey("ME"));
    GridCoordinate c = game.getHintsGiven().get("FIND");
    Assert.assertTrue(2 <= c.getColumn());
    Assert.assertTrue(4 >= c.getColumn());
    Assert.assertTrue(2 <= c.getRow());
    Assert.assertTrue(4 >= c.getRow());
    c = game.getHintsGiven().get("ME");
    Assert.assertTrue(0 <= c.getRow());
    Assert.assertTrue(2 >= c.getRow());
    Assert.assertTrue(7 <= c.getColumn());
    Assert.assertTrue(9 >= c.getColumn());
  }
}
