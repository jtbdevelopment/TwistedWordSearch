package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.players.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Date: 8/30/16 Time: 3:26 PM
 */
@Component
public class WordsFoundByPlayerInitializer implements GameInitializer<TWSGame> {

  public void initializeGame(final TWSGame game) {
    game.setWordsFoundByPlayer(
        game.getPlayers().stream().collect(Collectors.toMap(Player::getId, p -> new HashSet<>())));
    game.setFoundWordLocations(new HashMap<>());
  }

  public int getOrder() {
    return DEFAULT_ORDER;
  }

}
