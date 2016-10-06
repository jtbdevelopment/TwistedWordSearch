package com.jtbdevelopment.TwistedWordSearch.player

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.publish.PlayerPublisher
import com.jtbdevelopment.games.state.GamePhase
import org.bson.types.ObjectId

/**
 * Date: 10/5/16
 * Time: 6:54 PM
 */
class GameEndPlayerUpdaterTest extends MongoGameCoreTestCase {
    GameEndPlayerUpdater updater = new GameEndPlayerUpdater()

    MongoPlayer p1, p2, p3, p1loaded, p2loaded, p3loaded, p1saved, p2saved, p3saved;
    TWSPlayerAttributes p1a, p2a, p3a

    @Override
    void setUp() {
        p1 = makeSimplePlayer("100")
        p2 = makeSimplePlayer("101")
        p3 = makeSimplePlayer("103")
        p1loaded = makeSimplePlayer("100")
        p2loaded = makeSimplePlayer("101")
        p3loaded = makeSimplePlayer("103")
        p1saved = makeSimplePlayer("100")
        p2saved = makeSimplePlayer("101")
        p3saved = makeSimplePlayer("103")
        p1a = new TWSPlayerAttributes(
                gamesWonByPlayerCount: [(3): 10],
                maxScoreByPlayerCount: [(3): 33],
                gamesPlayedByPlayerCount: [(3): 24]
        )
        p1loaded.gameSpecificPlayerAttributes = p1a
        p2a = new TWSPlayerAttributes(
                gamesPlayedByPlayerCount: [(1): 24],
                gamesWonByPlayerCount: [(1): 24],
                maxScoreByPlayerCount: [(1): 90]
        )
        p2loaded.gameSpecificPlayerAttributes = p2a
        p3a = new TWSPlayerAttributes(
                gamesWonByPlayerCount: [(3): 5, (2): 20],
                maxScoreByPlayerCount: [(3): 18, (2): 130],
                gamesPlayedByPlayerCount: [(3): 100, (2): 40]
        )
        p3loaded.gameSpecificPlayerAttributes = p3a
    }


    void testUpdatesPlayerForGameEnd() {
        TWSGame game = new TWSGame()
        game.gamePhase = GamePhase.RoundOver
        game.players = [p1, p2, p3]
        game.winners = [p1.id, p3.id]
        game.scores = [(p1.id): 30, (p2.id): 10, (p3.id): 30]

        updater.playerRepository = [
                findOne: {
                    ObjectId id ->
                        switch (id) {
                            case p1.id:
                                return p1loaded
                            case p2.id:
                                return p2loaded
                            case p3.id:
                                return p3loaded
                        }
                },
                save   : {
                    Player p ->
                        if (p.is(p1loaded))
                            return p1saved
                        if (p.is(p2loaded))
                            return p2saved
                        if (p.is(p3loaded))
                            return p3saved
                }
        ] as AbstractPlayerRepository

        def published = [] as Set;
        updater.playerPublisher = [
                publish: {
                    Player p ->
                        published.add(p)
                }
        ] as PlayerPublisher

        updater.gameChanged(game, null, true)

        assert [(3): 11] == p1a.gamesWonByPlayerCount
        assert [(3): 25] == p1a.gamesPlayedByPlayerCount
        assert [(3): 33] == p1a.maxScoreByPlayerCount

        assert [(3): 0, (1): 24] == p2a.gamesWonByPlayerCount
        assert [(3): 1, (1): 24] == p2a.gamesPlayedByPlayerCount
        assert [(3): 10, (1): 90] == p2a.maxScoreByPlayerCount

        assert [(3): 6, (2): 20] == p3a.gamesWonByPlayerCount
        assert [(3): 101, (2): 40] == p3a.gamesPlayedByPlayerCount
        assert [(3): 30, (2): 130] == p3a.maxScoreByPlayerCount

        assert [p1saved, p2saved, p3saved] as Set == published
    }

    void testDoesNothingIfNotInitiatingServer() {
        TWSGame game = new TWSGame()
        game.gamePhase = GamePhase.RoundOver
        game.players = [p1, p2, p3]
        game.winners = [p1.id, p3.id]
        game.scores = [(p1.id): 30, (p2.id): 10, (p3.id): 30]

        updater.playerRepository = null

        updater.playerPublisher = null

        updater.gameChanged(game, null, false)

        assert [(3): 10] == p1a.gamesWonByPlayerCount
        assert [(3): 24] == p1a.gamesPlayedByPlayerCount
        assert [(3): 33] == p1a.maxScoreByPlayerCount

        assert [(1): 24] == p2a.gamesWonByPlayerCount
        assert [(1): 24] == p2a.gamesPlayedByPlayerCount
        assert [(1): 90] == p2a.maxScoreByPlayerCount

        assert [(3): 5, (2): 20] == p3a.gamesWonByPlayerCount
        assert [(3): 100, (2): 40] == p3a.gamesPlayedByPlayerCount
        assert [(3): 18, (2): 130] == p3a.maxScoreByPlayerCount
    }

    void testDoesNothingIfNotPhaseNotRoundOver() {
        GamePhase.values().findAll { it != GamePhase.RoundOver }.each {
            TWSGame game = new TWSGame()
            game.gamePhase = it
            game.players = [p1, p2, p3]
            game.winners = [p1.id, p3.id]
            game.scores = [(p1.id): 30, (p2.id): 10, (p3.id): 30]

            updater.playerRepository = null

            updater.playerPublisher = null

            updater.gameChanged(game, null, false)

            assert [(3): 10] == p1a.gamesWonByPlayerCount
            assert [(3): 24] == p1a.gamesPlayedByPlayerCount
            assert [(3): 33] == p1a.maxScoreByPlayerCount

            assert [(1): 24] == p2a.gamesWonByPlayerCount
            assert [(1): 24] == p2a.gamesPlayedByPlayerCount
            assert [(1): 90] == p2a.maxScoreByPlayerCount

            assert [(3): 5, (2): 20] == p3a.gamesWonByPlayerCount
            assert [(3): 100, (2): 40] == p3a.gamesPlayedByPlayerCount
            assert [(3): 18, (2): 130] == p3a.maxScoreByPlayerCount
        }
    }
}
