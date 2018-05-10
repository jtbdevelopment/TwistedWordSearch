package com.jtbdevelopment.TwistedWordSearch.exceptions;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 9/3/2016 Time: 3:59 PM
 */
public class InvalidStartCoordinateException extends GameInputException {

  private static final String MESSAGE = "Invalid starting coordinate for word find.";

  public InvalidStartCoordinateException() {
    super(MESSAGE);
  }
}
