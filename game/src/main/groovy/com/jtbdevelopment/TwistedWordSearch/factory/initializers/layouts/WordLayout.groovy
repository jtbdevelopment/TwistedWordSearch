package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts

/**
 * Date: 8/22/16
 * Time: 6:49 AM
 */
enum WordLayout {
    HorizontalForward(0, 1),
    HorizontalBackward(0, 1, true),
    VerticalDown(1, 0),
    VerticalUp(1, 0, true),
    SlopingDownForward(1, 1),
    SlopingDownBackward(1, 1, true),
    SlopingUpForward(-1, 1),
    SlopingUpBackward(-1, 1, true)

    boolean backwards = false
    int perLetterRowMovement
    int perLetterColumnMovement

    private WordLayout(int perLetterRowMovement, int perLetterColumnMovement, boolean backwards = false) {
        this.perLetterRowMovement = perLetterRowMovement
        this.perLetterColumnMovement = perLetterColumnMovement
        this.backwards = backwards
    }
}
