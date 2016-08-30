package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.exceptions.NoPossibleLayoutsForWordException
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayout
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 8/19/16
 * Time: 7:04 PM
 */
@Component
@CompileStatic
abstract class AbstractWordPlacementInitializer implements GameInitializer<TWSGame> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractWordPlacementInitializer.class)
    private Random random = new Random()

    @Autowired
    RandomLayoutPicker randomLayoutPicker

    protected abstract Collection<String> getWordsToPlace(final TWSGame game)

    void initializeGame(final TWSGame game) {
        boolean allowWordWrap = game.features.contains(GameFeature.WordWrapYes)

        int attempt = 0
        while (true) {
            attempt++
            try {
                layoutWordsOnGrid(game, allowWordWrap)
            } catch (NoPossibleLayoutsForWordException ignored) {
                logger.debug('failed to layout on attempt %d', attempt)
                game.grid.resetGridLetters()
                continue
            }
            break
        }
    }

    private void layoutWordsOnGrid(final TWSGame game, final boolean allowWordWrap) {
        getWordsToPlace(game).each {
            String word ->
                WordLayout layout = randomLayoutPicker.randomLayout
                List<Set<GridCoordinate>> possibilities = gatherPossiblePlacementsForWordLayout(
                        game,
                        layout,
                        allowWordWrap,
                        word)
                if (possibilities.size() == 0) {
                    throw new NoPossibleLayoutsForWordException()
                }
                Set<GridCoordinate> use = possibilities[random.nextInt(possibilities.size())]
                char[] lettersOfWord = word.toCharArray()
                use.eachWithIndex {
                    GridCoordinate coordinate, int i ->
                        game.grid.setGridCell(coordinate, lettersOfWord[i])
                }
        }
    }

    private static List<Set<GridCoordinate>> gatherPossiblePlacementsForWordLayout(
            final TWSGame game,
            final WordLayout layout,
            final boolean allowWordWrap,
            final String word) {

        char[] lettersOfWord = word.toCharArray()
        int wordSize = word.size();
        int perLetterRowAdjustment = layout.perLetterRowMovement
        int perLetterColAdjustment = layout.perLetterColumnMovement
        List<Set<GridCoordinate>> possibilities = (0..game.grid.rowUpperBound).collectMany {
            int gridRow ->
                List<Set<GridCoordinate>> unfiltered = (0..game.grid.columnUpperBound).collect {
                    int gridCol ->
                        GridCoordinate wrapAdjustment = new GridCoordinate(0, 0)
                        Set<GridCoordinate> cells = new LinkedHashSet<>()
                        lettersOfWord.eachWithIndex {
                            char letter, int index ->
                                GridCoordinate nextCoordinate = new GridCoordinate(
                                        gridRow + (index * perLetterRowAdjustment) + wrapAdjustment.row,
                                        gridCol + (index * perLetterColAdjustment) + wrapAdjustment.column)
                                if (!validGridCell(game, nextCoordinate)) {
                                    if (allowWordWrap && wrapAdjustment.row == 0 && wrapAdjustment.column == 0) {
                                        performWrap(game, wrapAdjustment, nextCoordinate)
                                    } else {
                                        return
                                    }
                                }
                                if (letterCanBePlacedAtCoordinate(game, letter, nextCoordinate)) {
                                    cells.add(nextCoordinate)
                                }
                        }
                        return (Set<GridCoordinate>) cells
                }
                List<Set<GridCoordinate>> filtered = unfiltered.findAll { it.size() == wordSize }
                return (Collection<Set<GridCoordinate>>) filtered
        }
        possibilities
    }

    //   TODO - move to grid
    private static boolean letterCanBePlacedAtCoordinate(
            final TWSGame game, final char letter, final GridCoordinate coordinate) {
        char existingLetter = game.grid.getGridCell(coordinate)
        existingLetter == Grid.QUESTION_MARK || existingLetter == letter
    }

    private static void performWrap(
            final TWSGame game, final GridCoordinate wrapAdjustment, final GridCoordinate coordinate) {
        if (coordinate.row > game.grid.rowUpperBound) {
            wrapAdjustment.row = -game.grid.rows
            coordinate.row = 0
        }
        if (coordinate.row < 0) {
            wrapAdjustment.row = game.grid.rows
            coordinate.row = game.grid.rowUpperBound
        }

        if (coordinate.column > game.grid.columnUpperBound) {
            wrapAdjustment.column = -game.grid.columns
            coordinate.column = 0
        }

        if (coordinate.column < 0) {
            wrapAdjustment.column = game.grid.columns
            coordinate.column = game.grid.columnUpperBound
        }
    }

    //  TODO - move to grid
    private static boolean validGridCell(final TWSGame game, final GridCoordinate nextCell) {
        return nextCell.row >= 0 &&
                nextCell.column >= 0 &&
                nextCell.row <= game.grid.rowUpperBound &&
                nextCell.column <= game.grid.columnUpperBound
    }
}
