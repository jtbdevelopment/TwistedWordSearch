package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries;

import com.jtbdevelopment.games.dictionary.Dictionary;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 8/19/16 Time: 6:48 AM
 */
@Component
public class DictionaryFilter {

  private static final Logger logger = LoggerFactory.getLogger(DictionaryFilter.class);

  public Set<String> getFilteredWords(final Dictionary dictionary) {
    logger.info("Filtering dictionary");
    Set<String> unfilteredWords = dictionary.words();
    Set<String> filteredWords = unfilteredWords.stream()
        .filter(word -> word.chars().allMatch(Character::isAlphabetic))
        .collect(Collectors.toSet());
    logger.info("Filtered dictionary");
    return filteredWords;

  }
}
