package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import groovy.transform.TypeChecked
import org.bson.types.ObjectId

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 7/13/16
 * Time: 9:44 PM
 */
class TWSPlayerServicesTest extends GroovyTestCase {
    TWSPlayerServices playerServices = new TWSPlayerServices()

    void testGetGames() {
        def APLAYER = new ObjectId()
        def results = [new TWSGame(), new TWSGame(), new TWSGame()]
        playerServices.playerGamesFinderHandler = [
                findGames: {
                    ObjectId it ->
                        assert it == APLAYER
                        return results
                }
        ] as PlayerGamesFinderHandler
        playerServices.playerID.set(APLAYER)
        assert results.is(playerServices.gamesForPlayer())
    }

    void testGamesAnnotations() {
        def gameServices = TWSPlayerServices.getMethod("gamesForPlayer", [] as Class[])
        assert (gameServices.annotations.size() == 3 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 4)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "games"
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(GET.class)
        def params = gameServices.parameterAnnotations
        assert params.length == 0
    }

}
