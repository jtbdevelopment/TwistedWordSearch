package com.jtbdevelopment.TwistedWordSearch.rest.services;

import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractPlayerRepository;
import com.jtbdevelopment.games.dao.StringToIDConverter;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.AbstractMultiPlayerServices;
import com.jtbdevelopment.games.rest.handlers.NewGameHandler;
import com.jtbdevelopment.games.rest.handlers.PlayerGamesFinderHandler;
import com.jtbdevelopment.games.rest.services.AbstractAdminServices;
import com.jtbdevelopment.games.rest.services.AbstractGameServices;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/14/14 Time: 6:40 AM
 */
@Component
public class TWSPlayerServices extends
    AbstractMultiPlayerServices<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {

  private final NewGameHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> newGameHandler;

  public TWSPlayerServices(
      final AbstractGameServices<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> gamePlayServices,
      final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
      final AbstractAdminServices<ObjectId, GameFeature, TWSGame, MongoPlayer> adminServices,
      final StringToIDConverter<ObjectId> stringToIDConverter,
      final PlayerGamesFinderHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> playerGamesFinderHandler,
      final NewGameHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> newGameHandler) {
    super(gamePlayServices, playerRepository, adminServices, stringToIDConverter,
        playerGamesFinderHandler);
    this.newGameHandler = newGameHandler;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("new")
  public Object createNewGame(final FeaturesAndPlayers featuresAndPlayers) {
    return newGameHandler
        .handleCreateNewGame(getPlayerID().get(), featuresAndPlayers.getPlayers(),
            featuresAndPlayers.getFeatures());
  }
}
