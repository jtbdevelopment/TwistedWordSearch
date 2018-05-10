package com.jtbdevelopment.TwistedWordSearch.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 9/3/2016 Time: 4:10 PM
 */
public class NoWordToFindAtCoordinatesExceptionTest {

  @Test
  public void testMessage() {
    Assert.assertEquals("There are no word to find at that location.",
        new NoWordToFindAtCoordinatesException().getMessage());
  }

}
