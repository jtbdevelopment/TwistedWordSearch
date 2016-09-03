package com.jtbdevelopment.TwistedWordSearch.exceptions

/**
 * Date: 9/3/2016
 * Time: 4:10 PM
 */
class NoWordToFindAtCoordinatesExceptionTest extends GroovyTestCase {
    void testMessage() {
        assert 'There are no word to find at that location.' == new NoWordToFindAtCoordinatesException().message
    }
}
