package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries;

import com.jtbdevelopment.games.dictionary.Dictionary;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/19/16 Time: 6:54 AM
 */
public class DictionaryFilterTest {

  private DictionaryFilter filter = new DictionaryFilter();

  @Test
  public void testGetFilteredWords() {
    Assert.assertEquals(new HashSet<>(Arrays.asList("ONE", "TWO", "THREE")),
        filter.getFilteredWords(new TestDictionary()));
  }

  private class TestDictionary implements Dictionary {

    private final HashSet<String> words = new HashSet<>(
        Arrays.asList("ONE", "BI-ANNUAL", "TWO", "DOG\'S", "THREE"));

    public Set<String> words() {
      return words;
    }

    public boolean isValidWord(final String input) {
      return false;
    }
  }
}
