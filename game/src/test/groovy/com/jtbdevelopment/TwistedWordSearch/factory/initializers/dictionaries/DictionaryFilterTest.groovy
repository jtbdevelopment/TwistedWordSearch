package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import com.jtbdevelopment.games.dictionary.Dictionary

/**
 * Date: 8/19/16
 * Time: 6:54 AM
 */
class DictionaryFilterTest extends GroovyTestCase {
    DictionaryFilter filter = new DictionaryFilter()
    private class TestDictionary implements Dictionary {
        private final HashSet<String> words = [
                'ONE',
                'BI-ANNUAL',
                'TWO',
                'DOG\'S',
                'THREE'
        ]

        Set<String> words() {
            return words
        }

        boolean isValidWord(final String input) {
            return false
        }
    }

    void testGetFilteredWords() {
        assert ['ONE', 'TWO', 'THREE'] as Set == filter.getFilteredWords(new TestDictionary())
    }
}
