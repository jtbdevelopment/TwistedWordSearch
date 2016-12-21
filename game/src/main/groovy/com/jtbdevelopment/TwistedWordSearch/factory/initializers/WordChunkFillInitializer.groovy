package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/30/16
 * Time: 6:35 AM
 */
@CompileStatic
@Component
class WordChunkFillInitializer extends AbstractWordPlacementInitializer implements GameInitializer<TWSGame> {
    private final double FILL_PERCENTAGE = 0.75
    private final double MAX_WORD_PERCENTAGE = 0.60

    protected Collection<String> getWordsToPlace(final TWSGame game) {
        List<String> chunks = []
        if (game.features.contains(GameFeature.WordChunks)) {
            int desiredFill = (int) (game.grid.questionMarkSquaresCount * FILL_PERCENTAGE)
            int generated = 0
            List<String> gameWordsList = game.words.toList()
            while (generated < desiredFill) {
                String word = gameWordsList[random.nextInt(gameWordsList.size())]
                int maxSize = (int) (word.size() * MAX_WORD_PERCENTAGE)
                int size = random.nextInt(maxSize) + 1
                int start = random.nextInt(word.size() - size)
                chunks.add(word.substring(start, start + size))
                generated += size
            }
        }
        return chunks
    }

    protected void wordPlacedAt(
            final TWSGame game, final String word, final GridCoordinate start, final GridCoordinate end) {
        game.wordEnds[word] = end
        game.wordStarts[word] = start
    }

    int getOrder() {
        return DEFAULT_ORDER + 20
    }
}
