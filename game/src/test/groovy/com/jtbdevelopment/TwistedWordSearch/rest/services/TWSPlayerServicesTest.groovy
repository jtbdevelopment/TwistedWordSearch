package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers
import com.jtbdevelopment.TwistedWordSearch.rest.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.rest.handlers.NewGameHandler
import groovy.transform.TypeChecked
import org.bson.types.ObjectId

import javax.ws.rs.*
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

    void testCreateNewGame() {
        def APLAYER = new ObjectId()
        playerServices.playerID.set(APLAYER)
        def features = [GameFeature.Grid40X40, GameFeature.HideWordLetters] as Set
        def players = ["1", "2", "3"]
        FeaturesAndPlayers input = new FeaturesAndPlayers(features: features, players: players)
        MaskedGame game = new MaskedGame()
        playerServices.newGameHandler = [
                handleCreateNewGame: {
                    ObjectId i, List<String> p, Set<GameFeature> f ->
                        assert i == APLAYER
                        assert p == players
                        assert f == features
                        game
                }
        ] as NewGameHandler
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
