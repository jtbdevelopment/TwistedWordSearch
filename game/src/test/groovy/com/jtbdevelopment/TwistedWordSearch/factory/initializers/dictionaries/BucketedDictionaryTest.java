package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/19/16 Time: 7:01 AM
 */
public class BucketedDictionaryTest {

  @Test
  public void testCreatesBucketedWords() {
    Set<String> words = new HashSet<>(
        Arrays.asList("ONE", "TWO", "THREE", "FOUR", "FIVE", "BIGWORDOVERHERE"));
    BucketedDictionary bucketedUSEnglishDictionary = new BucketedDictionary(words);

    Map<Integer, List<String>> map = new HashMap<>();
    map.put(3, Arrays.asList("ONE", "TWO"));
    map.put(5, Collections.singletonList("THREE"));
    map.put(4, Arrays.asList("FIVE", "FOUR"));
    map.put(15, Collections.singletonList("BIGWORDOVERHERE"));
    Assert.assertEquals(map, bucketedUSEnglishDictionary.getWordsByLength());
  }

}
