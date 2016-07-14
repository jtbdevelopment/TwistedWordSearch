package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.games.rest.services.AbstractPlayerServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:40 AM
 */
@Component
@CompileStatic
class TWSPlayerServices extends AbstractPlayerServices<ObjectId> {

//    @Autowired
//    NewGameHandler newGameHandler
    @Autowired
    PlayerGamesFinderHandler playerGamesFinderHandler

    @GET
    @Path("games")
    @Produces(MediaType.APPLICATION_JSON)
    public List gamesForPlayer() {
        playerGamesFinderHandler.findGames((ObjectId) playerID.get())
    }

    /*
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    MaskedMultiPlayerGame createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
        (AbstractMaskedMultiPlayerGame) newGameHandler.handleCreateNewGame(
                (Serializable) playerID.get(),
                featuresAndPlayers.players,
                featuresAndPlayers.features)
    }

    @PUT
    @Path('changeTheme/{newTheme}')
    @Produces(MediaType.APPLICATION_JSON)
    public Object changeTheme(@PathParam('newTheme') String newTheme) {
        Player player = playerRepository.findOne((ObjectId) playerID.get())
        TBPlayerAttributes playerAttributes = (TBPlayerAttributes) player.gameSpecificPlayerAttributes
        if (StringUtils.isEmpty(newTheme) || !playerAttributes.availableThemes.contains(newTheme)) {
            throw new NotAValidThemeException()
        }
        playerAttributes.theme = newTheme
        return playerRepository.save(player)
    }
    */
}
