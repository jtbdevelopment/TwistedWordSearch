package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayout
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.WordLayoutPatternMatcherCreator
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
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
        game.words.collect {
            String word ->
                WordLayout layout = layouts[random.nextInt(layouts.size())]
                char[][] cells = patternMatcherCreator.createMatchingArrayForLayout(word, layout)
        }
    }

    int getOrder() {
        return DEFAULT_ORDER + 10
    }
}
