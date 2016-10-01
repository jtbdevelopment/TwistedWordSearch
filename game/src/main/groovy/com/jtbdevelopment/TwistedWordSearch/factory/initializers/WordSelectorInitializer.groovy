package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionary
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionaryManager
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.dictionary.DictionaryType
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
    BucketedDictionaryManager dictionaryManager

    private static final Map<GameFeature, DictionaryType> DICTIONARY_MAP = [
            (GameFeature.SimpleWords)  : DictionaryType.USEnglishSimple,
            (GameFeature.StandardWords): DictionaryType.USEnglishModerate,
            (GameFeature.HardWords)    : DictionaryType.USEnglishMaximum
    ]
    private static final Random random = new Random()

    void initializeGame(final TWSGame game) {
        GameFeature wordDifficulty = game.features.find { it.group == GameFeature.WordDifficulty }
        BucketedDictionary dictionary = dictionaryManager.getDictionary(DICTIONARY_MAP[wordDifficulty])
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
            words.add(wordBucket.get(random.nextInt(wordBucket.size())).toUpperCase())
            currentAvg = (int) Math.round(
                    (float) ((float) words.collect {
                        it.size()
                    }.sum()) / words.size()
            )
        }
        game.words = words
        game.wordsToFind = words
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
