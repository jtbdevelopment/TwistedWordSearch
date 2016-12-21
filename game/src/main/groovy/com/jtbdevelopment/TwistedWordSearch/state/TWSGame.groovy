package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.mongo.state.AbstractMongoMultiPlayerGame
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Date: 7/13/16
 * Time: 7:04 PM
 */
@CompileStatic
@Document(collection = 'game')
class TWSGame extends AbstractMongoMultiPlayerGame<GameFeature> {
    Grid grid
    int numberOfWords
    int wordAverageLengthGoal
    int hintsRemaining
    int usableSquares // computation saver - should equal grid.getUsableSquaresCount when done initializing

    Set<String> words
    Map<String, GridCoordinate> wordStarts = [:]
    Map<String, GridCoordinate> wordEnds = [:]
    Set<String> wordsToFind
    Map<String, GridCoordinate> hintsGiven = [:]
    Map<ObjectId, Set<String>> wordsFoundByPlayer
    Map<String, Set<GridCoordinate>> foundWordLocations

    Map<ObjectId, Integer> scores

    List<ObjectId> winners  // in case there is tie
}
