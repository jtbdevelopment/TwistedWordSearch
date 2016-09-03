package com.jtbdevelopment.TwistedWordSearch.exceptions

/**
 * Date: 9/3/2016
 * Time: 4:02 PM
 */
class InvalidWordFindCoordinatesExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'Invalid word find coordinates.' == new InvalidWordFindCoordinatesException().message
    }
}
