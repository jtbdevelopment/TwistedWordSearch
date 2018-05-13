package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import java.util.Arrays;
import java.util.LinkedHashMap;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 9/23/16 Time: 10:32 PM
 */
public class HintsTakenInitializerTest extends MongoGameCoreTestCase {

  private HintsTakenInitializer initializer = new HintsTakenInitializer();

  @Test
  public void testInitializeGame() {
    TWSGame game = new TWSGame();
    game.setPlayers(Arrays.asList(PFOUR, PTHREE));
    Assert.assertNull(game.getScores());

    initializer.initializeGame(game);
    LinkedHashMap<ObjectId, Integer> map = new LinkedHashMap<>(2);
    map.put(PFOUR.getId(), 0);
    map.put(PTHREE.getId(), 0);
    Assert.assertEquals(map, game.getHintsTaken());
  }

  @Test
  public void testGetOrder() {
    Assert.assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
  }
}
