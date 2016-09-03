package com.jtbdevelopment.TwistedWordSearch.exceptions

import com.jtbdevelopment.games.exceptions.GameInputException
import groovy.transform.CompileStatic

/**
 * Date: 9/3/2016
 * Time: 4:01 PM
 */
@CompileStatic
class InvalidWordFindCoordinatesException extends GameInputException {
    private static final String MESSAGE = 'Invalid word find coordinates.'

    public InvalidWordFindCoordinatesException() {
        super(MESSAGE)
    }
}
