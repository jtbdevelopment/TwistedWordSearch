package com.jtbdevelopment.TwistedWordSearch.state;

import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.scoring.GameScorer;
import com.jtbdevelopment.games.state.transition.AbstractMPGamePhaseTransitionEngine;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 7/13/16 Time: 9:26 PM
 */
@Component
class GamePhaseTransitionEngine extends
    AbstractMPGamePhaseTransitionEngine<ObjectId, GameFeature, TWSGame> {

  GamePhaseTransitionEngine(final GameScorer<TWSGame> gameScorer) {
    super(gameScorer);
  }

  @Override
  protected TWSGame evaluateSetupPhase(final TWSGame game) {
    return changeStateAndReevaluate(GamePhase.Playing, game);
  }

  @Override
  protected TWSGame evaluatePlayingPhase(final TWSGame game) {
    if (game.getWordsToFind().isEmpty()) {
      return changeStateAndReevaluate(GamePhase.RoundOver, game);
    }
    return super.evaluatePlayingPhase(game);
  }
}
