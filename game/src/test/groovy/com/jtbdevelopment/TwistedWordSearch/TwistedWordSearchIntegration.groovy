package com.jtbdevelopment.TwistedWordSearch

import com.jtbdevelopment.TwistedWordSearch.dao.GameRepository
import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers
import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.core.hazelcast.caching.HazelcastCacheManager
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration
import org.junit.BeforeClass
import org.junit.Test

import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType

/**
 * Date: 7/13/16
 * Time: 6:57 PM
 */
class TwistedWordSearchIntegration extends AbstractGameIntegration<MaskedGame> {
    Class<MaskedGame> returnedGameClass() {
        return MaskedGame.class
    }

    static HazelcastCacheManager cacheManager
    static GameRepository gameRepository

    @BeforeClass
    static void setup() {
        cacheManager = context.getBean(HazelcastCacheManager.class)
        gameRepository = context.getBean(GameRepository.class)
    }

    @Test
    void testGetFeatures() {
        def client = createAPITarget(TEST_PLAYER2)
        def features = client.path("features").request(MediaType.APPLICATION_JSON_TYPE).get(
                new GenericType<List<GameFeatureInfo>>() {
                })
        assert features == [
                new GameFeatureInfo(
                        GameFeature.Grid,
                        [
                                new GameFeatureInfo.Detail(GameFeature.Grid40X40),
                                new GameFeatureInfo.Detail(GameFeature.Grid20X20),
                                new GameFeatureInfo.Detail(GameFeature.Grid10X10),
                                new GameFeatureInfo.Detail(GameFeature.CircleX20),
                                new GameFeatureInfo.Detail(GameFeature.CircleX40),
                                new GameFeatureInfo.Detail(GameFeature.PyramidX20),
                                new GameFeatureInfo.Detail(GameFeature.PyramidX40),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.WordWrap,
                        [
                                new GameFeatureInfo.Detail(GameFeature.WordWrapYes),
                                new GameFeatureInfo.Detail(GameFeature.WordWrapNo),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.JumbleOnFind,
                        [
                                new GameFeatureInfo.Detail(GameFeature.JumbleOnFindNo),
                                new GameFeatureInfo.Detail(GameFeature.JumbleOnFindYes),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.AverageWordLength,
                        [
                                new GameFeatureInfo.Detail(GameFeature.AverageOf5),
                                new GameFeatureInfo.Detail(GameFeature.AverageOf4),
                                new GameFeatureInfo.Detail(GameFeature.AverageOf3),
                                new GameFeatureInfo.Detail(GameFeature.AverageOf6),
                                new GameFeatureInfo.Detail(GameFeature.AverageOf7),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.FillDifficulty,
                        [
                                new GameFeatureInfo.Detail(GameFeature.RandomFill),
                                new GameFeatureInfo.Detail(GameFeature.SomeOverlap),
                                new GameFeatureInfo.Detail(GameFeature.StrongOverlap),
                                new GameFeatureInfo.Detail(GameFeature.WordChunks)
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.HideWordLetters,
                        [
                                new GameFeatureInfo.Detail(GameFeature.HideWordLettersNone),
                                new GameFeatureInfo.Detail(GameFeature.HideWordLettersSome),
                                new GameFeatureInfo.Detail(GameFeature.HideWordLettersMany),
                        ]
                ),
        ]
    }

    //  TODO - doesn't do much yet
    @Test
    void testCreateNewGame() {
        def P3 = createPlayerAPITarget(TEST_PLAYER3)
        MaskedGame game = newGame(P3,
                new FeaturesAndPlayers(
                        features: [
                                GameFeature.Grid20X20,
                                GameFeature.HideWordLettersNone,
                                GameFeature.JumbleOnFindNo,
                                GameFeature.AverageOf6,
                                GameFeature.StrongOverlap,
                                GameFeature.WordWrapYes,
                        ] as Set,
                        players: [TEST_PLAYER3.md5],
                ))
        assert game
    }

    protected MaskedGame newGame(WebTarget target, FeaturesAndPlayers featuresAndPlayers) {
        def entity = Entity.entity(
                featuresAndPlayers,
                MediaType.APPLICATION_JSON)
        target.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, returnedGameClass())
    }

}