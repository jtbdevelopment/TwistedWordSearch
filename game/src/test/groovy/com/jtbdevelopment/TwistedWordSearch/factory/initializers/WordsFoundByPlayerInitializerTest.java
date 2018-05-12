package com.jtbdevelopment.TwistedWordSearch.factory.initializers;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.factory.GameInitializer;
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;
import org.bson.types.ObjectId;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.junit.Test;

/**
 * Date: 8/30/16 Time: 3:28 PM
 */
public class WordsFoundByPlayerInitializerTest extends MongoGameCoreTestCase {

    private WordsFoundByPlayerInitializer initializer = new WordsFoundByPlayerInitializer();

    @Test
    public void testInitializeGame() {
        TWSGame game = new TWSGame();
        game.setPlayers(Arrays.asList(PONE, PTHREE));
        initializer.initializeGame(game);

        LinkedHashMap<ObjectId, Set> map = new LinkedHashMap<ObjectId, Set>(2);
        map.put(PONE.getId(), DefaultGroovyMethods.asType(new ArrayList(), Set.class));
        map.put(PTHREE.getId(), DefaultGroovyMethods.asType(new ArrayList(), Set.class));
        assertEquals(map, game.getWordsFoundByPlayer());
        assertEquals(Collections.emptyMap(), game.getFoundWordLocations());
    }

    @Test
    public void testGetOrder() {
        assertEquals(GameInitializer.DEFAULT_ORDER, initializer.getOrder());
    }
}
