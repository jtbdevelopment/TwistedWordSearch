package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

/**
 * Date: 8/19/16
 * Time: 6:59 AM
 */
@Component
@CompileStatic
class BucketedUSEnglishDictionary {
    @Autowired
    FilteredUSEnglishDictionary filteredUSEnglishDictionary

    final HashMap<Integer, List<String>> wordsByLength = new HashMap<>();

    @PostConstruct
    public void setup() {
        filteredUSEnglishDictionary.filteredWords.each {
            String word ->
                int size = word.size()
                if (!wordsByLength.containsKey(size)) {
                    wordsByLength.put(size, new ArrayList<String>(100));
                }
                wordsByLength[size].add(word)
        }
    }
}
