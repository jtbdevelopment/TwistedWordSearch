package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.games.factory.GameInitializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Date: 8/30/16 Time: 6:35 AM
 */
@Component
public class WordChunkFillInitializer extends AbstractWordPlacementInitializer implements
    GameInitializer<TWSGame> {

  @SuppressWarnings("FieldCanBeLocal")
  private final double FILL_PERCENTAGE = 0.75;
  @SuppressWarnings("FieldCanBeLocal")
  private final double MAX_WORD_PERCENTAGE = 0.60;

  public WordChunkFillInitializer(
      final RandomLayoutPicker randomLayoutPicker) {
    super(randomLayoutPicker);
  }

  protected Collection<String> getWordsToPlace(final TWSGame game) {
    List<String> chunks = new ArrayList<>();
    if (game.getFeatures().contains(GameFeature.WordChunks)) {
      int desiredFill = (int) (game.getGrid().getQuestionMarkSquaresCount() * FILL_PERCENTAGE);
      int generated = 0;
      List<String> gameWordsList = new ArrayList<>(game.getWords());
      while (generated < desiredFill) {
        String word = gameWordsList.get(random.nextInt(gameWordsList.size()));
        int maxSize = (int) (word.length() * MAX_WORD_PERCENTAGE);
        int size = random.nextInt(maxSize) + 1;
        int start = random.nextInt(word.length() - size);
        chunks.add(word.substring(start, start + size));
        generated += size;
      }

    }

    return chunks;
  }

  protected void wordPlacedAt(final TWSGame game, final String word, final GridCoordinate start,
      final GridCoordinate end) {
    game.getWordEnds().put(word, end);
    game.getWordStarts().put(word, start);
  }

  public int getOrder() {
    return DEFAULT_ORDER + 20;
  }
}
