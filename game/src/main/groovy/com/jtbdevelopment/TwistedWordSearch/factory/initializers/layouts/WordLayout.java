package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts;

/**
 * Date: 8/22/16 Time: 6:49 AM
 */
public enum WordLayout {
  HorizontalForward(0, 1),
  HorizontalBackward(0, -1),
  VerticalDown(1, 0),
  VerticalUp(-1, 0),
  SlopingDownForward(1, 1),
  SlopingDownBackward(-1, -1),
  SlopingUpForward(-1, 1),
  SlopingUpBackward(1, -1);

  private int perLetterRowMovement;
  private int perLetterColumnMovement;

  WordLayout(int perLetterRowMovement, int perLetterColumnMovement) {
    this.perLetterRowMovement = perLetterRowMovement;
    this.perLetterColumnMovement = perLetterColumnMovement;
  }

  public int getPerLetterRowMovement() {
    return perLetterRowMovement;
  }

  public int getPerLetterColumnMovement() {
    return perLetterColumnMovement;
  }
}
