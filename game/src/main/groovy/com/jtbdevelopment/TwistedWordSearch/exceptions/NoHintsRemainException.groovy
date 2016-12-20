package com.jtbdevelopment.TwistedWordSearch.exceptions

import com.jtbdevelopment.games.exceptions.GameInputException
import groovy.transform.CompileStatic

/**
 * Date: 12/20/16
 * Time: 6:36 PM
 */
@CompileStatic
class NoHintsRemainException extends GameInputException {
    private static String MESSAGE = "No hints remain in this game."

    NoHintsRemainException() {
        super(MESSAGE)
    }
}
