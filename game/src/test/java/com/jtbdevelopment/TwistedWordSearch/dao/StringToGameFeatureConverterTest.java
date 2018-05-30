package com.jtbdevelopment.TwistedWordSearch.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import org.junit.Test;

/**
 * Date: 5/30/18 Time: 6:51 AM
 */
public class StringToGameFeatureConverterTest {

  private StringToGameFeatureConverter converter = new StringToGameFeatureConverter();

  @Test
  public void testConverts() {
    assertEquals(GameFeature.Diamond30X30, converter.convert("Diamond30X30"));
  }

  @Test
  public void testIgnoresNull() {
    assertNull(converter.convert(null));
  }
}
