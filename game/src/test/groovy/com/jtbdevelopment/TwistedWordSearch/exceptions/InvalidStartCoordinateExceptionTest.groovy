package com.jtbdevelopment.TwistedWordSearch.exceptions

/**
 * Date: 9/3/2016
 * Time: 4:00 PM
 */
class InvalidStartCoordinateExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'Invalid starting coordinate for word find.' == new InvalidStartCoordinateException().message
    }
}
