package com.jtbdevelopment.TwistedWordSearch.factory.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 9/24/2016 Time: 4:51 PM
 */
public class MaxPlayersValidatorTest extends MongoGameCoreTestCase {

  private MaxPlayersValidator validator = new MaxPlayersValidator();

  @Test
  public void testValidateGameOnePlayerOK() {
    TWSGame game = new TWSGame();
    game.setPlayers(Collections.singletonList(PONE));
    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testValidateGameTwoPlayerOK() {
    TWSGame game = new TWSGame();
    game.setPlayers(Arrays.asList(PONE, PTHREE));
    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testValidateGameThreePlayerOK() {
    TWSGame game = new TWSGame();
    game.setPlayers(Arrays.asList(PONE, PTHREE, PTWO));
    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testValidateGameFourPlayerOK() {
    TWSGame game = new TWSGame();
    game.setPlayers(Arrays.asList(PONE, PTHREE, PTWO, PFIVE));
    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testValidateGameFivePlayerOK() {
    TWSGame game = new TWSGame();
    game.setPlayers(Arrays.asList(PONE, PTHREE, PTWO, PFIVE, PINACTIVE1));
    assertTrue(validator.validateGame(game));
  }

  @Test
  public void testValidateGameSixPlayerNotOK() {
    TWSGame game = new TWSGame();
    game.setPlayers(Arrays.asList(PONE, PTHREE, PTWO, PFIVE, PINACTIVE1, PFOUR));
    assertFalse(validator.validateGame(game));
  }

  @Test
  public void testErrorMessage() {
    Assert.assertEquals("Sorry, there are too many players - maximum is 5.",
        validator.errorMessage());
  }
}
