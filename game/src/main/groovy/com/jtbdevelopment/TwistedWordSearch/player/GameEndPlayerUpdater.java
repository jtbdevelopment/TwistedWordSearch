package com.jtbdevelopment.TwistedWordSearch.player;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.publish.GameListener;
import com.jtbdevelopment.games.publish.PlayerPublisher;
import com.jtbdevelopment.games.state.GamePhase;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 10/5/16 Time: 6:39 PM
 */
@Component
public class GameEndPlayerUpdater implements GameListener<TWSGame, MongoPlayer> {

  private final PlayerPublisher playerPublisher;
  private final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository;

  public GameEndPlayerUpdater(final PlayerPublisher playerPublisher,
      final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository) {
    this.playerPublisher = playerPublisher;
    this.playerRepository = playerRepository;
  }

  public void gameChanged(final TWSGame game, final MongoPlayer initiatingPlayer,
      final boolean initiatingServer) {
    if (game.getGamePhase().equals(GamePhase.RoundOver) && initiatingServer) {
      final int playerCount = game.getPlayers().size();
      game.getPlayers().forEach(gamePlayer -> {
        Optional<MongoPlayer> optional = playerRepository.findById(gamePlayer.getId());
        if (optional.isPresent()) {
          MongoPlayer p = optional.get();
          TWSPlayerAttributes twsPlayerAttributes = p.getGameSpecificPlayerAttributes();
          if (!twsPlayerAttributes.getGamesPlayedByPlayerCount().containsKey(playerCount)) {
            twsPlayerAttributes.getGamesPlayedByPlayerCount().put(playerCount, 0);
            twsPlayerAttributes.getGamesWonByPlayerCount().put(playerCount, 0);
            twsPlayerAttributes.getMaxScoreByPlayerCount().put(playerCount, 0);
          }

          twsPlayerAttributes.getGamesPlayedByPlayerCount().put(playerCount,
              twsPlayerAttributes.getGamesPlayedByPlayerCount().get(playerCount) + 1);

          int score = game.getScores().get(p.getId());
          if (score > twsPlayerAttributes.getMaxScoreByPlayerCount().get(playerCount)) {
            twsPlayerAttributes.getMaxScoreByPlayerCount().put(playerCount, score);
          }

          if (game.getWinners().contains(gamePlayer.getId())) {
            twsPlayerAttributes.getGamesWonByPlayerCount().put(playerCount,
                twsPlayerAttributes.getGamesWonByPlayerCount().get(playerCount) + 1);
          }

          MongoPlayer saved = playerRepository.save(p);
          playerPublisher.publish(saved);
        }
      });
    }

  }
}
