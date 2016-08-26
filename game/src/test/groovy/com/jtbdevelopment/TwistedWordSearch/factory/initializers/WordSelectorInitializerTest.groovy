package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedUSEnglishDictionary
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/19/16
 * Time: 6:31 PM
 */
class WordSelectorInitializerTest extends GroovyTestCase {
    WordSelectorInitializer initializer = new WordSelectorInitializer()

    void testInitializeGame() {
        def dictionary = [
                getWordsByLength: {
                    return [
                            (2) : ['of', 'at'],
                            (3) : ['one'],
                            (4) : ['four'],
                            (5) : ['fired'],
                            (6) : ['forged'],
                            (7) : ['forgets'],
                            (8) : ['doctored'],
                            (9) : ['doctoring'],
                            (10): ['0123456789']
                    ] as Map<Integer, List<String>>
                }
        ] as BucketedUSEnglishDictionary
        initializer.dictionary = dictionary
        TWSGame game = new TWSGame(wordAverageLengthGoal: 5, numberOfWords: 2)
        Set<Set<String>> validCombos = [
                ['FOUR', 'FORGETS'] as Set,
                ['OF', 'FORGETS'] as Set,
                ['FORGED', 'ONE'] as Set,
                ['FIRED', 'ONE'] as Set,
                ['FIRED', 'FORGED'] as Set,
                ['ONE', 'DOCTORING'] as Set,
                ['AT', 'FORGETS'] as Set,
                ['FIRED', 'FOUR'] as Set,
                ['FORGED', 'OF'] as Set,
                ['FORGED', 'FOUR'] as Set,
                ['FORGED', 'AT'] as Set,
                ['ONE', 'FORGETS'] as Set,
                ['DOCTORED', 'ONE'] as Set,
                ['DOCTORED', 'FOUR'] as Set,
                ['FIRED', 'FORGETS'] as Set,
        ] as Set
        initializer.initializeGame(game)
        assertNotNull game.words
        assert validCombos.contains(game.words)

    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }
}
