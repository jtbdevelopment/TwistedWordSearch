package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import java.util.Collection;
import org.springframework.stereotype.Component;

/**
 * Date: 8/19/16 Time: 7:04 PM
 */
@Component
public class WordPlacementInitializer extends AbstractWordPlacementInitializer {

  public WordPlacementInitializer(
      final RandomLayoutPicker randomLayoutPicker) {
    super(randomLayoutPicker);
  }

  public int getOrder() {
    return DEFAULT_ORDER + 10;
  }

  protected Collection<String> getWordsToPlace(final TWSGame game) {
    return game.getWords();
  }

  protected void wordPlacedAt(final TWSGame game, final String word, final GridCoordinate start,
      final GridCoordinate end) {
    game.getWordStarts().put(word, start);
    game.getWordEnds().put(word, end);
  }

}
