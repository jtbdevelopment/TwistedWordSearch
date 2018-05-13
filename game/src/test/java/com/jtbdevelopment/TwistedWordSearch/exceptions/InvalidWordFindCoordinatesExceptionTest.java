package com.jtbdevelopment.TwistedWordSearch.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 9/3/2016 Time: 4:02 PM
 */
public class InvalidWordFindCoordinatesExceptionTest {

  @Test
  public void testMessage() {
    Assert.assertEquals("Invalid word find coordinates.",
        new InvalidWordFindCoordinatesException().getMessage());
  }

}
