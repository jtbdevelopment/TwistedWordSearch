package com.jtbdevelopment.TwistedWordSearch.rest.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException;
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException;
import com.jtbdevelopment.TwistedWordSearch.exceptions.NoWordToFindAtCoordinatesException;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Date: 9/3/2016 Time: 3:44 PM
 */
public class SubmitFindHandlerTest extends MongoGameCoreTestCase {

  private SubmitFindHandler handler = new SubmitFindHandler(null, null, null, null, null, null);
  private TWSGame game = new TWSGame();

  @Before
  public void setUp() throws Exception {
    Map<ObjectId, Set<String>> map = new HashMap<>();
    map.put(PONE.getId(), new HashSet<>());

    game.setGrid(new Grid(10, 10));
    game.setPlayers(Collections.singletonList(PONE));
    game.setWordsFoundByPlayer(map);
    game.setWordsToFind(new HashSet<>(Arrays.asList("WORD", "WRAPPED", "AT")));
    game.setGamePhase(GamePhase.Playing);
    game.setFoundWordLocations(new HashMap<>());

    for (int row = 0; row < game.getGrid().getRows(); ++row) {
      for (int col = 0; col < game.getGrid().getColumns(); ++col) {
        game.getGrid().setGridCell(row, col, 'A');
      }
    }
    game.getGrid().setGridCell(0, 0, Grid.SPACE);
    game.getGrid().setGridCell(0, 1, 'W');
    game.getGrid().setGridCell(0, 2, 'O');
    game.getGrid().setGridCell(0, 3, 'R');
    game.getGrid().setGridCell(0, 4, 'D');

    game.getGrid().setGridCell(2, 7, 'W');
    game.getGrid().setGridCell(1, 8, 'R');
    game.getGrid().setGridCell(0, 9, 'A');
    game.getGrid().setGridCell(9, 0, 'P');
    game.getGrid().setGridCell(8, 1, 'P');
    game.getGrid().setGridCell(7, 2, 'E');
    game.getGrid().setGridCell(6, 3, 'D');

    game.getGrid().setGridCell(8, 8, 'A');
    game.getGrid().setGridCell(8, 7, 'T');
    LinkedHashMap<ObjectId, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(PONE.getId(), 0);
    game.setScores(map1);
  }

  @Test(expected = InvalidWordFindCoordinatesException.class)
  public void testInvalidWordFindCoordinatesIfSetEmpty() {
    handler.handleActionInternal(PONE, game, new ArrayList<>());
  }

  @Test(expected = InvalidWordFindCoordinatesException.class)
  public void testInvalidWordFindCoordinatesIfSetNull() {
    handler.handleActionInternal(PONE, game, null);
  }

