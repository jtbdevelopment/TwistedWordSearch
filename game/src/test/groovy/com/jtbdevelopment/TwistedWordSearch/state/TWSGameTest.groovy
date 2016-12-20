package com.jtbdevelopment.TwistedWordSearch.state

/**
 * Date: 7/13/16
 * Time: 7:11 PM
 */
class TWSGameTest extends GroovyTestCase {
    TWSGame game = new TWSGame()

    void testInitialize() {
        assertNull game.grid
        assert 0 == game.wordAverageLengthGoal
        assert 0 == game.numberOfWords
        assert 0 == game.usableSquares
        assert 0 == game.hintsRemaining
        assert [:] == game.hintsGiven
        assertNull game.words
        assertNull game.wordsToFind
        assertNull game.wordsFoundByPlayer
        assertNull game.foundWordLocations
    }
}
