package com.jtbdevelopment.TwistedWordSearch.state.masking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.bson.types.ObjectId;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 7/13/16 Time: 7:09 PM
 */
public class GameMaskerTest extends MongoGameCoreTestCase {

  private GameMasker masker = new GameMasker();

  @Test
  public void testNewMaskedGame() {
    MaskedGame game = masker.newMaskedGame();
    Assert.assertNotNull(game);
  }

  @Test
  public void testMaskingAGameNotInChallengedOrSetupPhases() {
    Arrays.stream(GamePhase.values())
        .filter(p -> !(GamePhase.Setup.equals(p) || GamePhase.Challenged.equals(p)))
        .forEach(gamePhase -> {

          TWSGame game = new TWSGame();
          game.setPlayers(Arrays.asList(PONE, PFOUR));
          game.setInitiatingPlayer(PONE.getId());
          game.setGamePhase(gamePhase);
          game.setGrid(new Grid(10, 10));
          game.setWordsToFind(new HashSet<>(Arrays.asList("A", "SET", "OF", "WORDS")));
          Map<String, GridCoordinate> map = new LinkedHashMap<>(2);
          map.put("A", new GridCoordinate(0, 3));
          map.put("OF", new GridCoordinate(5, 6));
          game.setHintsGiven(map);
          Map<ObjectId, Integer> map1 = new LinkedHashMap<>(2);
          map1.put(PONE.getId(), 10);
          map1.put(PFOUR.getId(), 32);
          game.setScores(map1);
          Map<ObjectId, Set<String>> map2 = new LinkedHashMap<>(2);
          map2.put(PONE.getId(), new HashSet<>(Arrays.asList("I", "FOUND", "THESE")));
          map2.put(PFOUR.getId(), Collections.emptySet());
          game.setWordsFoundByPlayer(map2);
          game.setHintsRemaining(4);
          LinkedHashMap<ObjectId, Integer> map3 = new LinkedHashMap<>(2);
          map3.put(PONE.getId(), 1);
          map3.put(PFOUR.getId(), 0);
          game.setHintsTaken(map3);
          Map<String, Set<GridCoordinate>> map4 = new LinkedHashMap<>(1);
          map4.put("I", new HashSet<>(
              Arrays.asList(new GridCoordinate(1, 1), new GridCoordinate(1, 2),
                  new GridCoordinate(1, 3))));
          game.setFoundWordLocations(map4);

          MaskedGame masked = masker.maskGameForPlayer(game, PONE);
          Assert.assertSame(masked.getGrid(), game.getGrid().getGridCells());
          assertEquals(game.getWordsToFind(), new TreeSet<>(masked.getWordsToFind()));
          LinkedHashMap<String, Set> map5 = new LinkedHashMap<>(2);
          map5.put(PONE.getMd5(), new TreeSet<>(Arrays.asList("I", "FOUND", "THESE")));
          map5.put(PFOUR.getMd5(),
              DefaultGroovyMethods.asType(new ArrayList(), Set.class));
          assertEquals(map5, masked.getWordsFoundByPlayer());
          LinkedHashMap<String, Integer> map6 = new LinkedHashMap<>(2);
          map6.put(PONE.getMd5(), 10);
          map6.put(PFOUR.getMd5(), 32);
          assertEquals(map6, masked.getScores());
          Assert
              .assertSame(game.getFoundWordLocations(), masked.getFoundWordLocations());

          //  Minor proofs that overridden methods called base implementations
          LinkedHashMap<String, String> map7 = new LinkedHashMap<>(2);
          map7.put(PONE.getMd5(), PONE.getDisplayName());
          map7.put(PFOUR.getMd5(), PFOUR.getDisplayName());
          assertEquals(map7, masked.getPlayers());
          assertEquals(gamePhase, masked.getGamePhase());
          assertEquals(
              DefaultGroovyMethods.asType(game.getHintsGiven().values(), Set.class),
              masked.getHints());
          assertEquals(game.getHintsRemaining(), masked.getHintsRemaining());
          LinkedHashMap<String, Integer> map8 = new LinkedHashMap<>(2);
          map8.put(PONE.getMd5(), 1);
          map8.put(PFOUR.getMd5(), 0);
          assertEquals(map8, masked.getHintsTaken());
        });
  }

  @Test
  public void testMaskingAGameInChallengedOrSetupPhases() {
    Arrays.stream(GamePhase.values())
        .filter(p -> (GamePhase.Setup.equals(p) || GamePhase.Challenged.equals(p)))
        .forEach(it -> {
          TWSGame game = new TWSGame();
          game.setPlayers(Arrays.asList(PONE, PFOUR));
          game.setInitiatingPlayer(PONE.getId());
          game.setGamePhase(it);
          game.setGrid(new Grid(10, 10));
          game.setWordsToFind(new HashSet<>(Arrays.asList("A", "SET", "OF", "WORDS")));
          LinkedHashMap<ObjectId, Integer> map = new LinkedHashMap<>(2);
          map.put(PONE.getId(), 10);
          map.put(PFOUR.getId(), 32);
          game.setScores(map);
          LinkedHashMap<ObjectId, Set<String>> map1 = new LinkedHashMap<>(2);
          map1.put(PONE.getId(), new HashSet<>(Arrays.asList("I", "FOUND", "THESE")));
          map1.put(PFOUR.getId(), Collections.emptySet());
          game.setWordsFoundByPlayer(map1);
          game.setHintsRemaining(3);
          LinkedHashMap<String, Set<GridCoordinate>> map2 = new LinkedHashMap<>(1);
          map2.put("I", new HashSet<>(
              Arrays.asList(new GridCoordinate(1, 1), new GridCoordinate(1, 2),
                  new GridCoordinate(1, 3))));
          game.setFoundWordLocations(map2);

          MaskedGame masked = masker.maskGameForPlayer(game, PONE);
          assertNull(masked.getGrid());
          assertNull(masked.getWordsToFind());
          assertNull(masked.getWordsFoundByPlayer());
          LinkedHashMap<String, Integer> map3 = new LinkedHashMap<>(2);
          map3.put(PONE.getMd5(), 10);
          map3.put(PFOUR.getMd5(), 32);
          assertEquals(map3, masked.getScores());
          assertNull(masked.getFoundWordLocations());

          //  Minor proofs that overridden methods called base implementations
          LinkedHashMap<String, String> map4 = new LinkedHashMap<>(2);
          map4.put(PONE.getMd5(), PONE.getDisplayName());
          map4.put(PFOUR.getMd5(), PFOUR.getDisplayName());
          assertEquals(map4, masked.getPlayers());
          assertEquals(it, masked.getGamePhase());
          assertEquals(
              DefaultGroovyMethods.asType(game.getHintsGiven().values(), Set.class),
              masked.getHints());
          assertEquals(game.getHintsRemaining(), masked.getHintsRemaining());

        });
  }

  @Test
  public void testGetIDClass() {
    assertEquals(ObjectId.class, masker.getIDClass());
  }
}
