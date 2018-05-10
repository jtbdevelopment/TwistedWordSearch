package com.jtbdevelopment.TwistedWordSearch.player;

import com.jtbdevelopment.games.player.tracking.AbstractPlayerGameTrackingAttributes;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.players.PlayerPayLevel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Transient;

/**
 * Date: 1/30/15 Time: 6:34 PM
 */
public class TWSPlayerAttributes extends AbstractPlayerGameTrackingAttributes {

  private static final int DEFAULT_FREE_GAMES_PER_DAY = 50;
  private static final int DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY = 100;
  @Transient
  private int maxDailyFreeGames;
  private Map<Integer, Integer> gamesWonByPlayerCount = new HashMap<>();
  private Map<Integer, Integer> gamesPlayedByPlayerCount = new HashMap<>();
  private Map<Integer, Integer> maxScoreByPlayerCount = new HashMap<>();

  @Transient
  @java.beans.Transient
  @Override
  public void setPlayer(final Player player) {
    super.setPlayer(player);
    maxDailyFreeGames = (player.getPayLevel().equals(PlayerPayLevel.FreeToPlay)
        ? DEFAULT_FREE_GAMES_PER_DAY : DEFAULT_PREMIUM_PLAYER_GAMES_PER_DAY);
  }

  @Transient
  @java.beans.Transient
  public int getMaxDailyFreeGames() {
    return maxDailyFreeGames;
  }

  public void setMaxDailyFreeGames(int maxDailyFreeGames) {
    this.maxDailyFreeGames = maxDailyFreeGames;
  }

  public Map<Integer, Integer> getGamesWonByPlayerCount() {
    return gamesWonByPlayerCount;
  }

  public void setGamesWonByPlayerCount(Map<Integer, Integer> gamesWonByPlayerCount) {
    this.gamesWonByPlayerCount = gamesWonByPlayerCount;
  }

  public Map<Integer, Integer> getGamesPlayedByPlayerCount() {
    return gamesPlayedByPlayerCount;
  }

  public void setGamesPlayedByPlayerCount(Map<Integer, Integer> gamesPlayedByPlayerCount) {
    this.gamesPlayedByPlayerCount = gamesPlayedByPlayerCount;
  }

  public Map<Integer, Integer> getMaxScoreByPlayerCount() {
    return maxScoreByPlayerCount;
  }

  public void setMaxScoreByPlayerCount(Map<Integer, Integer> maxScoreByPlayerCount) {
    this.maxScoreByPlayerCount = maxScoreByPlayerCount;
  }
}
