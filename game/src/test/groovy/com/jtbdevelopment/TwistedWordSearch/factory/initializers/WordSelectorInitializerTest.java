package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionary;
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionaryManager;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 8/19/16 Time: 6:31 PM
 */
public class WordSelectorInitializerTest {

  private BucketedDictionary dictionary = Mockito.mock(BucketedDictionary.class);
  private BucketedDictionaryManager bucketedDictionaryManager = Mockito
      .mock(BucketedDictionaryManager.class);
  private WordSelectorInitializer initializer = new WordSelectorInitializer(
      bucketedDictionaryManager);
  private Map<GameFeature, DictionaryType> EXPECTED_DICTIONARY = new HashMap<GameFeature, DictionaryType>() {{
    put(GameFeature.SimpleWords, DictionaryType.USEnglishSimple);
    put(GameFeature.StandardWords, DictionaryType.USEnglishModerate);
    put(GameFeature.HardWords, DictionaryType.USEnglishMaximum);
  }};
  private Map<Integer, List<String>> wordsByLength = new HashMap<Integer, List<String>>() {
    {
      put(2, Arrays.asList("OF", "AT"));
      put(3, Collections.singletonList("ONE"));
      put(4, Collections.singletonList("FOUR"));
      put(5, Collections.singletonList("FIRED"));
      put(6, Collections.singletonList("FORGED"));
      put(7, Collections.singletonList("FORGETS"));
      put(8, Collections.singletonList("DOCTORED"));
      put(9, Collections.singletonList("DOCTORING"));
      put(10, Collections.singletonList("0123456789"));

    }
  };
  private List<Set<String>> validCombos = Arrays.asList(
      new TreeSet<>(Arrays.asList("FOUR", "FORGETS")),
      new TreeSet<>(Arrays.asList("OF", "FORGETS")),
      new TreeSet<>(Arrays.asList("FORGED", "ONE")),
      new TreeSet<>(Arrays.asList("FIRED", "ONE")),
      new TreeSet<>(Arrays.asList("FIRED", "FORGED")),
      new TreeSet<>(Arrays.asList("ONE", "DOCTORING")),
      new TreeSet<>(Arrays.asList("AT", "FORGETS")),
      new TreeSet<>(Arrays.asList("FIRED", "FOUR")),
      new TreeSet<>(Arrays.asList("FORGED", "OF")),
      new TreeSet<>(Arrays.asList("FORGED", "FOUR")),
      new TreeSet<>(Arrays.asList("FORGED", "AT")),
      new TreeSet<>(Arrays.asList("ONE", "FORGETS")),
      new TreeSet<>(Arrays.asList("DOCTORED", "ONE")),
      new TreeSet<>(Arrays.asList("DOCTORED", "FOUR")),
      new TreeSet<>(Arrays.asList("FIRED", "FORGETS"))
  );

  @Test
  public void testInitializeGame() {
    when(dictionary.getWordsByLength()).thenReturn(wordsByLength);
    Arrays.stream(GameFeature.values())
        .filter(g -> GameFeature.WordDifficulty.equals(g.getGroup()))
        .forEach(wordType -> {
          reset(bucketedDictionaryManager);
          when(bucketedDictionaryManager.getDictionary(EXPECTED_DICTIONARY.get(wordType)))
              .thenReturn(dictionary);
          TWSGame game = new TWSGame();
          game.setWordAverageLengthGoal(5);
          game.setNumberOfWords(2);
          game.setFeatures(new HashSet<>(Collections.singletonList(wordType)));
          initializer.initializeGame(game);
          assertNotNull(game.getWords());
          Set<String> words = game.getWords();
          assertTrue(validCombos.stream().anyMatch(combo -> combo.containsAll(words)));
          assertEquals(game.getWords(), game.getWordsToFind());
        });
  }

  @Test
  public void testGetOrder() {
    assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
  }
}
