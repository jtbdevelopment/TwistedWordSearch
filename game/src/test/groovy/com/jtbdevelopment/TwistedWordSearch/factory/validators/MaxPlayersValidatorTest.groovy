package com.jtbdevelopment.TwistedWordSearch.factory.validators

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase

/**
 * Date: 9/24/2016
 * Time: 4:51 PM
 */
class MaxPlayersValidatorTest extends MongoGameCoreTestCase {
    MaxPlayersValidator validator = new MaxPlayersValidator()
    void testValidateGameOnePlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE]))
    }

    void testValidateGameTwoPlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE, PTWO]))
    }

    void testValidateGameThreePlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE, PTWO, PTHREE]))
    }

    void testValidateGameFourPlayerNotOK() {
        assertFalse validator.validateGame(new TWSGame(players: [PONE, PTWO, PTHREE, PFIVE]))
    }

    void testErrorMessage() {
        assert "Sorry, there are too many players - maximum is 3." == validator.errorMessage()
    }
}
