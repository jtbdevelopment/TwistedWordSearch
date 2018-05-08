package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.dao.StringToIDConverter
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.rest.services.AbstractAdminServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

/**
 * Date: 11/27/2014
 * Time: 6:34 PM
 */
@Component
@CompileStatic
class TWSAdminServices extends AbstractAdminServices<ObjectId, GameFeature, TWSGame, MongoPlayer> {
    TWSAdminServices(
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository,
            final AbstractGameRepository<ObjectId, GameFeature, TWSGame> gameRepository,
            final StringToIDConverter<ObjectId> stringToIDConverter) {
        super(playerRepository, gameRepository, stringToIDConverter)
    }
}
