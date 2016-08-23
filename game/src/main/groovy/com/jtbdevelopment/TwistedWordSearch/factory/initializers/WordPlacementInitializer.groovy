package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayout
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayoutPatternMatcherCreator
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
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
    WordLayoutPatternMatcherCreator patternMatcherCreator

    void initializeGame(final TWSGame game) {
        boolean wordWrap = game.features.contains(GameFeature.WordWrapYes)

        List<WordLayout> layouts = WordLayout.values().toList()
        int gridRows = game.grid.rowUpperBound
        int gridCols = game.grid.columnUpperBound

        game.words.each {
            String word ->
                WordLayout layout = layouts[random.nextInt(layouts.size())]
                String wordToUse = layout.backwards ? word.reverse() : word
                int perLetterRowAdjustment = layout.perLetterRowMovement
                int perLetterColAdjustment = layout.perLetterColumnMovement
                (0..gridRows).each {
                    int gridRow ->
                        (0..gridCols).each {
                            int gridCol ->
                                GridCoordinate wrapAdjustment = new GridCoordinate(0, 0)
                                GridCoordinate initialCell = new GridCoordinate(gridRow, gridCol)
                                if (!validCell(game, initialCell)) {
                                    return
                                }
                                Set<GridCoordinate> cells = new HashSet<>()
                                wordToUse.eachWithIndex {
                                    char letter, int index ->
                                        GridCoordinate nextCell = new GridCoordinate(
                                                gridRow + (index * perLetterRowAdjustment) + wrapAdjustment.row,
                                                gridCol + (index * perLetterColAdjustment) + wrapAdjustment.column)
                                        if (validCell(game, nextCell)) {
                                            char existingLetter = game.grid.getGridCell(nextCell)
                                            if (existingLetter == letter || existingLetter == '?' as char) {
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

                                            //  Proceed along until we find an non-space
                                            if (nextCell.row > gridRows) {
                                                nextCell = new GridCoordinate(0, nextCell.column)
                                                wrapAdjustment = new GridCoordinate(gridRows, nextCell.column)
                                            }
                                            if (nextCell.row < 0) {
                                            }
                                        }
                                }
                        }
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
                game.grid.getGridCell(nextCell) != ' ' as char
    }

    int getOrder() {
        return DEFAULT_ORDER + 10
    }
}