  @Test
  public void testInvalidWordStartExceptionIfOutsideGrid() {
    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(-1, 5), new GridCoordinate(0, 1)));
      Assert.fail();
    } catch (InvalidStartCoordinateException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, -3), new GridCoordinate(0, 1)));
      Assert.fail();
    } catch (InvalidStartCoordinateException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, 10), new GridCoordinate(0, 1)));
      Assert.fail();
    } catch (InvalidStartCoordinateException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(11, 5), new GridCoordinate(0, 1)));
      Assert.fail();
    } catch (InvalidStartCoordinateException ignore) {
      //
    }

  }

  @Test(expected = InvalidStartCoordinateException.class)
  public void testInvalidWordStartExceptionIfStartingOnSpace() {
    handler.handleActionInternal(PONE, game,
        Arrays.asList(new GridCoordinate(0, 0), new GridCoordinate(0, 1)));
  }

  @Test(expected = InvalidWordFindCoordinatesException.class)
  public void testInvalidWordFindCoordinatesIfNoSecondCoordinate() {
    handler.handleActionInternal(PONE, game,
        Collections.singletonList(new GridCoordinate(5, 5)));
  }

  @Test
  public void testInvalidWordFindCoordinatesIfListLongerThanBiggestBoundary() {
    try {
      TWSGame game = new TWSGame();
      game.setGrid(new Grid(5, 3));
      game.setGamePhase(GamePhase.Playing);
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(0, 0),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1)));
      Assert.fail();
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      TWSGame game = new TWSGame();
      game.setGrid(new Grid(3, 4));
      game.setGamePhase(GamePhase.Playing);
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(0, 0),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1),
              new GridCoordinate(1, 1), new GridCoordinate(1, 1)));
      Assert.fail();
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

  }

  @Test
  public void testInvalidWordFindCoordinatesIfSecondCoordinateIsNotAProperMoveOneOrZero() {
    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, 5), new GridCoordinate(0, 0)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, 5), new GridCoordinate(-2, 0)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, 5), new GridCoordinate(3, 1)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, 5), new GridCoordinate(1, 2)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(new GridCoordinate(5, 5), new GridCoordinate(1, -4)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

  }

  @Test
  public void testInvalidWordFindCoordinatesIfRemainingCoordinatesNotInALine() {
    try {
      handler.handleActionInternal(PONE, game, Arrays.asList(
          new GridCoordinate(5, 5),
          new GridCoordinate(0, 1),
          new GridCoordinate(0, 0),
          new GridCoordinate(1, 1)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game, Arrays.asList(
          new GridCoordinate(5, 5),
          new GridCoordinate(0, 1),
          new GridCoordinate(0, 1),
          new GridCoordinate(1, 1)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(5, 5),
              new GridCoordinate(0, -1),
              new GridCoordinate(0, -1),
              new GridCoordinate(1, -1)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(5, 5),
              new GridCoordinate(1, 0),
              new GridCoordinate(1, 0),
              new GridCoordinate(1, -1)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game, Arrays.asList(
          new GridCoordinate(5, 5),
          new GridCoordinate(-1, 0),
          new GridCoordinate(-1, 0),
          new GridCoordinate(-1, -1)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game, Arrays.asList(
          new GridCoordinate(5, 5),
          new GridCoordinate(1, 1),
          new GridCoordinate(1, 1),
          new GridCoordinate(1, 0)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(5, 5),
              new GridCoordinate(1, -1),
              new GridCoordinate(1, -1),
              new GridCoordinate(1, 0)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(5, 5),
              new GridCoordinate(-1, 1),
              new GridCoordinate(-1, 1),
              new GridCoordinate(-1, 0)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

    try {
      handler.handleActionInternal(PONE, game,
          Arrays.asList(
              new GridCoordinate(5, 5),
              new GridCoordinate(-1, -1),
              new GridCoordinate(-1, -1),
              new GridCoordinate(-1, 0)));
    } catch (InvalidWordFindCoordinatesException ignore) {
      //
    }

  }

  @Test
  public void testFailsIfGameNotInPlayingMode() {
    Arrays.stream(GamePhase.values()).filter(p -> !GamePhase.Playing.equals(p)).forEach(it -> {
      try {
        game.setGamePhase(it);
        handler.handleActionInternal(PONE, game,
            Arrays.asList(new GridCoordinate(8, 8), new GridCoordinate(0, -1)));
      } catch (GameIsNotInPlayModeException ignore) {
        return;

      }

      Assert.fail("Should have exceptioned");
    });
  }

  @Test(expected = NoWordToFindAtCoordinatesException.class)
  public void testNoWordFoundExceptionIfNotActuallyGood() {
    handler.handleActionInternal(PONE, game,
        Arrays.asList(new GridCoordinate(9, 9), new GridCoordinate(0, -1)));
  }

  @Test
  public void testAbleToFindWordCoordinatesGivenInForwardDirection() {
    TWSGame update = handler.handleActionInternal(PONE, game,
        Arrays.asList(new GridCoordinate(8, 8), new GridCoordinate(0, -1)));
    assertSame(update, game);
    assertEquals(new HashSet<>(Arrays.asList("WORD", "WRAPPED")), update.getWordsToFind());
    LinkedHashMap<ObjectId, Set<String>> map = new LinkedHashMap<>(1);
    map.put(PONE.getId(), new HashSet<>(Collections.singletonList("AT")));
    assertEquals(map, update.getWordsFoundByPlayer());
    LinkedHashMap<String, Set<GridCoordinate>> map1 = new LinkedHashMap<>(1);
    map1.put("AT",
        new HashSet<>(Arrays.asList(new GridCoordinate(8, 8), new GridCoordinate(8, 7))));
    assertEquals(map1, update.getFoundWordLocations());
    LinkedHashMap<ObjectId, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(PONE.getId(), 2);
    assertEquals(map2, update.getScores());
  }

  @Test
  public void testAbleToFindWordCoordinatesGivenInBackwardDirection() {
    TWSGame update = handler.handleActionInternal(PONE, game,
        Arrays.asList(new GridCoordinate(8, 7), new GridCoordinate(0, 1)));
    assertSame(update, game);
    assertEquals(new HashSet<>(Arrays.asList("WORD", "WRAPPED")), update.getWordsToFind());
    LinkedHashMap<ObjectId, Set<String>> map = new LinkedHashMap<>(1);
    map.put(PONE.getId(), new HashSet<>(Collections.singletonList("AT")));
    assertEquals(map, update.getWordsFoundByPlayer());
    LinkedHashMap<String, Set<GridCoordinate>> map1 = new LinkedHashMap<>(1);
    map1.put("AT", new HashSet<>(
        Arrays.asList(new GridCoordinate(8, 8), new GridCoordinate(8, 7))));
    assertEquals(map1, update.getFoundWordLocations());
    LinkedHashMap<ObjectId, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(PONE.getId(), 2);
    assertEquals(map2, update.getScores());
  }

  @Test
  public void testAbleToFindWordCoordinatesGivenInWrap() {
    TWSGame update = handler.handleActionInternal(PONE, game,
        Arrays.asList(
            new GridCoordinate(2, 7), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1),
            new GridCoordinate(-1, 1), new GridCoordinate(-1, 1), new GridCoordinate(-1, 1),
            new GridCoordinate(-1, 1)));
    assertSame(update, game);
    assertEquals(
        new HashSet<>(Arrays.asList("WORD", "AT")),
        update.getWordsToFind());
    LinkedHashMap<ObjectId, Set<String>> map = new LinkedHashMap<>(1);
    map.put(PONE.getId(), new HashSet<>(Collections.singletonList("WRAPPED")));
    assertEquals(map, update.getWordsFoundByPlayer());
    LinkedHashMap<String, Set<GridCoordinate>> map1 = new LinkedHashMap<>(1);
    map1.put("WRAPPED", new HashSet<>(
        Arrays.asList(new GridCoordinate(2, 7), new GridCoordinate(1, 8), new GridCoordinate(1, 8),
            new GridCoordinate(0, 9), new GridCoordinate(9, 0), new GridCoordinate(8, 1),
            new GridCoordinate(7, 2), new GridCoordinate(6, 3))));
    assertEquals(map1, update.getFoundWordLocations());
    LinkedHashMap<ObjectId, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(PONE.getId(), 7);
    assertEquals(map2, update.getScores());
  }

  @Test
  public void testRemovesRelevantHint() {
    LinkedHashMap<String, GridCoordinate> map = new LinkedHashMap<>(2);
    map.put("AT", new GridCoordinate(1, 1));
    map.put("WRAPPED", new GridCoordinate(2, 5));
    game.setHintsGiven(map);
    TWSGame update = handler.handleActionInternal(PONE, game,
        Arrays.asList(new GridCoordinate(8, 8), new GridCoordinate(0, -1)));
    assertSame(update, game);
    assertEquals(new HashSet<>(Arrays.asList("WORD", "WRAPPED")),
        update.getWordsToFind());
    LinkedHashMap<ObjectId, Set<String>> map1 = new LinkedHashMap<>(1);
    map1.put(PONE.getId(), new HashSet<>(Collections.singletonList("AT")));
    assertEquals(map1, update.getWordsFoundByPlayer());
    LinkedHashMap<String, Set<GridCoordinate>> map2 = new LinkedHashMap<>(1);
    map2.put("AT", new HashSet<>(
        Arrays.asList(new GridCoordinate(8, 8), new GridCoordinate(8, 7))));
    assertEquals(map2, update.getFoundWordLocations());
    LinkedHashMap<ObjectId, Integer> map3 = new LinkedHashMap<>(1);
    map3.put(PONE.getId(), 2);
    assertEquals(map3, update.getScores());
    Assert.assertFalse(game.getHintsGiven().containsKey("AT"));
    LinkedHashMap<String, GridCoordinate> map4 = new LinkedHashMap<>(1);
    map4.put("WRAPPED", new GridCoordinate(2, 5));
    assertEquals(map4, game.getHintsGiven());
  }
}
