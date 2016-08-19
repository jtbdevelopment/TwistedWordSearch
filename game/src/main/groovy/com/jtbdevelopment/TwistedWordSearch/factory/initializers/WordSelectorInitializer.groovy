package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedUSEnglishDictionary
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 8/17/16
 * Time: 6:41 PM
 */
@CompileStatic
@Component
class WordSelectorInitializer implements GameInitializer<TWSGame> {

    @Autowired
    protected BucketedUSEnglishDictionary dictionary

    private static final Random random = new Random()

    void initializeGame(final TWSGame game) {
        int currentAvg = game.wordAverageLengthGoal
        Set<String> words = new HashSet<>()
        while (words.size() < game.numberOfWords) {
            int goal = game.wordAverageLengthGoal - (currentAvg - game.wordAverageLengthGoal)
            int lower = goal - 2
            int upper = goal + 2
            if (lower < 2) lower = 2
            if (upper < lower) upper = lower

            int range = upper - lower + 1
            int sizeChoice = random.nextInt(range) + lower
            def wordBucket = dictionary.getWordsByLength()[sizeChoice]
            words.add(wordBucket.get(random.nextInt(wordBucket.size())))
            currentAvg = (int) Math.round(
                    (float) ((float) words.collect {
                        it.size()
                    }.sum()) / words.size()
            )
        }
        game.words = words
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
