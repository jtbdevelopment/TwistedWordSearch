package com.jtbdevelopment.TwistedWordSearch.player

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.exceptions.input.PlayerNotPartOfGameException
import com.jtbdevelopment.games.mongo.players.MongoPlayer
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.publish.GameListener
import com.jtbdevelopment.games.publish.PlayerPublisher
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.stereotype.Component

import java.util.function.Supplier

/**
 * Date: 10/5/16
 * Time: 6:39 PM
 */
@Component
@CompileStatic
class GameEndPlayerUpdater implements GameListener<TWSGame, MongoPlayer> {
    private final PlayerPublisher playerPublisher
    private final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository

    GameEndPlayerUpdater(
            final PlayerPublisher playerPublisher,
            final AbstractPlayerRepository<ObjectId, MongoPlayer> playerRepository) {
        this.playerPublisher = playerPublisher
        this.playerRepository = playerRepository
    }

    void gameChanged(final TWSGame game, final MongoPlayer initiatingPlayer, final boolean initiatingServer) {
        if (game.gamePhase == GamePhase.RoundOver && initiatingServer) {

            def playerCount = game.players.size()
            game.players.each {
                Player<ObjectId> gamePlayer ->
                    Optional<MongoPlayer> optional = playerRepository.findById(gamePlayer.id)
                    MongoPlayer p = optional.orElseThrow(new Supplier() {
                        @Override
                        Object get() {
                            return new PlayerNotPartOfGameException()
                        }
                    })
                    TWSPlayerAttributes twsPlayerAttributes = (TWSPlayerAttributes) p.gameSpecificPlayerAttributes
                    if (!twsPlayerAttributes.gamesPlayedByPlayerCount.containsKey(playerCount)) {
                        twsPlayerAttributes.gamesPlayedByPlayerCount[playerCount] = 0
                        twsPlayerAttributes.gamesWonByPlayerCount[playerCount] = 0
                        twsPlayerAttributes.maxScoreByPlayerCount[playerCount] = 0
                    }

                    twsPlayerAttributes.gamesPlayedByPlayerCount[playerCount] += 1

                    int score = game.scores[(ObjectId) p.id]
                    if (score > twsPlayerAttributes.maxScoreByPlayerCount[playerCount]) {
                        twsPlayerAttributes.maxScoreByPlayerCount[playerCount] = score
                    }

                    if (game.winners.contains(gamePlayer.id)) {
                        twsPlayerAttributes.gamesWonByPlayerCount[playerCount] += 1
                    }

                    def saved = playerRepository.save(p)
                    playerPublisher.publish(saved)
            }
        }
    }
}
