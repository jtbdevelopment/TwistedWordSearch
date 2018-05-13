package com.jtbdevelopment.TwistedWordSearch.rest.services;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.rest.handlers.NewGameHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 7/13/16 Time: 9:44 PM
 */
public class TWSPlayerServicesTest {

  private NewGameHandler newGameHandler = Mockito.mock(NewGameHandler.class);
  private TWSPlayerServices playerServices = new TWSPlayerServices(null, null, null, null, null,
      newGameHandler);

  @Test
  public void testCreateNewGame() {
    ObjectId APLAYER = new ObjectId();
    playerServices.getPlayerID().set(APLAYER);
    Set<GameFeature> features = new HashSet<>(
        Arrays.asList(GameFeature.Grid40X40, GameFeature.WordWrapYes));
    List<String> players = new ArrayList<String>(Arrays.asList("1", "2", "3"));
    FeaturesAndPlayers input = new FeaturesAndPlayers();
    input.setFeatures(features);
    input.setPlayers(players);
    MaskedGame game = new MaskedGame();
    Mockito.when(newGameHandler.handleCreateNewGame(APLAYER, players, features)).thenReturn(game);
    Assert.assertSame(game, playerServices.createNewGame(input));
  }

  @Test
  public void testCreateNewGameAnnotations() throws NoSuchMethodException {
    Method gameServices = TWSPlayerServices.class
        .getMethod("createNewGame", FeaturesAndPlayers.class);
    assertEquals(4, gameServices.getAnnotations().length);
    Assert.assertTrue(gameServices.isAnnotationPresent(Path.class));
    assertEquals("new", gameServices.getAnnotation(Path.class).value());
    Assert.assertTrue(gameServices.isAnnotationPresent(Consumes.class));
    Assert.assertArrayEquals(
        Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
        gameServices.getAnnotation(Consumes.class).value());
    Assert.assertTrue(gameServices.isAnnotationPresent(Produces.class));
    Assert.assertArrayEquals(
        Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
        gameServices.getAnnotation(Produces.class).value());
    Assert.assertTrue(gameServices.isAnnotationPresent(POST.class));
    Annotation[][] params = gameServices.getParameterAnnotations();
    assertEquals(1, params.length);
    assertEquals(0, params[0].length);
  }
}
