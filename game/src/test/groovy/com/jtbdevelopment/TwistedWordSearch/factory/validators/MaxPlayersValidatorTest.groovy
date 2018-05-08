package com.jtbdevelopment.TwistedWordSearch.factory.validators

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import org.junit.Test

import static org.junit.Assert.assertFalse

/**
 * Date: 9/24/2016
 * Time: 4:51 PM
 */
class MaxPlayersValidatorTest extends MongoGameCoreTestCase {
    MaxPlayersValidator validator = new MaxPlayersValidator()

    @Test
    void testValidateGameOnePlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE]))
    }

    @Test
    void testValidateGameTwoPlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE, PTWO]))
    }

    @Test
    void testValidateGameThreePlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE, PTWO, PTHREE]))
    }

    @Test
    void testValidateGameFourPlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE, PTWO, PTHREE, PFIVE]))
    }

    @Test
    void testValidateGameFivePlayerOK() {
        assert validator.validateGame(new TWSGame(players: [PONE, PTWO, PTHREE, PFIVE, PINACTIVE1]))
    }

    @Test
    void testValidateGameSixPlayerNotOK() {
        assertFalse validator.validateGame(new TWSGame(players: [PONE, PTWO, PTHREE, PFIVE, PINACTIVE1, PFOUR]))
    }

    @Test
    void testErrorMessage() {
        assert "Sorry, there are too many players - maximum is 5." == validator.errorMessage()
    }
}
