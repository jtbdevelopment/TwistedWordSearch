package com.jtbdevelopment.TwistedWordSearch.player;

import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 7/27/16 Time: 6:39 PM
 */
public class TWSPlayerAttributesFactoryTest {

  private TWSPlayerAttributesFactory factory = new TWSPlayerAttributesFactory();

  @Test
  public void testNewPlayerAttributes() {
    Assert.assertTrue(factory.newPlayerAttributes() instanceof TWSPlayerAttributes);
  }

  @Test
  public void testNewManualPlayerAttributes() {
    Assert.assertTrue(factory.newManualPlayerAttributes() instanceof TWSPlayerAttributes);
  }

  @Test
  public void testNewSystemPlayerAttributes() {
    Assert.assertNull(factory.newSystemPlayerAttributes());
  }
}
