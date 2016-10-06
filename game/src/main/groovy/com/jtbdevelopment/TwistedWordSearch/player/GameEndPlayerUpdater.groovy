package com.jtbdevelopment.TwistedWordSearch.player

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.games.dao.AbstractPlayerRepository
import com.jtbdevelopment.games.players.Player
import com.jtbdevelopment.games.publish.GameListener
import com.jtbdevelopment.games.publish.PlayerPublisher
import com.jtbdevelopment.games.state.GamePhase
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Date: 10/5/16
 * Time: 6:39 PM
 */
@Component
@CompileStatic
class GameEndPlayerUpdater implements GameListener<TWSGame> {
    @Autowired
    PlayerPublisher playerPublisher
    @Autowired
    AbstractPlayerRepository playerRepository

    void gameChanged(final TWSGame game, final Player initiatingPlayer, final boolean initiatingServer) {
        if (game.gamePhase == GamePhase.RoundOver && initiatingServer) {

            def playerCount = game.players.size()
            game.players.each {
                Player gamePlayer ->
                    Player p = playerRepository.findOne(gamePlayer.id)
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
