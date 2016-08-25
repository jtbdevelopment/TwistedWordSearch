package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts

/**
 * Date: 8/25/16
 * Time: 7:06 AM
 */
class WordLayoutTest extends GroovyTestCase {
    void testPerRowLetterMovement() {
        assert [
                (WordLayout.HorizontalForward)  : 0,
                (WordLayout.HorizontalBackward) : 0,
                (WordLayout.VerticalDown)       : 1,
                (WordLayout.VerticalUp)         : -1,
                (WordLayout.SlopingDownForward) : 1,
                (WordLayout.SlopingDownBackward): -1,
                (WordLayout.SlopingUpForward)   : -1,
                (WordLayout.SlopingUpBackward)  : 1
        ] == WordLayout.values().collectEntries {
            [(it): it.perLetterRowMovement]
        }
    }

    void testPerColumnLetterMovement() {
        assert [
                (WordLayout.HorizontalForward)  : 1,
                (WordLayout.HorizontalBackward) : -1,
                (WordLayout.VerticalDown)       : 0,
                (WordLayout.VerticalUp)         : 0,
                (WordLayout.SlopingDownForward) : 1,
                (WordLayout.SlopingDownBackward): -1,
                (WordLayout.SlopingUpForward)   : 1,
                (WordLayout.SlopingUpBackward)  : -1
        ] == WordLayout.values().collectEntries {
            [(it): it.perLetterColumnMovement]
        }
    }
}
