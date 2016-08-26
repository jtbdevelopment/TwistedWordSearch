package com.jtbdevelopment.TwistedWordSearch.exceptions

/**
 * Date: 8/25/16
 * Time: 8:21 PM
 */
class NoPossibleLayoutsForWordExceptionTest extends GroovyTestCase {
    void testMessageIsBlank() {
        assert "" == new NoPossibleLayoutsForWordException().message
    }
}
