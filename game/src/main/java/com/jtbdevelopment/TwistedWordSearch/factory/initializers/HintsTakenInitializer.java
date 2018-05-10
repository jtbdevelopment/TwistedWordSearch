package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.players.Player;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Date: 9/23/16 Time: 10:31 PM
 */
@Component
public class HintsTakenInitializer implements GameInitializer<TWSGame> {

  public void initializeGame(final TWSGame game) {
    game.setHintsTaken(game.getPlayers().stream().collect(Collectors.toMap(Player::getId, p -> 0)));
  }

  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
