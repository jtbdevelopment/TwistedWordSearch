package com.jtbdevelopment.TwistedWordSearch.exceptions

import com.jtbdevelopment.games.exceptions.GameSystemException
import groovy.transform.CompileStatic

/**
 * Date: 8/25/16
 * Time: 8:20 PM
 */
@CompileStatic
class NoPossibleLayoutsForWordException extends GameSystemException {
    NoPossibleLayoutsForWordException() {
        super("")
    }
}
