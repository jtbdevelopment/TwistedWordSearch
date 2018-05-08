package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.dao.StringToIDConverter
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.rest.AbstractMultiPlayerServices
import com.jtbdevelopment.games.rest.handlers.NewGameHandler
import com.jtbdevelopment.games.rest.handlers.PlayerGamesFinderHandler
import com.jtbdevelopment.games.rest.services.AbstractAdminServices
import com.jtbdevelopment.games.rest.services.AbstractGameServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/14/14
 * Time: 6:40 AM
 */
@Component
@CompileStatic
class TWSPlayerServices extends AbstractMultiPlayerServices<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {

    TWSPlayerServices(
            final AbstractGameServices<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> gamePlayServices,
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
            final AbstractAdminServices<ObjectId, GameFeature, TWSGame, MongoPlayer> adminServices,
            final StringToIDConverter<ObjectId> stringToIDConverter,
            final PlayerGamesFinderHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> playerGamesFinderHandler,
            final NewGameHandler newGameHandler) {
        super(gamePlayServices, playerRepository, adminServices, stringToIDConverter, playerGamesFinderHandler)
        this.newGameHandler = newGameHandler
    }
    private final NewGameHandler newGameHandler

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("new")
    Object createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
        newGameHandler.handleCreateNewGame(
                (Serializable) playerID.get(),
                featuresAndPlayers.players,
                featuresAndPlayers.features)
    }

    /*
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
