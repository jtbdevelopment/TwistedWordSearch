package com.jtbdevelopment.TwistedWordSearch.factory.validators;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameValidator;
import org.springframework.stereotype.Component;

/**
 * Date: 9/24/2016 Time: 4:46 PM
 */
@Component
public class MaxPlayersValidator implements GameValidator<TWSGame> {

  private static final int MAX_PLAYERS = 5;

  public boolean validateGame(final TWSGame game) {
    return game.getPlayers().size() <= MAX_PLAYERS;
  }

  public String errorMessage() {
    return "Sorry, there are too many players - maximum is 5.";
  }
}
