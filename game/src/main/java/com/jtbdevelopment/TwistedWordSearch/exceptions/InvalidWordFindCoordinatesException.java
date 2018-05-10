package com.jtbdevelopment.TwistedWordSearch.exceptions;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 9/3/2016 Time: 4:01 PM
 */
public class InvalidWordFindCoordinatesException extends GameInputException {

  private static final String MESSAGE = "Invalid word find coordinates.";

  public InvalidWordFindCoordinatesException() {
    super(MESSAGE);
  }
}
