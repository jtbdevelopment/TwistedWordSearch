package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import groovy.transform.CompileStatic

/**
 * Date: 8/19/16
 * Time: 6:59 AM
 */
@CompileStatic
class BucketedDictionary {
    private final Map<Integer, List<String>> wordsByLength = new HashMap<>();

    public BucketedDictionary() {
        this([] as Set)
    }

    public BucketedDictionary(final Set<String> filteredWords) {
        filteredWords.each {
            String word ->
                int size = word.size()
                if (!wordsByLength.containsKey(size)) {
                    wordsByLength.put(size, new ArrayList<String>(100));
                }
                wordsByLength[size].add(word)
        }
    }

    public Map<Integer, List<String>> getWordsByLength() {
        return wordsByLength
    }
}
