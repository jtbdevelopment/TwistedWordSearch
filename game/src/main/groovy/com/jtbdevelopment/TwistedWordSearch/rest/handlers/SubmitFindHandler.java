package com.jtbdevelopment.TwistedWordSearch.rest.handlers;

import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidStartCoordinateException;
import com.jtbdevelopment.TwistedWordSearch.exceptions.InvalidWordFindCoordinatesException;
import com.jtbdevelopment.TwistedWordSearch.exceptions.NoWordToFindAtCoordinatesException;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.events.GamePublisher;
import com.jtbdevelopment.games.exceptions.input.GameIsNotInPlayModeException;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.players.Player;
import com.jtbdevelopment.games.rest.handlers.AbstractGameActionHandler;
import com.jtbdevelopment.games.state.GamePhase;
import com.jtbdevelopment.games.state.masking.GameMasker;
import com.jtbdevelopment.games.state.transition.GameTransitionEngine;
import com.jtbdevelopment.games.tracking.GameEligibilityTracker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 9/3/2016 Time: 3:37 PM
 */
@Component
public class SubmitFindHandler extends
    AbstractGameActionHandler<List<GridCoordinate>, ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {

  public SubmitFindHandler(final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractGameRepository<ObjectId, GameFeature, TWSGame> gameRepository,
      final GameTransitionEngine<TWSGame> transitionEngine,
      final GamePublisher<TWSGame, MongoPlayer> gamePublisher,
      final GameEligibilityTracker gameTracker,
      final GameMasker<ObjectId, TWSGame, MaskedGame> gameMasker) {
    super(playerRepository, gameRepository, transitionEngine, gamePublisher, gameTracker,
        gameMasker);
  }

  protected TWSGame handleActionInternal(final MongoPlayer player, final TWSGame game,
      final List<GridCoordinate> relativeCoordinates) {
    if (!game.getGamePhase().equals(GamePhase.Playing)) {
      throw new GameIsNotInPlayModeException();
    }

    validateCoordinates(game, relativeCoordinates);

    List<GridCoordinate> absoluteCoordinates = collectCoordinates(game, relativeCoordinates);
    String word = collectWord(game, absoluteCoordinates);
    if (game.getWordsToFind().contains(word)) {
      updateGameForFoundWord(game, player, word, absoluteCoordinates);
    } else {
      String reverse = new StringBuffer(word).reverse().toString();
      if (game.getWordsToFind().contains(reverse)) {
        updateGameForFoundWord(game, player, reverse, absoluteCoordinates);
      } else {
        throw new NoWordToFindAtCoordinatesException();
      }

    }

    return game;
  }

  private void updateGameForFoundWord(final TWSGame game, final Player player,
      final String word, final List<GridCoordinate> absoluteCoordinates) {
    game.getWordsToFind().remove(word);
    //noinspection SuspiciousMethodCalls
    game.getWordsFoundByPlayer().get(player.getId()).add(word);
    game.getFoundWordLocations()
        .put(word, new HashSet<>(absoluteCoordinates));
    game.getScores().put((ObjectId) player.getId(),
        game.getScores().get(player.getId()) + absoluteCoordinates.size());
    game.getHintsGiven().remove(word);
  }

  private List<GridCoordinate> collectCoordinates(
      final TWSGame game,
      final List<GridCoordinate> relativeCoordinates) {
    final List<GridCoordinate> absolute = new LinkedList<>(
        Collections.singletonList(new GridCoordinate(relativeCoordinates.get(0))));
    final GridCoordinate coordinate = new GridCoordinate(relativeCoordinates.get(0)
    );
    for (int index = 1; index < relativeCoordinates.size(); ++index) {
      GridCoordinate adjust = relativeCoordinates.get(index);
      coordinate.setRow(coordinate.getRow() + adjust.getRow());
      coordinate.setColumn(coordinate.getColumn() + adjust.getColumn());
      if (coordinate.getRow() < 0) {
        coordinate.setRow(game.getGrid().getRowUpperBound());
      }

      if (coordinate.getColumn() < 0) {
        coordinate.setColumn(game.getGrid().getColumnUpperBound());
      }

      if (coordinate.getRow() > game.getGrid().getRowUpperBound()) {
        coordinate.setRow(0);
      }

      if (coordinate.getColumn() > game.getGrid().getColumnUpperBound()) {
        coordinate.setColumn(0);
      }
      absolute.add(new GridCoordinate(coordinate));
    }
    return absolute;
  }

  private String collectWord(final TWSGame game,
      final List<GridCoordinate> absoluteCoordinates) {
    StringBuilder builder = new StringBuilder();
    absoluteCoordinates
        .forEach(coordinate -> builder.append(game.getGrid().getGridCell(coordinate)));
    return builder.toString();
  }

  private void validateCoordinates(final TWSGame game,
      final List<GridCoordinate> relativeCoordinates) {
    if (relativeCoordinates == null || relativeCoordinates.size() < 2) {
      throw new InvalidWordFindCoordinatesException();
    }

    if (relativeCoordinates.size() > Math
        .max(game.getGrid().getRows(), game.getGrid().getColumns())) {
      throw new InvalidWordFindCoordinatesException();
    }

    GridCoordinate start = relativeCoordinates.get(0);

    if (start.getColumn() < 0 || start.getRow() < 0 || start.getColumn() > game.getGrid()
        .getColumnUpperBound() || start.getRow() > game.getGrid().getRowUpperBound()) {
      throw new InvalidStartCoordinateException();
    }

    if (game.getGrid().getGridCell(start) == Grid.SPACE) {
      throw new InvalidStartCoordinateException();
    }

    final GridCoordinate next = relativeCoordinates.get(1);
    if ((next.getColumn() == 0 && next.getRow() == 0) || next.getColumn() < -1
        || next.getColumn() > 1 || next.getRow() < -1 || next.getRow() > 1) {
      throw new InvalidWordFindCoordinatesException();
    }

    List<GridCoordinate> remaining = new ArrayList<>(relativeCoordinates);
    remaining.remove(0);
    remaining.remove(0);

    Optional<GridCoordinate> badCoordinate = remaining.stream().filter(
        coordinate -> coordinate.getRow() != next.getRow() || coordinate.getColumn() != next
            .getColumn()).findFirst();
    if (badCoordinate.isPresent()) {
      throw new InvalidWordFindCoordinatesException();
    }
  }

}
