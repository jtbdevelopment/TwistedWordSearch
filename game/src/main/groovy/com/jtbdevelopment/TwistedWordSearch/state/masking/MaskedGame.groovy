package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.state.masking.AbstractMaskedMultiPlayerGame
import groovy.transform.CompileStatic

/**
 * Date: 7/13/16
 * Time: 7:06 PM
 */
@CompileStatic
class MaskedGame extends AbstractMaskedMultiPlayerGame<GameFeature> {
    char[][] grid

    Set<String> wordsToFind
    int hintsRemaining
    Set<GridCoordinate> hints
    Map<String, Set<String>> wordsFoundByPlayer
    Map<String, Set<GridCoordinate>> foundWordLocations
    Map<String, Integer> scores
}
