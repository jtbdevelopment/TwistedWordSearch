package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries

import com.jtbdevelopment.games.dictionary.Dictionary
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 8/19/16
 * Time: 6:48 AM
 */
@Component
@CompileStatic
class FilteredUSEnglishDictionary {
    private static final Logger logger = LoggerFactory.getLogger(FilteredUSEnglishDictionary.class)

    @Autowired
    private Dictionary dictionary

    public Set<String> getFilteredWords() {
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
