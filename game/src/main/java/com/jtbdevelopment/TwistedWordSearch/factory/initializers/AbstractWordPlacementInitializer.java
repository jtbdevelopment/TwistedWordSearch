package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoPossibleLayoutsForWordException;
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker;
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayout;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 8/19/16 Time: 7:04 PM
 */
@Component
public abstract class AbstractWordPlacementInitializer implements GameInitializer<TWSGame> {

  private static final Logger logger = LoggerFactory
      .getLogger(AbstractWordPlacementInitializer.class);
  protected final Random random = new Random();
  private final RandomLayoutPicker randomLayoutPicker;

  public AbstractWordPlacementInitializer(final RandomLayoutPicker randomLayoutPicker) {
    this.randomLayoutPicker = randomLayoutPicker;
  }

  protected abstract Collection<String> getWordsToPlace(final TWSGame game);

  protected abstract void wordPlacedAt(final TWSGame game, final String word,
      final GridCoordinate start, final GridCoordinate end);

  public void initializeGame(final TWSGame game) {
    boolean allowWordWrap = game.getFeatures().contains(GameFeature.WordWrapYes);

    int attempt = 0;
    char[][] gridBackup = game.getGrid().backupGridLetters();
    while (true) {
      attempt++;
      try {
        layoutWordsOnGrid(game, allowWordWrap);
      } catch (NoPossibleLayoutsForWordException ignored) {
        logger.debug("failed to layout on attempt %d", attempt);
        game.getGrid().restoreGridLetters(gridBackup);
        continue;
      }

      break;
    }

  }

  private void layoutWordsOnGrid(final TWSGame game, final boolean allowWordWrap) {
    getWordsToPlace(game).forEach(word -> {
      WordLayout layout = randomLayoutPicker.getRandomLayout();
      List<List<GridCoordinate>> possibilities = gatherPossiblePlacementsForWordLayout(
          game.getGrid(),
          layout,
          allowWordWrap,
          word);
      if (possibilities.size() == 0) {
        throw new NoPossibleLayoutsForWordException();
      }
      List<GridCoordinate> use = possibilities.get(random.nextInt(possibilities.size()));
      final char[] lettersOfWord = word.toCharArray();
      GridCoordinate first = null;
      GridCoordinate last = null;
      for (int i = 0; i < use.size(); ++i) {
        GridCoordinate coordinate = use.get(i);
        game.getGrid().setGridCell(coordinate, lettersOfWord[i]);
        last = coordinate;
        if (first == null) {
          first = coordinate;
        }
      }
      wordPlacedAt(game, word, first, last);
    });
  }

  private List<List<GridCoordinate>> gatherPossiblePlacementsForWordLayout(
      final Grid grid,
      final WordLayout layout,
      final boolean allowWordWrap,
      final String word) {

    final char[] lettersOfWord = word.toCharArray();
    final int perLetterRowAdjustment = layout.getPerLetterRowMovement();
    final int perLetterColAdjustment = layout.getPerLetterColumnMovement();
    List<List<GridCoordinate>> possibilities = new LinkedList<>();
    for (int gridRow = 0; gridRow < grid.getRows(); ++gridRow) {
      for (int gridCol = 0; gridCol < grid.getColumns(); ++gridCol) {
        final GridCoordinate wrapAdjustment = new GridCoordinate(0, 0);
        final Set<GridCoordinate> cells = new LinkedHashSet<>();
        for (int letterIndex = 0; letterIndex < lettersOfWord.length; ++letterIndex) {
          GridCoordinate nextCoordinate = new GridCoordinate(
              gridRow + (letterIndex * perLetterRowAdjustment) + wrapAdjustment.getRow(),
              gridCol + (letterIndex * perLetterColAdjustment) + wrapAdjustment.getColumn());
          if (!validGridCell(grid, nextCoordinate)) {
            if (allowWordWrap && wrapAdjustment.getRow() == 0 && wrapAdjustment.getColumn() == 0) {
              performWrap(grid, wrapAdjustment, nextCoordinate);
            } else {
              continue;
            }
          }
          if (letterCanBePlacedAtCoordinate(grid, lettersOfWord[letterIndex], nextCoordinate)) {
            cells.add(nextCoordinate);
          }
        }
        if (cells.size() == lettersOfWord.length) {
          possibilities.add(new ArrayList<>(cells));
        }
      }
    }
    return possibilities;
  }

  private boolean letterCanBePlacedAtCoordinate(
      final Grid grid,
      final char letter,
      final GridCoordinate coordinate) {
    char existingLetter = grid.getGridCell(coordinate);
    return existingLetter == Grid.QUESTION_MARK || existingLetter == letter;
  }

  private void performWrap(
      final Grid grid,
      final GridCoordinate wrapAdjustment,
      final GridCoordinate coordinate) {
    if (coordinate.getRow() >= grid.getRows()) {
      wrapAdjustment.setRow(-grid.getRows());
      coordinate.setRow(0);
    }

    if (coordinate.getRow() < 0) {
      wrapAdjustment.setRow(grid.getRows());
      coordinate.setRow(grid.getRowUpperBound());
    }

    if (coordinate.getColumn() >= grid.getColumns()) {
      wrapAdjustment.setColumn(-grid.getColumns());
      coordinate.setColumn(0);
    }

    if (coordinate.getColumn() < 0) {
      wrapAdjustment.setColumn(grid.getColumns());
      coordinate.setColumn(grid.getColumnUpperBound());
    }

  }

  private boolean validGridCell(final Grid grid, final GridCoordinate nextCell) {
    return
        nextCell.getRow() >= 0 &&
            nextCell.getColumn() >= 0 &&
            nextCell.getRow() < grid.getRows() &&
            nextCell.getColumn() < grid.getColumns();
  }

}
