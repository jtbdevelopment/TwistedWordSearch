package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayout
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 8/19/16
 * Time: 7:04 PM
 */
@Component
@CompileStatic
class WordPlacementInitializer implements GameInitializer<TWSGame> {
    private Random random = new Random()

    @Autowired
    RandomLayoutPicker randomLayoutPicker

    void initializeGame(final TWSGame game) {
        boolean wordWrap = game.features.contains(GameFeature.WordWrapYes)

        int gridRows = game.grid.rowUpperBound
        int gridCols = game.grid.columnUpperBound

        game.words.each {
            String word ->
                WordLayout layout = randomLayoutPicker.randomLayout
                println layout
                char[] lettersOfWord = word.toCharArray()
                int wordSize = word.size()
                int perLetterRowAdjustment = layout.perLetterRowMovement
                int perLetterColAdjustment = layout.perLetterColumnMovement
                List<Set<GridCoordinate>> possibilities = (0..gridRows).collectMany {
                    int gridRow ->
                        List<Set<GridCoordinate>> unfiltered = (0..gridCols).collect {
                            int gridCol ->
                                GridCoordinate wrapAdjustment = new GridCoordinate(0, 0)
                                GridCoordinate initialCell = new GridCoordinate(gridRow, gridCol)
                                Set<GridCoordinate> cells = new LinkedHashSet<>()
                                if (!validCell(game, initialCell)) {
                                    return cells
                                }
                                lettersOfWord.eachWithIndex {
                                    char letter, int index ->
                                        GridCoordinate nextCell = new GridCoordinate(
                                                gridRow + (index * perLetterRowAdjustment) + wrapAdjustment.row,
                                                gridCol + (index * perLetterColAdjustment) + wrapAdjustment.column)
                                        if (validCell(game, nextCell)) {
                                            char existingLetter = game.grid.getGridCell(nextCell)
                                            if (existingLetter == letter || existingLetter == Grid.QUESTION_MARK) {
                                                cells.add(nextCell)
                                            }
                                        } else {
                                            if (!wordWrap) {
                                                return
                                            }

                                            //  Already wrapped once
                                            if (wrapAdjustment.row != 0 || wrapAdjustment.column != 0) {
                                                return
                                            }

                                            while (!validCell(game, nextCell)) {
                                                //  Proceed along until we find an non-space
                                                if (nextCell.row > gridRows) {
                                                    wrapAdjustment = new GridCoordinate(-game.grid.rows, wrapAdjustment.column)
                                                    nextCell = new GridCoordinate(0, nextCell.column)
                                                }
                                                if (nextCell.row < 0) {
                                                    wrapAdjustment = new GridCoordinate(game.grid.rows, wrapAdjustment.column)
                                                    nextCell = new GridCoordinate(gridRows, nextCell.column)
                                                }

                                                if (nextCell.column > gridCols) {
                                                    wrapAdjustment = new GridCoordinate(wrapAdjustment.row, -game.grid.columns)
                                                    nextCell = new GridCoordinate(nextCell.row, 0)
                                                }

                                                if (nextCell.column < 0) {
                                                    wrapAdjustment = new GridCoordinate(wrapAdjustment.row, game.grid.columns)
                                                    nextCell = new GridCoordinate(nextCell.row, gridCols)
                                                }

                                                if (!validCell(game, nextCell)) {
                                                    nextCell = new GridCoordinate(
                                                            nextCell.row + layout.perLetterRowMovement,
                                                            nextCell.column + layout.perLetterColumnMovement)
                                                }
                                            }
                                            char existingLetter = game.grid.getGridCell(nextCell)
                                            if (existingLetter == letter || existingLetter == Grid.QUESTION_MARK) {
                                                cells.add(nextCell)
                                            }
                                        }
                                }
                                return (Set<GridCoordinate>) cells
                        }
                        List<Set<GridCoordinate>> filtered = unfiltered.findAll { it.size() == wordSize }
                        return (Collection<Set<GridCoordinate>>) filtered
                }
                if (possibilities.size() == 0) {
                    //  TODO
                }
                Set<GridCoordinate> use = possibilities[random.nextInt(possibilities.size())]
                use.eachWithIndex {
                    GridCoordinate coordinate, int i ->
                        game.grid.setGridCell(coordinate, lettersOfWord[i])
                }
                //char[][] cells = patternMatcherCreator.createMatchingArrayForLayout(word, layout)
        }
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private boolean validCell(final TWSGame game, final GridCoordinate nextCell) {
        return nextCell.row >= 0 &&
                nextCell.column >= 0 &&
                nextCell.row <= game.grid.rowUpperBound &&
                nextCell.column <= game.grid.columnUpperBound &&
                game.grid.getGridCell(nextCell) != Grid.SPACE
    }

    int getOrder() {
        return DEFAULT_ORDER + 10
    }
}
