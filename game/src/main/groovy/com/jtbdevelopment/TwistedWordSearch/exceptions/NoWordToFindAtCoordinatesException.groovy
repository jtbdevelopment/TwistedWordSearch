package com.jtbdevelopment.TwistedWordSearch.exceptions

import com.jtbdevelopment.games.exceptions.GameInputException
import groovy.transform.CompileStatic

/**
 * Date: 9/3/2016
 * Time: 4:09 PM
 */
@CompileStatic
class NoWordToFindAtCoordinatesException extends GameInputException {
    private static final String MESSAGE = 'There are no word to find at that location.'

    public NoWordToFindAtCoordinatesException() {
        super(MESSAGE)
    }
}
