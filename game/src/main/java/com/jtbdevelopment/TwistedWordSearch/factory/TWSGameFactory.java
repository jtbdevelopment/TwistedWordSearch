package com.jtbdevelopment.TwistedWordSearch.factory;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.AbstractMultiPlayerGameFactory;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.factory.GameValidator;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 7/13/16 Time: 9:30 PM
 */
@Component
public class TWSGameFactory extends AbstractMultiPlayerGameFactory<ObjectId, GameFeature, TWSGame> {

  public TWSGameFactory(
      final List<GameInitializer> gameInitializers,
      final List<GameValidator> gameValidators) {
    super(gameInitializers, gameValidators);
  }

  protected TWSGame newGame() {
    return new TWSGame();
  }

}
