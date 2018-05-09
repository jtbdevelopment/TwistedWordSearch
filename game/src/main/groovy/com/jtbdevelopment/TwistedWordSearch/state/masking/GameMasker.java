package com.jtbdevelopment.TwistedWordSearch.state.masking;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.mongo.state.masking.AbstractMongoMultiPlayerGameMasker;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 7/13/16 Time: 7:07 PM
 */
@Component
public class GameMasker extends
    AbstractMongoMultiPlayerGameMasker<GameFeature, TWSGame, MaskedGame> {

  protected MaskedGame newMaskedGame() {
    return new MaskedGame();
  }

  @Override
  protected void copyUnmaskedData(final TWSGame twsGame, final MaskedGame playerMaskedGame) {
    super.copyUnmaskedData(twsGame, playerMaskedGame);
    if (!twsGame.getGamePhase().equals(GamePhase.Challenged) && !twsGame.getGamePhase()
        .equals(GamePhase.Setup)) {
      playerMaskedGame.setGrid(twsGame.getGrid().getGridCells());
      playerMaskedGame.setWordsToFind(new TreeSet<>(twsGame.getWordsToFind()));
      playerMaskedGame.setFoundWordLocations(twsGame.getFoundWordLocations());
    }

    playerMaskedGame
        .setHints(new HashSet<>(twsGame.getHintsGiven().values()));
    playerMaskedGame.setHintsRemaining(twsGame.getHintsRemaining());
  }

  @Override
  protected void copyMaskedData(
      final TWSGame twsGame,
      final Player<ObjectId> player,
      final MaskedGame playerMaskedGame,
      final Map<ObjectId, Player<ObjectId>> idMap) {
    super.copyMaskedData(twsGame, player, playerMaskedGame, idMap);

    playerMaskedGame.setScores(twsGame.getScores()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(e -> idMap.get(e.getKey()).getMd5(), Entry::getValue)));

    playerMaskedGame.setHintsTaken(twsGame.getHintsTaken()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(e -> idMap.get(e.getKey()).getMd5(), Entry::getValue)));

    if (!twsGame.getGamePhase().equals(GamePhase.Challenged) && !twsGame.getGamePhase()
        .equals(GamePhase.Setup)) {
      playerMaskedGame.setWordsFoundByPlayer(twsGame.getWordsFoundByPlayer()
          .entrySet()
          .stream()
          .collect(Collectors
              .toMap(e -> idMap.get(e.getKey()).getMd5(), e -> new TreeSet<>(e.getValue()))));
    }

  }

}
