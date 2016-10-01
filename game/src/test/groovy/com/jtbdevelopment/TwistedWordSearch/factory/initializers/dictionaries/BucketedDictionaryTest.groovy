package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

/**
 * Date: 8/19/16
 * Time: 7:01 AM
 */
class BucketedDictionaryTest extends GroovyTestCase {
    public void testCreatesBucketedWords() {
        def words = ['ONE', 'TWO', 'THREE', 'FOUR', 'FIVE', 'BIGWORDOVERHERE'] as Set
        BucketedDictionary bucketedUSEnglishDictionary = new BucketedDictionary(words)

        assert [
                (3) : ['ONE', 'TWO'],
                (5) : ['THREE'],
                (4) : ['FOUR', 'FIVE'],
                (15): ['BIGWORDOVERHERE']
        ] == bucketedUSEnglishDictionary.wordsByLength
    }
}
