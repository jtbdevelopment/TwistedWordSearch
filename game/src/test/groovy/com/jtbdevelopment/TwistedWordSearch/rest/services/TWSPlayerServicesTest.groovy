package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.rest.handlers.NewGameHandler
import groovy.transform.TypeChecked
import org.bson.types.ObjectId
import org.mockito.Mockito

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

import static org.mockito.Mockito.mock

/**
 * Date: 7/13/16
 * Time: 9:44 PM
 */
class TWSPlayerServicesTest extends GroovyTestCase {
    private NewGameHandler newGameHandler = mock(NewGameHandler.class)
    private TWSPlayerServices playerServices = new TWSPlayerServices(null, null, null, null, null, newGameHandler)

    void testCreateNewGame() {
        def APLAYER = new ObjectId()
        playerServices.playerID.set(APLAYER)
        def features = [GameFeature.Grid40X40, GameFeature.WordWrapYes] as Set
        def players = ["1", "2", "3"]
        FeaturesAndPlayers input = new FeaturesAndPlayers(features: features, players: players)
        MaskedGame game = new MaskedGame()
        Mockito.when(newGameHandler.handleCreateNewGame(APLAYER, players, features)).thenReturn(game)
        assert game.is(playerServices.createNewGame(input))
    }

    void testCreateNewGameAnnotations() {
        def gameServices = TWSPlayerServices.getMethod("createNewGame", [FeaturesAndPlayers.class] as Class[])
        assert (gameServices.annotations.size() == 4 ||
                (gameServices.isAnnotationPresent(TypeChecked.TypeCheckingInfo) && gameServices.annotations.size() == 5)
        )
        assert gameServices.isAnnotationPresent(Path.class)
        assert gameServices.getAnnotation(Path.class).value() == "new"
        assert gameServices.isAnnotationPresent(Consumes.class)
        assert gameServices.getAnnotation(Consumes.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(Produces.class)
        assert gameServices.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
        assert gameServices.isAnnotationPresent(POST.class)
        def params = gameServices.parameterAnnotations
        assert params.length == 1
        assert params[0].length == 0
    }

}
