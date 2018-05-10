package com.jtbdevelopment.TwistedWordSearch.exceptions;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 9/3/2016 Time: 4:09 PM
 */
public class NoWordToFindAtCoordinatesException extends GameInputException {

  private static final String MESSAGE = "There are no word to find at that location.";

  public NoWordToFindAtCoordinatesException() {
    super(MESSAGE);
  }
}
