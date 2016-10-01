package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import com.jtbdevelopment.games.dictionary.DictionaryManager
import com.jtbdevelopment.games.dictionary.DictionaryType
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 10/1/2016
 * Time: 12:58 PM
 */
@CompileStatic
@Component
class BucketedDictionaryManager {
    @Autowired
    DictionaryFilter dictionaryFilter
    @Autowired
    DictionaryManager dictionaryManager

    private final HashMap<DictionaryType, BucketedDictionary> bucketedDictionaries = [:]

    BucketedDictionary getDictionary(final DictionaryType type) {
        if (!bucketedDictionaries.containsKey(type)) {
            synchronized (bucketedDictionaries) {
                if (!bucketedDictionaries.containsKey(type)) {
                    bucketedDictionaries[type] = new BucketedDictionary(
                            dictionaryFilter.getFilteredWords(
                                    dictionaryManager.getDictionary(type)))
                }
            }
        }
        return bucketedDictionaries[type]
    }
}
