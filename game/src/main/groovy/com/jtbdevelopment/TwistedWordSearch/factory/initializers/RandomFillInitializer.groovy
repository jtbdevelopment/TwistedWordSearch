package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.factory.GameInitializer
import org.springframework.stereotype.Component

/**
 * Date: 8/26/16
 * Time: 6:57 PM
 */
@Component
class RandomFillInitializer implements GameInitializer<TWSGame> {
    private static Map<GameFeature, Double> RANDOM_PERCENT = [
            (GameFeature.RandomFill)   : (double) 1.0,
            (GameFeature.SomeOverlap)  : (double) 0.75,
            (GameFeature.StrongOverlap): (double) 0.50,
            (GameFeature.WordChunks)   : (double) 1.0  //  Assume chunks already filled in before this
    ]
    private static char[] RANDOM_POOL = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.toCharArray()
    private Random random = new Random()

    void initializeGame(final TWSGame game) {
        GameFeature fillDifficulty = game.features.find { it.group == GameFeature.FillDifficulty }
        Set<Character> wordLettersHash = game.words.collectMany {
            String word ->
                word.toCharArray().collect {
                    it
                }
        } as Set
        Character[] wordLetters = (Character[]) wordLettersHash.toArray()
        List<GridCoordinate> coordinatesToFill = getCoordinatesToFill(game)
        int randomLetters = (int) (RANDOM_PERCENT[fillDifficulty] * coordinatesToFill.size())
        int nonRandomLetters = coordinatesToFill.size() - randomLetters

        if (randomLetters > 0) {
            (1..randomLetters).each {
                char fill = RANDOM_POOL[random.nextInt(RANDOM_POOL.size())]
                def randomCoordinate = random.nextInt(coordinatesToFill.size())
                game.grid.setGridCell(coordinatesToFill[randomCoordinate], fill)
                coordinatesToFill.remove(randomCoordinate)
            }
        }
        if (nonRandomLetters > 0) {
            (1..nonRandomLetters).each {
                Character fill = wordLetters[random.nextInt(wordLetters.size())]
                def randomCoordinate = random.nextInt(coordinatesToFill.size())
                game.grid.setGridCell(coordinatesToFill[randomCoordinate], fill)
                coordinatesToFill.remove(randomCoordinate)
            }
        }
    }

    private static List<GridCoordinate> getCoordinatesToFill(TWSGame game) {
        (List<GridCoordinate>) (0..game.grid.rowUpperBound).collectMany {
            int row ->
                (0..game.grid.columnUpperBound).collect {
                    int col ->
                        if (game.grid.getGridCell(row, col) == Grid.QUESTION_MARK) {
                            return new GridCoordinate(row, col)
                        }
                        null
                }.findAll {
                    it != null
                }
        }
    }

    int getOrder() {
        return DEFAULT_ORDER + 30
    }
}
