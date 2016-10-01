package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import com.jtbdevelopment.games.dictionary.DictionaryManager
import com.jtbdevelopment.games.dictionary.DictionaryType

/**
 * Date: 10/1/2016
 * Time: 2:47 PM
 */
class BucketedDictionaryManagerTest extends GroovyTestCase {
    def simple = [
            words: {
                return ['SIMPLE', 'WORDS'] as Set
            }
    ] as com.jtbdevelopment.games.dictionary.Dictionary
    def moderate = [
            words: {
                return ['MORE', 'COMPLEX', 'WORDS'] as Set
            }
    ] as com.jtbdevelopment.games.dictionary.Dictionary
    def hard = [
            words: {
                return ['GONNA', 'NEED', 'A', 'DICTIONARY', 'FOR', 'WORDS'] as Set
            }
    ] as com.jtbdevelopment.games.dictionary.Dictionary

    def manager = [
            getDictionary: {
                DictionaryType type ->
                    switch (type) {
                        case DictionaryType.USEnglishMaximum:
                            return hard
                        case DictionaryType.USEnglishModerate:
                            return moderate
                        case DictionaryType.USEnglishSimple:
                            return simple
                    }
            }
    ] as DictionaryManager

    def filter = [
            getFilteredWords: {
                com.jtbdevelopment.games.dictionary.Dictionary dictionary ->
                    def s = new HashSet(dictionary.words())
                    s.remove('WORDS')
                    return s
            }
    ] as DictionaryFilter

    BucketedDictionaryManager bucketedDictionaryManager = new BucketedDictionaryManager(dictionaryManager: manager, dictionaryFilter: filter)

    void testGetsABucketedDictionaryOnce() {
        DictionaryType.values().each {
            assert bucketedDictionaryManager.getDictionary(it).is(bucketedDictionaryManager.getDictionary(it))
        }
    }

    void testGetBucketedDictionarySimple() {
        assert [(6): ['SIMPLE']] == bucketedDictionaryManager.getDictionary(DictionaryType.USEnglishSimple).wordsByLength
    }

    void testGetBucketedDictionaryModerate() {
        assert [
                (4): ['MORE'],
                (7): ['COMPLEX']
        ] == bucketedDictionaryManager.getDictionary(DictionaryType.USEnglishModerate).wordsByLength
    }

    void testGetBucketedDictionaryMax() {
        assert [
                (1) : ['A'],
                (3) : ['FOR'],
                (4) : ['NEED'],
                (5) : ['GONNA'],
                (10): ['DICTIONARY']
        ] == bucketedDictionaryManager.getDictionary(DictionaryType.USEnglishMaximum).wordsByLength
    }
}
