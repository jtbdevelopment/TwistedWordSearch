package com.jtbdevelopment.TwistedWordSearch.player;

import static org.mockito.Mockito.mock;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.publish.PlayerPublisher;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 10/5/16 Time: 6:54 PM
 */
public class GameEndPlayerUpdaterTest extends MongoGameCoreTestCase {

  private PlayerPublisher playerPublisher = mock(PlayerPublisher.class);
  private AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository = mock(
      AbstractPlayerRepository.class);
  private GameEndPlayerUpdater updater = new GameEndPlayerUpdater(playerPublisher,
      playerRepository);
  private MongoPlayer p1;
  private MongoPlayer p2;
  private MongoPlayer p3;
  private MongoPlayer p1loaded;
  private MongoPlayer p2loaded;
  private MongoPlayer p3loaded;
  private MongoPlayer p1saved;
  private MongoPlayer p2saved;
  private MongoPlayer p3saved;
  private TWSPlayerAttributes p1a;
  private TWSPlayerAttributes p2a;
  private TWSPlayerAttributes p3a;

  @Before
  public void setUp() {
    p1 = MongoGameCoreTestCase.makeSimplePlayer("100");
    p2 = MongoGameCoreTestCase.makeSimplePlayer("101");
    p3 = MongoGameCoreTestCase.makeSimplePlayer("103");
    p1loaded = MongoGameCoreTestCase.makeSimplePlayer("100");
    p2loaded = MongoGameCoreTestCase.makeSimplePlayer("101");
    p3loaded = MongoGameCoreTestCase.makeSimplePlayer("103");
    p1saved = MongoGameCoreTestCase.makeSimplePlayer("100");
    p2saved = MongoGameCoreTestCase.makeSimplePlayer("101");
    p3saved = MongoGameCoreTestCase.makeSimplePlayer("103");
    p1a = new TWSPlayerAttributes();
    LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(1);
    map.put(3, 10);
    LinkedHashMap<Integer, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(3, 33);
    LinkedHashMap<Integer, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(3, 24);
    p1a.setGamesWonByPlayerCount(map);
    p1a.setMaxScoreByPlayerCount(map1);
    p1a.setGamesPlayedByPlayerCount(map2);
    p1loaded.setGameSpecificPlayerAttributes(p1a);

    p2a = new TWSPlayerAttributes();
    LinkedHashMap<Integer, Integer> map3 = new LinkedHashMap<>(1);
    map3.put(1, 24);

    LinkedHashMap<Integer, Integer> map4 = new LinkedHashMap<>(1);
    map4.put(1, 24);

    LinkedHashMap<Integer, Integer> map5 = new LinkedHashMap<>(1);
    map5.put(1, 90);

    p2a.setGamesPlayedByPlayerCount(map3);
    p2a.setGamesWonByPlayerCount(map4);
    p2a.setMaxScoreByPlayerCount(map5);
    p2loaded.setGameSpecificPlayerAttributes(p2a);

    p3a = new TWSPlayerAttributes();
    LinkedHashMap<Integer, Integer> map6 = new LinkedHashMap<>(2);
    map6.put(3, 5);
    map6.put(2, 20);

    LinkedHashMap<Integer, Integer> map7 = new LinkedHashMap<>(2);
    map7.put(3, 18);
    map7.put(2, 130);

    LinkedHashMap<Integer, Integer> map8 = new LinkedHashMap<>(2);
    map8.put(3, 100);
    map8.put(2, 40);

    p3a.setGamesWonByPlayerCount(map6);
    p3a.setMaxScoreByPlayerCount(map7);
    p3a.setGamesPlayedByPlayerCount(map8);
    p3loaded.setGameSpecificPlayerAttributes(p3a);
  }

