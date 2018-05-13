package com.jtbdevelopment.TwistedWordSearch.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 12/20/16 Time: 6:37 PM
 */
public class NoHintsRemainExceptionTest {

  @Test
  public void testMessage() {
    Assert.assertEquals("No hints remain in this game.", new NoHintsRemainException().getMessage());
  }

}
