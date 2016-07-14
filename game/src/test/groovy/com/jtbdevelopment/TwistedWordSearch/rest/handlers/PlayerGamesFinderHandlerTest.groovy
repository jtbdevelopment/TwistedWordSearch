package com.jtbdevelopment.TwistedWordSearch.rest.handlers

import com.jtbdevelopment.TwistedWordSearch.dao.GameRepository
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.masking.TWSMaskedGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.state.GamePhase
import com.jtbdevelopment.games.state.masking.MultiPlayerGameMasker
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest

import java.time.ZonedDateTime

/**
 * Date: 7/13/16
 * Time: 9:42 PM
 */
class PlayerGamesFinderHandlerTest extends MongoGameCoreTestCase {
    PlayerGamesFinderHandler handler = new PlayerGamesFinderHandler()

    void testTest() {
        def game1 = new TWSGame(id: new ObjectId())
        def game2 = new TWSGame(id: new ObjectId())
        def game3 = new TWSGame(id: new ObjectId())
        def masked1 = new TWSMaskedGame(id: "1")
        def masked2 = new TWSMaskedGame(id: "2")
        def masked3 = new TWSMaskedGame(id: "3")
        def queryResults = [
                (GamePhase.Challenged)      : [game1],
                (GamePhase.Declined)        : [],
                (GamePhase.NextRoundStarted): [game2],
                (GamePhase.Playing)         : [],
                (GamePhase.Quit)            : [],
                (GamePhase.RoundOver)       : [game3],
                (GamePhase.Setup)           : [],
        ]
        def maskResults = [
                (game1): masked1,
                (game2): masked2,
                (game3): masked3
        ]
        handler.playerRepository = [
                findOne: {
                    ObjectId it ->
                        assert it == PONE.id
                        return PONE
                }
        ] as AbstractPlayerRepository<ObjectId>

        handler.gameRepository = [
                findByPlayersIdAndGamePhaseAndLastUpdateGreaterThan: {
                    ObjectId id, GamePhase gp, ZonedDateTime dt, PageRequest pr ->
                        return queryResults[gp]
                }
        ] as GameRepository
        handler.gameMasker = [
                maskGameForPlayer: {
                    TWSGame game, MongoPlayer player ->
                        assert player.is(PONE)
                        return maskResults[game]
                }
        ] as MultiPlayerGameMasker

        assert handler.findGames(PONE.id) as Set == [masked3, masked2, masked1] as Set
    }
}
