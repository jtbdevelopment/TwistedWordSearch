package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Date: 8/19/16 Time: 6:59 AM
 */
public class BucketedDictionary {

  private final Map<Integer, List<String>> wordsByLength = new HashMap<>();

  public BucketedDictionary() {
    this(Collections.emptySet());
  }

  public BucketedDictionary(final Set<String> filteredWords) {
    filteredWords.forEach(word -> {
      int size = word.length();
      wordsByLength.putIfAbsent(size, new LinkedList<>());
      wordsByLength.getOrDefault(size, new LinkedList<>()).add(word);
    });
  }

  public Map<Integer, List<String>> getWordsByLength() {
    return wordsByLength;
  }
}
