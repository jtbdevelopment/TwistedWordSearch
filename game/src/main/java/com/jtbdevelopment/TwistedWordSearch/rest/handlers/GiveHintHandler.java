package com.jtbdevelopment.TwistedWordSearch.rest.handlers;

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoHintsRemainException;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.masking.GameMasker;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.LinkedList;
import java.util.Random;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 12/20/16 Time: 6:34 PM
 */
@Component
public class GiveHintHandler extends
    AbstractGameActionHandler<Integer, ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {

  private final Random random = new Random();

  public GiveHintHandler(final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractGameRepository<ObjectId, GameFeature, TWSGame> gameRepository,
      final GameTransitionEngine<TWSGame> transitionEngine,
      final GamePublisher<TWSGame, MongoPlayer> gamePublisher,
      final GameEligibilityTracker gameTracker,
      final GameMasker<ObjectId, TWSGame, MaskedGame> gameMasker) {
    super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
        gameMasker);
  }

  protected TWSGame handleActionInternal(final MongoPlayer player, final TWSGame game,
      final Integer param) {
    if (!game.getGamePhase().equals(GamePhase.Playing)) {
      throw new GameIsNotInPlayModeException();
    }

    if (game.getHintsRemaining() <= 0) {
      throw new NoHintsRemainException();
    }

    String word = pickWord(game);
    GridCoordinate hintCoordinate = generateCoordinate(game, word);
    game.getHintsGiven().put(word, hintCoordinate);
    game.getScores().put(player.getId(),
        game.getScores().get(player.getId()) - (word.length() / 2));
    game.setHintsRemaining(game.getHintsRemaining() - 1);
    game.getHintsTaken().put(player.getId(), game.getHintsTaken().get(player.getId()) + 1);
    return game;
  }

  private GridCoordinate generateCoordinate(TWSGame game, String word) {
    GridCoordinate wordStart = game.getWordStarts().get(word);

    int adjustColumn = random.nextInt(3) - 1;
    int adjustRow = random.nextInt(3) - 1;
    if (wordStart.getRow() == 0) {
      adjustRow += 1;
    }

    if (wordStart.getRow() == (game.getGrid().getRows() - 1)) {
      adjustRow -= 1;
    }

    if (wordStart.getColumn() == 0) {
      adjustColumn += 1;
    }

    if (wordStart.getColumn() == (game.getGrid().getColumns() - 1)) {
      adjustColumn -= 1;
    }

    return new GridCoordinate(wordStart.getRow() + adjustRow, wordStart.getColumn() + adjustColumn);
  }

  private String pickWord(TWSGame game) {
    int wordToHint;
    wordToHint = random.nextInt(game.getWordsToFind().size());
    String word = new LinkedList<>(game.getWordsToFind()).get(wordToHint);
    if (game.getHintsGiven().containsKey(word)) {
      return pickWord(game);
    }

    return word;
  }
}
