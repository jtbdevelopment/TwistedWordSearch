package com.jtbdevelopment.TwistedWordSearch.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import org.junit.Test;

/**
 * Date: 5/30/18 Time: 6:53 AM
 */
public class GameFeatureToStringConverterTest {

  private GameFeatureToStringConverter converter = new GameFeatureToStringConverter();

  @Test
  public void testConverts() {
    assertEquals("HarderDifficulty", converter.convert(GameFeature.HarderDifficulty));
  }

  @Test
  public void testConvertsNull() {
    assertNull(converter.convert(null));
  }
}
