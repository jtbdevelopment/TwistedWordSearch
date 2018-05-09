package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionary
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionaryManager
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.dictionary.DictionaryType
import com.jtbdevelopment.games.factory.GameInitializer
import org.mockito.Mockito

/**
 * Date: 8/19/16
 * Time: 6:31 PM
 */
class WordSelectorInitializerTest extends GroovyTestCase {
    private BucketedDictionaryManager bucketedDictionaryManager = Mockito.mock(BucketedDictionaryManager.class)
    private WordSelectorInitializer initializer = new WordSelectorInitializer()

    private def EXPECTED_DICTIONARY = [
            (GameFeature.SimpleWords)  : DictionaryType.USEnglishSimple,
            (GameFeature.StandardWords): DictionaryType.USEnglishModerate,
            (GameFeature.HardWords)    : DictionaryType.USEnglishMaximum,
    ]

    void testInitializeGame() {
        GameFeature.values().findAll { it.group == GameFeature.WordDifficulty }.each {
            GameFeature wordType ->
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
                ] as BucketedDictionary

                Mockito.when(bucketedDictionaryManager.getDictionary(EXPECTED_DICTIONARY[wordType])).thenReturn(dictionary)
                initializer.dictionaryManager = bucketedDictionaryManager
                TWSGame game = new TWSGame(wordAverageLengthGoal: 5, numberOfWords: 2, features: [wordType] as Set)
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
                assert game.words == game.wordsToFind
        }

    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER == initializer.order
    }
}
