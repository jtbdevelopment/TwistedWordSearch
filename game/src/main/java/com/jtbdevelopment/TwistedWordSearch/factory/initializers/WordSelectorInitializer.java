package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionary;
import com.jtbdevelopment.TwistedWordSearch.factory.initializers.dictionaries.BucketedDictionaryManager;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.dictionary.DictionaryType;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Date: 8/17/16 Time: 6:41 PM
 */
@Component
public class WordSelectorInitializer implements GameInitializer<TWSGame> {

  private static final Map<GameFeature, DictionaryType> DICTIONARY_MAP = new HashMap<GameFeature, DictionaryType>() {{
    put(GameFeature.SimpleWords, DictionaryType.USEnglishSimple);
    put(GameFeature.StandardWords, DictionaryType.USEnglishModerate);
    put(GameFeature.HardWords, DictionaryType.USEnglishMaximum);
  }};
  private static final Random random = new Random();
  private final BucketedDictionaryManager dictionaryManager;

  public WordSelectorInitializer(
      BucketedDictionaryManager dictionaryManager) {
    this.dictionaryManager = dictionaryManager;
  }

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature wordDifficulty = game.getFeatures()
        .stream()
        .filter(f -> GameFeature.WordDifficulty.equals(f.getGroup()))
        .findFirst()
        .get();
    BucketedDictionary dictionary = dictionaryManager
        .getDictionary(DICTIONARY_MAP.get(wordDifficulty));

    int currentAvg = game.getWordAverageLengthGoal();
    Set<String> words = new HashSet<>();
    while (words.size() < game.getNumberOfWords()) {
      int goal = game.getWordAverageLengthGoal() - (currentAvg - game.getWordAverageLengthGoal());
      int lower = goal - 2;
      int upper = goal + 2;
      if (lower < 2) {
        lower = 2;
      }
      if (upper < lower) {
        upper = lower;
      }

      int range = upper - lower + 1;
      int sizeChoice = random.nextInt(range) + lower;
      List<String> wordBucket = dictionary.getWordsByLength().get(sizeChoice);
      words.add(wordBucket.get(random.nextInt(wordBucket.size())).toUpperCase());
      currentAvg = words.stream().mapToInt(String::length).sum() / words.size();
    }

    game.setWords(words);
    game.setWordsToFind(words);
  }

  public int getOrder() {
    return DEFAULT_ORDER;
  }
}
