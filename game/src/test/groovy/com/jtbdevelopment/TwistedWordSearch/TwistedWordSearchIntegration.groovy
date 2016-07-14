package com.jtbdevelopment.TwistedWordSearch

import com.jtbdevelopment.TwistedWordSearch.dao.GameRepository
import com.jtbdevelopment.TwistedWordSearch.state.masking.TWSMaskedGame
import com.jtbdevelopment.core.hazelcast.caching.HazelcastCacheManager
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration
import org.junit.BeforeClass

/**
 * Date: 7/13/16
 * Time: 6:57 PM
 */
class TwistedWordSearchIntegration extends AbstractGameIntegration<TWSMaskedGame> {
    Class<TWSMaskedGame> returnedGameClass() {
        return TWSMaskedGame.class
    }

    static HazelcastCacheManager cacheManager
    static GameRepository gameRepository

    @BeforeClass
    static void setup() {
        cacheManager = context.getBean(HazelcastCacheManager.class)
        gameRepository = context.getBean(GameRepository.class)
    }

}
