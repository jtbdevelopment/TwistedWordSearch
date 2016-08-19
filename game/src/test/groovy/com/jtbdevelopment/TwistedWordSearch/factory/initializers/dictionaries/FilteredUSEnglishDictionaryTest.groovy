package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import com.jtbdevelopment.games.dictionary.Dictionary

/**
 * Date: 8/19/16
 * Time: 6:54 AM
 */
class FilteredUSEnglishDictionaryTest extends GroovyTestCase {
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
        FilteredUSEnglishDictionary dictionary = new FilteredUSEnglishDictionary(dictionary: new TestDictionary())

        assert ['ONE', 'TWO', 'THREE'] as Set == dictionary.filteredWords
    }
}
