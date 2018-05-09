package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries;

import com.jtbdevelopment.games.dictionary.DictionaryManager;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Date: 10/1/2016 Time: 12:58 PM
 */
@Component
public class BucketedDictionaryManager {

  private final Map<DictionaryType, BucketedDictionary> bucketedDictionaries = new HashMap<>();
  private DictionaryFilter dictionaryFilter;
  private DictionaryManager dictionaryManager;

  public BucketedDictionaryManager(
      final DictionaryFilter dictionaryFilter,
      final DictionaryManager dictionaryManager) {
    this.dictionaryFilter = dictionaryFilter;
    this.dictionaryManager = dictionaryManager;
  }

  public BucketedDictionary getDictionary(final DictionaryType type) {
    if (!bucketedDictionaries.containsKey(type)) {
      synchronized (bucketedDictionaries) {
        if (!bucketedDictionaries.containsKey(type)) {
          bucketedDictionaries.put(type, new BucketedDictionary(
              dictionaryFilter.getFilteredWords(dictionaryManager.getDictionary(type))));
        }

      }

    }

    return bucketedDictionaries.get(type);
  }
}
