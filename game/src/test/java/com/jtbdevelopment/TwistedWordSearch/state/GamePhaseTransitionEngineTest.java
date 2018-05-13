package com.jtbdevelopment.TwistedWordSearch.state;

import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.scoring.GameScorer;
import java.util.Collections;
import java.util.HashSet;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 8/12/16 Time: 4:54 PM
 */
public class GamePhaseTransitionEngineTest {

  private GameScorer<TWSGame> scorer = Mockito.mock(GameScorer.class);
  private GamePhaseTransitionEngine engine = new GamePhaseTransitionEngine(scorer);

  @Test
  public void testEvaluateSetupPhase() {
    TWSGame game = new TWSGame();
    game.setGamePhase(GamePhase.Setup);
    game.setWordsToFind(new HashSet<>(Collections.singletonList("TESTING")));
    TWSGame g = engine.evaluateSetupPhase(game);
    Assert.assertSame(game, g);
    Assert.assertEquals(GamePhase.Playing, g.getGamePhase());
  }

  @Test
  public void testEvaluatePlayingWhileWordsToFindNotEmptyDoesNothing() {
    TWSGame game = new TWSGame();
    game.setGamePhase(GamePhase.Playing);
    game.setWordsToFind(new HashSet<>(Collections.singletonList("LEFT")));
    TWSGame g = engine.evaluateSetupPhase(game);
    Assert.assertSame(game, g);
    Assert.assertEquals(GamePhase.Playing, g.getGamePhase());
  }

  @Test
  public void testEvaluatePlayingWhileWordsToFindEmptyEndsGameAndScoresIt() {
    TWSGame scored = new TWSGame();
    scored.setId(new ObjectId());
    TWSGame game = new TWSGame();
    game.setId(new ObjectId());
    game.setGamePhase(GamePhase.Playing);
    game.setWordsToFind(Collections.emptySet());
    Mockito.when(scorer.scoreGame(game)).thenReturn(scored);
    TWSGame g = engine.evaluateSetupPhase(game);
    Assert.assertSame(scored, g);
    Assert.assertEquals(GamePhase.RoundOver, game.getGamePhase());
  }
}
