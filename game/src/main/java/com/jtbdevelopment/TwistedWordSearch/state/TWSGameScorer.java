package com.jtbdevelopment.TwistedWordSearch.state;

import com.jtbdevelopment.games.state.scoring.GameScorer;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Date: 7/13/16 Time: 9:27 PM
 */
@Component
public class TWSGameScorer implements GameScorer<TWSGame> {

  public TWSGame scoreGame(final TWSGame game) {
    //noinspection ConstantConditions
    final int maxScore = game.getScores().values().stream().mapToInt(v -> v).max().getAsInt();
    game.setWinners(
        game.getScores().entrySet()
            .stream()
            .filter(entry -> entry.getValue() == maxScore)
            .map(Entry::getKey)
            .collect(Collectors.toList())
    );
    return game;
  }

}
