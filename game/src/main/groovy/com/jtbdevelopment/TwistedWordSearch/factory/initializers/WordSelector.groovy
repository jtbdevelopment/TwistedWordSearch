package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 8/17/16
 * Time: 6:41 PM
 */
@CompileStatic
@Component
class WordSelector implements GameInitializer<TWSGame> {
    private static final Logger logger = LoggerFactory.getLogger(WordSelector.class)

    @Autowired
    private com.jtbdevelopment.games.dictionary.Dictionary dictionary

    private final HashMap<Integer, List<String>> wordsByLength = new HashMap<>();

    private static final Random random = new Random()

    @PostConstruct
    public void setup() {
        logger.info("Splitting dictionary by length")
        dictionary.words().findAll {
            String word ->
                !(word.toCharArray().find {
                    char c ->
                        !c.isLetter()
                })
        }.each {
            String word ->
                int size = word.size()
                if (!wordsByLength.containsKey(size)) {
                    wordsByLength.put(size, new ArrayList<String>(1000));
                }
                wordsByLength[size].add(word)
        }
        logger.info("Split dictionary by length")
    }

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
            def wordBucket = wordsByLength[sizeChoice]
            words.add(wordBucket.get(random.nextInt(wordBucket.size())))
            currentAvg = (int) Math.round(
                    (float) ((float) words.collect {
                        it.size()
                    }.sum()) / words.size()
            )
        }
    }

    int getOrder() {
        return DEFAULT_ORDER
    }
}
