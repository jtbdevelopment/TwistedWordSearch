package com.jtbdevelopment.TwistedWordSearch.factory;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 7/13/16 Time: 9:32 PM
 */
public class TWSGameFactoryTest {

  private TWSGameFactory factory = new TWSGameFactory(Collections.emptyList(),
      Collections.emptyList());

  @Test
  public void testCreatesNewGame() {
    TWSGame game1 = factory.newGame();
    TWSGame game2 = factory.newGame();

    Assert.assertNotNull(game1);
    Assert.assertNotNull(game2);
    Assert.assertNotSame(game1, game2);
  }
}