  @Test
  public void testUpdatesPlayerForGameEnd() {
    TWSGame game = new TWSGame();
    game.setGamePhase(GamePhase.RoundOver);
    game.setPlayers(Arrays.asList(p1, p2, p3));
    game.setWinners(Arrays.asList(p1.getId(), p3.getId()));
    LinkedHashMap<ObjectId, Integer> map = new LinkedHashMap<>(3);
    map.put(p1.getId(), 30);
    map.put(p2.getId(), 10);
    map.put(p3.getId(), 30);
    game.setScores(map);

    Mockito.when(playerRepository.findById(p1.getId())).thenReturn(Optional.of(p1loaded));
    Mockito.when(playerRepository.findById(p2.getId())).thenReturn(Optional.of(p2loaded));
    Mockito.when(playerRepository.findById(p3.getId())).thenReturn(Optional.of(p3loaded));
    Mockito.when(playerRepository.save(p1loaded)).thenReturn(p1saved);
    Mockito.when(playerRepository.save(p2loaded)).thenReturn(p2saved);
    Mockito.when(playerRepository.save(p3loaded)).thenReturn(p3saved);

    updater.gameChanged(game, null, true);

    LinkedHashMap<Integer, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(3, 11);
    Assert.assertEquals(map1, p1a.getGamesWonByPlayerCount());
    LinkedHashMap<Integer, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(3, 25);
    Assert.assertEquals(map2, p1a.getGamesPlayedByPlayerCount());
    LinkedHashMap<Integer, Integer> map3 = new LinkedHashMap<>(1);
    map3.put(3, 33);
    Assert.assertEquals(map3, p1a.getMaxScoreByPlayerCount());

    LinkedHashMap<Integer, Integer> map4 = new LinkedHashMap<>(2);
    map4.put(3, 0);
    map4.put(1, 24);
    Assert.assertEquals(map4, p2a.getGamesWonByPlayerCount());
    LinkedHashMap<Integer, Integer> map5 = new LinkedHashMap<>(2);
    map5.put(3, 1);
    map5.put(1, 24);
    Assert.assertEquals(map5, p2a.getGamesPlayedByPlayerCount());
    LinkedHashMap<Integer, Integer> map6 = new LinkedHashMap<>(2);
    map6.put(3, 10);
    map6.put(1, 90);
    Assert.assertEquals(map6, p2a.getMaxScoreByPlayerCount());

    LinkedHashMap<Integer, Integer> map7 = new LinkedHashMap<>(2);
    map7.put(3, 6);
    map7.put(2, 20);
    Assert.assertEquals(map7, p3a.getGamesWonByPlayerCount());
    LinkedHashMap<Integer, Integer> map8 = new LinkedHashMap<>(2);
    map8.put(3, 101);
    map8.put(2, 40);
    Assert.assertEquals(map8, p3a.getGamesPlayedByPlayerCount());
    LinkedHashMap<Integer, Integer> map9 = new LinkedHashMap<>(2);
    map9.put(3, 30);
    map9.put(2, 130);
    Assert.assertEquals(map9, p3a.getMaxScoreByPlayerCount());

    Mockito.verify(playerPublisher).publish(p1saved);
    Mockito.verify(playerPublisher).publish(p2saved);
    Mockito.verify(playerPublisher).publish(p3saved);
  }

