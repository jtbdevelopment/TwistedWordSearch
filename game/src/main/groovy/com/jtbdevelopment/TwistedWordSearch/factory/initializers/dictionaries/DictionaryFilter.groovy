package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import com.jtbdevelopment.games.dictionary.Dictionary
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Date: 8/19/16
 * Time: 6:48 AM
 */
@CompileStatic
@Component
class DictionaryFilter {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryFilter.class)

    @SuppressWarnings("GrMethodMayBeStatic")
    public Set<String> getFilteredWords(final Dictionary dictionary) {
        logger.info("Filtering dictionary")
        def unfilteredWords = dictionary.words()
        Set<String> filteredWords = unfilteredWords.findAll {
            String word ->
                !(word.toCharArray().find {
                    char c ->
                        !c.isLetter()
                })
        }
        logger.info("Filtered dictionary")
        return filteredWords

    }
}
