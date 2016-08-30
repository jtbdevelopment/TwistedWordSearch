package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.state.masking.AbstractMaskedMultiPlayerGame
import groovy.transform.CompileStatic

/**
 * Date: 7/13/16
 * Time: 7:06 PM
 */
@CompileStatic
class MaskedGame extends AbstractMaskedMultiPlayerGame<GameFeature> {
    Grid grid

    Set<String> wordsToFind
    Map<String, Set<String>> wordsFoundByPlayer
}
