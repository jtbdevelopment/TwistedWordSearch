package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Date: 8/26/16 Time: 6:57 PM
 */
@Component
public class RandomFillInitializer implements GameInitializer<TWSGame> {

  private static Map<GameFeature, Double> RANDOM_PERCENT = new HashMap<GameFeature, Double>() {{
    put(GameFeature.RandomFill, 1.0);
    put(GameFeature.SomeOverlap, 0.75);
    put(GameFeature.StrongOverlap, 0.50);
    put(GameFeature.WordChunks, 1.0);
  }};
  private static char[] RANDOM_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private Random random = new Random();

  public void initializeGame(final TWSGame game) {
    //noinspection ConstantConditions
    GameFeature fillDifficulty = game.getFeatures()
        .stream()
        .filter(f -> GameFeature.FillDifficulty.equals(f.getGroup()))
        .findFirst()
        .get();

    Set<Character> wordLettersHash = new HashSet<>();
    game.getWords()
        .forEach(word -> word.chars().forEach(letter -> wordLettersHash.add((char) letter)));
    final Character[] wordLetters = wordLettersHash.toArray(new Character[wordLettersHash.size()]);
    final List<GridCoordinate> coordinatesToFill = getCoordinatesToFill(game);
    int randomLetters = (int) (RANDOM_PERCENT.get(fillDifficulty) * coordinatesToFill.size());
    int nonRandomLetters = coordinatesToFill.size() - randomLetters;

    if (randomLetters > 0) {
      for (int currentFull = 0; currentFull < randomLetters; ++currentFull) {
        char fill = RANDOM_POOL[random.nextInt(RANDOM_POOL.length)];
        int randomCoordinate = random.nextInt(coordinatesToFill.size());
        game.getGrid().setGridCell(coordinatesToFill.get(randomCoordinate), fill);
        coordinatesToFill.remove(randomCoordinate);
      }
    }

    if (nonRandomLetters > 0) {
      for (int currentFull = 0; currentFull < nonRandomLetters; ++currentFull) {
        Character fill = wordLetters[random.nextInt(wordLetters.length)];
        int randomCoordinate = random.nextInt(coordinatesToFill.size());
        game.getGrid().setGridCell(coordinatesToFill.get(randomCoordinate), fill);
        coordinatesToFill.remove(randomCoordinate);
      }
    }

  }

  private List<GridCoordinate> getCoordinatesToFill(final TWSGame game) {
    List<GridCoordinate> coordinates = new ArrayList<>();
    for (int row = 0; row < game.getGrid().getRows(); ++row) {
      for (int col = 0; col < game.getGrid().getColumns(); ++col) {
        if (Grid.QUESTION_MARK == game.getGrid().getGridCell(row, col)) {
          coordinates.add(new GridCoordinate(row, col));
        }

      }
    }
    return coordinates;
  }

  public int getOrder() {
    return DEFAULT_ORDER + 30;
  }
}
