package com.jtbdevelopment.TwistedWordSearch.exceptions;

import com.jtbdevelopment.games.exceptions.GameInputException;

/**
 * Date: 12/20/16 Time: 6:36 PM
 */
public class NoHintsRemainException extends GameInputException {

  private static String MESSAGE = "No hints remain in this game.";

  public NoHintsRemainException() {
    super(MESSAGE);
  }
}
