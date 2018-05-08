package com.jtbdevelopment.TwistedWordSearch.rest.services;

import com.jtbdevelopment.TwistedWordSearch.rest.handlers.GiveHintHandler;
import com.jtbdevelopment.TwistedWordSearch.rest.handlers.SubmitFindHandler;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.AbstractMultiPlayerGameServices;
import com.jtbdevelopment.games.rest.handlers.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Date: 11/11/14
 * Time: 9:42 PM
 */
@Component
public class TWSGameServices extends AbstractMultiPlayerGameServices<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {
    private final SubmitFindHandler submitFindHandler;
    private final GiveHintHandler giveHintHandler;

    TWSGameServices(final GameGetterHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> gameGetterHandler, final DeclineRematchOptionHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> declineRematchOptionHandler, final ChallengeResponseHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> responseHandler, final ChallengeToRematchHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> rematchHandler, final QuitHandler<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> quitHandler, final SubmitFindHandler submitFindHandler, final GiveHintHandler giveHintHandler) {
        super(gameGetterHandler, declineRematchOptionHandler, responseHandler, rematchHandler, quitHandler);
        this.submitFindHandler = submitFindHandler;
        this.giveHintHandler = giveHintHandler;
    }

    @PUT
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object findWord(final List<GridCoordinate> relativeCoordinates) {
        return submitFindHandler.handleAction(getPlayerID().get(), getGameID().get(), relativeCoordinates);
    }

    @PUT
    @Path("hint")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object giveHint() {
        return giveHintHandler.handleAction(getPlayerID().get(), getGameID().get());
    }
}
