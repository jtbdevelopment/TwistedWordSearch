package com.jtbdevelopment.TwistedWordSearch.exceptions

/**
 * Date: 12/20/16
 * Time: 6:37 PM
 */
class NoHintsRemainExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert "No hints remain in this game." == new NoHintsRemainException().message
    }
}
