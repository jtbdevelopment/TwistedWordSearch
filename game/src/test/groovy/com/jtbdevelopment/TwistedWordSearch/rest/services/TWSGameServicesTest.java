package com.jtbdevelopment.TwistedWordSearch.rest.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.jtbdevelopment.TwistedWordSearch.rest.handlers.GiveHintHandler;
import com.jtbdevelopment.TwistedWordSearch.rest.handlers.SubmitFindHandler;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 9/3/2016 Time: 6:58 PM
 */
public class TWSGameServicesTest extends MongoGameCoreTestCase {

    private GiveHintHandler giveHintHandler = Mockito.mock(GiveHintHandler.class);
    private SubmitFindHandler submitFindHandler = Mockito.mock(SubmitFindHandler.class);
    private TWSGameServices services = new TWSGameServices(null, null, null, null, null,
            submitFindHandler, giveHintHandler);

    @Test
    public void testActionAnnotations() {
        Map<String, List<Object>> stuff = new HashMap<>();
        stuff.put("findWord",
                Arrays.asList("find", Collections.singletonList(List.class), new ArrayList()));
        stuff.put("giveHint", Arrays.asList("hint", new ArrayList(), new ArrayList()));

        stuff.forEach((method, details) -> {
            final Method m;
            try {
                m = TWSGameServices.class
                        .getMethod(method, ((List<Class>) details.get(1)).toArray(new Class[0]));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            int expectedAnnotations = 4;
            assertEquals(expectedAnnotations, m.getAnnotations().length);
            assertTrue(m.isAnnotationPresent(PUT.class));
            assertTrue(m.isAnnotationPresent(Produces.class));
            assertArrayEquals(Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
                    m.getAnnotation(Produces.class).value());
            assertTrue(m.isAnnotationPresent(Path.class));
            assertEquals(details.get(0), m.getAnnotation(Path.class).value());
            assertTrue(m.isAnnotationPresent(Consumes.class));
            assertArrayEquals(Collections.singletonList(MediaType.APPLICATION_JSON).toArray(),
                    m.getAnnotation(Consumes.class).value());

        });
    }

    @Test
    public void testGiveHintHandler() {
        MaskedGame maskedGame = new MaskedGame();
        ObjectId gameId = new ObjectId();
        services.getPlayerID().set(PONE.getId());
        services.getGameID().set(gameId);
        when(giveHintHandler.handleAction(PONE.getId(), gameId)).thenReturn(maskedGame);
        assertSame(maskedGame, services.giveHint());
    }

    @Test
    public void testFindWordUsesHandler() {
        MaskedGame maskedGame = new MaskedGame();
        List<GridCoordinate> inputCoordinates = Arrays
                .asList(new GridCoordinate(0, 0), new GridCoordinate(1, 1),
                        new GridCoordinate(1, 1));
        ObjectId gameId = new ObjectId();
        services.getPlayerID().set(PONE.getId());
        services.getGameID().set(gameId);
        when(submitFindHandler.handleAction(PONE.getId(), gameId, inputCoordinates))
                .thenReturn(maskedGame);
        assertSame(maskedGame, services.findWord(inputCoordinates));

    }
}
