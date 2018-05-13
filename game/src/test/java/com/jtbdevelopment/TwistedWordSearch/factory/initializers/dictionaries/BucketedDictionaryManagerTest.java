package com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries;

import static org.mockito.Mockito.when;

import com.jtbdevelopment.games.dictionary.Dictionary;
import com.jtbdevelopment.games.dictionary.DictionaryManager;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 10/1/2016 Time: 2:47 PM
 */
public class BucketedDictionaryManagerTest {

  private Dictionary simple = Mockito.mock(Dictionary.class);
  private Dictionary moderate = Mockito.mock(Dictionary.class);
  private Dictionary hard = Mockito.mock(Dictionary.class);
  private DictionaryManager manager = Mockito.mock(DictionaryManager.class);
  private DictionaryFilter filter = new DictionaryFilter() {
    @Override
    public Set<String> getFilteredWords(Dictionary dictionary) {
      Set<String> filtered = new HashSet<>(dictionary.words());
      filtered.remove("WORDS");
      return filtered;
    }

  };
  private BucketedDictionaryManager bucketedDictionaryManager = new BucketedDictionaryManager(
      filter, manager);

  @Before
  public void setup() {
    when(simple.words()).thenReturn(new HashSet<>(Arrays.asList("SIMPLE", "WORDS")));
    when(moderate.words())
        .thenReturn(new HashSet<>(Arrays.asList("MORE", "COMPLEX", "WORDS")));
    when(hard.words()).thenReturn(
        new HashSet<>(Arrays.asList("GONNA", "NEED", "A", "DICTIONARY", "FOR", "WORDS")));
    when(manager.getDictionary(DictionaryType.USEnglishMaximum)).thenReturn(hard);
    when(manager.getDictionary(DictionaryType.USEnglishModerate)).thenReturn(moderate);
    when(manager.getDictionary(DictionaryType.USEnglishSimple)).thenReturn(simple);
  }

  @Test
  public void testGetsABucketedDictionaryOnce() {
    Arrays.stream(DictionaryType.values()).forEach(type ->
        Assert.assertSame(
            bucketedDictionaryManager.getDictionary(type),
            bucketedDictionaryManager.getDictionary(type)));
  }

  @Test
  public void testGetBucketedDictionarySimple() {
    LinkedHashMap<Integer, List<String>> map = new LinkedHashMap<>(1);
    map.put(6, new ArrayList<>(Collections.singletonList("SIMPLE")));
    Assert.assertEquals(map,
        bucketedDictionaryManager.getDictionary(DictionaryType.USEnglishSimple).getWordsByLength());
  }

  @Test
  public void testGetBucketedDictionaryModerate() {
    LinkedHashMap<Integer, List<String>> map = new LinkedHashMap<>(2);
    map.put(4, new ArrayList<>(Collections.singletonList("MORE")));
    map.put(7, new ArrayList<>(Collections.singletonList("COMPLEX")));
    Assert.assertEquals(map,
        bucketedDictionaryManager.getDictionary(DictionaryType.USEnglishModerate)
            .getWordsByLength());
  }

  @Test
  public void testGetBucketedDictionaryMax() {
    LinkedHashMap<Integer, List<String>> map = new LinkedHashMap<>(5);
    map.put(1, new ArrayList<>(Collections.singletonList("A")));
    map.put(3, new ArrayList<>(Collections.singletonList("FOR")));
    map.put(4, new ArrayList<>(Collections.singletonList("NEED")));
    map.put(5, new ArrayList<>(Collections.singletonList("GONNA")));
    map.put(10, new ArrayList<>(Collections.singletonList("DICTIONARY")));
    Assert.assertEquals(map,
        bucketedDictionaryManager.getDictionary(DictionaryType.USEnglishMaximum)
            .getWordsByLength());
  }
}