  @Test
  public void testDoesNothingIfNotInitiatingServer() {
    TWSGame game = new TWSGame();
    game.setGamePhase(GamePhase.RoundOver);
    game.setPlayers(Arrays.asList(p1, p2, p3));
    game.setWinners(Arrays.asList(p1.getId(), p3.getId()));
    LinkedHashMap<ObjectId, Integer> map = new LinkedHashMap<>(3);
    map.put(p1.getId(), 30);
    map.put(p2.getId(), 10);
    map.put(p3.getId(), 30);
    game.setScores(map);

    updater.gameChanged(game, null, false);

    LinkedHashMap<Integer, Integer> map1 = new LinkedHashMap<>(1);
    map1.put(3, 10);
    Assert.assertEquals(map1, p1a.getGamesWonByPlayerCount());
    LinkedHashMap<Integer, Integer> map2 = new LinkedHashMap<>(1);
    map2.put(3, 24);
    Assert.assertEquals(map2, p1a.getGamesPlayedByPlayerCount());
    LinkedHashMap<Integer, Integer> map3 = new LinkedHashMap<>(1);
    map3.put(3, 33);
    Assert.assertEquals(map3, p1a.getMaxScoreByPlayerCount());

    LinkedHashMap<Integer, Integer> map4 = new LinkedHashMap<>(1);
    map4.put(1, 24);
    Assert.assertEquals(map4, p2a.getGamesWonByPlayerCount());
    LinkedHashMap<Integer, Integer> map5 = new LinkedHashMap<>(1);
    map5.put(1, 24);
    Assert.assertEquals(map5, p2a.getGamesPlayedByPlayerCount());
    LinkedHashMap<Integer, Integer> map6 = new LinkedHashMap<>(1);
    map6.put(1, 90);
    Assert.assertEquals(map6, p2a.getMaxScoreByPlayerCount());

    LinkedHashMap<Integer, Integer> map7 = new LinkedHashMap<>(2);
    map7.put(3, 5);
    map7.put(2, 20);
    Assert.assertEquals(map7, p3a.getGamesWonByPlayerCount());
    LinkedHashMap<Integer, Integer> map8 = new LinkedHashMap<>(2);
    map8.put(3, 100);
    map8.put(2, 40);
    Assert.assertEquals(map8, p3a.getGamesPlayedByPlayerCount());
    LinkedHashMap<Integer, Integer> map9 = new LinkedHashMap<>(2);
    map9.put(3, 18);
    map9.put(2, 130);
    Assert.assertEquals(map9, p3a.getMaxScoreByPlayerCount());
  }

  @Test
  public void testDoesNothingIfNotPhaseNotRoundOver() {
    Arrays.stream(GamePhase.values()).filter(p -> GamePhase.RoundOver != p).forEach(it -> {

      TWSGame game = new TWSGame();
      game.setGamePhase(it);
      game.setPlayers(Arrays.asList(p1, p2, p3));
      game.setWinners(Arrays.asList(p1.getId(), p3.getId()));
      LinkedHashMap<ObjectId, Integer> map = new LinkedHashMap<>(3);
      map.put(p1.getId(), 30);
      map.put(p2.getId(), 10);
      map.put(p3.getId(), 30);
      game.setScores(map);

      updater.gameChanged(game, null, false);

      LinkedHashMap<Integer, Integer> map1 = new LinkedHashMap<>(1);
      map1.put(3, 10);
      Assert.assertEquals(map1, p1a.getGamesWonByPlayerCount());
      LinkedHashMap<Integer, Integer> map2 = new LinkedHashMap<>(1);
      map2.put(3, 24);
      Assert.assertEquals(map2, p1a.getGamesPlayedByPlayerCount());
      LinkedHashMap<Integer, Integer> map3 = new LinkedHashMap<>(1);
      map3.put(3, 33);
      Assert.assertEquals(map3, p1a.getMaxScoreByPlayerCount());

      LinkedHashMap<Integer, Integer> map4 = new LinkedHashMap<>(1);
      map4.put(1, 24);
      Assert.assertEquals(map4, p2a.getGamesWonByPlayerCount());
      LinkedHashMap<Integer, Integer> map5 = new LinkedHashMap<>(1);
      map5.put(1, 24);
      Assert.assertEquals(map5, p2a.getGamesPlayedByPlayerCount());
      LinkedHashMap<Integer, Integer> map6 = new LinkedHashMap<>(1);
      map6.put(1, 90);
      Assert.assertEquals(map6, p2a.getMaxScoreByPlayerCount());

      LinkedHashMap<Integer, Integer> map7 = new LinkedHashMap<>(2);
      map7.put(3, 5);
      map7.put(2, 20);
      Assert.assertEquals(map7, p3a.getGamesWonByPlayerCount());
      LinkedHashMap<Integer, Integer> map8 = new LinkedHashMap<>(2);
      map8.put(3, 100);
      map8.put(2, 40);
      Assert.assertEquals(map8, p3a.getGamesPlayedByPlayerCount());
      LinkedHashMap<Integer, Integer> map9 = new LinkedHashMap<>(2);
      map9.put(3, 18);
      map9.put(2, 130);
      Assert.assertEquals(map9, p3a.getMaxScoreByPlayerCount());
    });
  }
}
