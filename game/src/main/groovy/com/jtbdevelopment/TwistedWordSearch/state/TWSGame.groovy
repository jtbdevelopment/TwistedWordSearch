package com.jtbdevelopment.TwistedWordSearch.state

import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
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
    int usableSquares // computation saver - should equal grid.getUsableSquaresCount when done initializing

    Set<String> words
    Set<String> wordsToFind
    Map<ObjectId, Set<String>> wordsFoundByPlayer

    //  TODO - map
}
