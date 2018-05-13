package com.jtbdevelopment.TwistedWordSearch.player;

import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.players.PlayerPayLevel;
import java.util.Collections;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 7/27/16 Time: 6:41 PM
 */
public class TWSPlayerAttributesTest extends MongoGameCoreTestCase {

  @Test
  public void testFreeToPlayPlayer() {
    MongoPlayer player = MongoGameCoreTestCase.makeSimplePlayer(new ObjectId().toHexString());
    player.setPayLevel(PlayerPayLevel.FreeToPlay);

    TWSPlayerAttributes attributes = new TWSPlayerAttributes();
    attributes.setPlayer(player);
    Assert.assertEquals(50, attributes.getMaxDailyFreeGames());
    Assert.assertEquals(Collections.emptyMap(), attributes.getGamesPlayedByPlayerCount());
    Assert.assertEquals(Collections.emptyMap(), attributes.getMaxScoreByPlayerCount());
    Assert.assertEquals(Collections.emptyMap(), attributes.getGamesWonByPlayerCount());
  }

  @Test
  public void testPremiumPlayer() {
    MongoPlayer player = MongoGameCoreTestCase.makeSimplePlayer(new ObjectId().toHexString());
    player.setPayLevel(PlayerPayLevel.PremiumPlayer);

    TWSPlayerAttributes attributes = new TWSPlayerAttributes();
    attributes.setPlayer(player);
    Assert.assertEquals(100, attributes.getMaxDailyFreeGames());
    Assert.assertEquals(Collections.emptyMap(), attributes.getGamesPlayedByPlayerCount());
    Assert.assertEquals(Collections.emptyMap(), attributes.getMaxScoreByPlayerCount());
    Assert.assertEquals(Collections.emptyMap(), attributes.getGamesWonByPlayerCount());
  }

}
