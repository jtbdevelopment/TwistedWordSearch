package com.jtbdevelopment.TwistedWordSearch.exceptions;

import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 8/25/16 Time: 8:21 PM
 */
public class NoPossibleLayoutsForWordExceptionTest {

  @Test
  public void testMessageIsBlank() {
    Assert.assertEquals("", new NoPossibleLayoutsForWordException().getMessage());
  }

}
