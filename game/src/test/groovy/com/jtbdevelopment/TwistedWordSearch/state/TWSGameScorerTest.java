package com.jtbdevelopment.TwistedWordSearch.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.junit.Test;

/**
 * Date: 7/13/16 Time: 9:28 PM
 */
public class TWSGameScorerTest extends MongoGameCoreTestCase {

  private TWSGameScorer scorer = new TWSGameScorer();

  @Test
  public void testScoreGameReturnsGameWithSingleWinner() {
    TWSGame game = new TWSGame();
    Map<ObjectId, Integer> map = new LinkedHashMap<>(1);
    map.put(PONE.getId(), 34);
    game.setScores(map);

    assertSame(game, scorer.scoreGame(game));
    assertEquals(Collections.singletonList(PONE.getId()), game.getWinners());
  }

  @Test
  public void testScoreGameReturnsGameWithMultipleWinners() {
    TWSGame game = new TWSGame();
    Map<ObjectId, Integer> map = new LinkedHashMap<>(4);
    map.put(PONE.getId(), 34);
    map.put(PTWO.getId(), 38);
    map.put(PTHREE.getId(), 15);
    map.put(PFOUR.getId(), 38);
    game.setScores(map);

    assertSame(game, scorer.scoreGame(game));
    assertEquals(Arrays.asList(PTWO.getId(), PFOUR.getId()), game.getWinners());
  }
}
