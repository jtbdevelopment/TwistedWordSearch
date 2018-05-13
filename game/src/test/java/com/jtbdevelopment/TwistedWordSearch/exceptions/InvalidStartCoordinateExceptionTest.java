package com.jtbdevelopment.TwistedWordSearch.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 9/3/2016 Time: 4:00 PM
 */
public class InvalidStartCoordinateExceptionTest {

  @Test
  public void testMessage() {
    Assert.assertEquals("Invalid starting coordinate for word find.",
        new InvalidStartCoordinateException().getMessage());
  }

}
