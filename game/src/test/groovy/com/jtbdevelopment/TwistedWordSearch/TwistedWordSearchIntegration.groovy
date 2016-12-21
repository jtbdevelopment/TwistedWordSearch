package com.jtbdevelopment.TwistedWordSearch

import com.jtbdevelopment.TwistedWordSearch.dao.GameRepository
import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers
import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.core.hazelcast.caching.HazelcastCacheManager
import com.jtbdevelopment.games.dao.AbstractGameRepository
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration
import org.bson.types.ObjectId
import org.glassfish.jersey.client.ClientProperties
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
class TwistedWordSearchIntegration extends AbstractGameIntegration<TWSGame, MaskedGame> {
    Class<MaskedGame> returnedGameClass() {
        return MaskedGame.class
    }

    Class<TWSGame> internalGameClass() {
        return TWSGame.class
    }

    TWSGame newGame() {
        return new TWSGame()
    }

    AbstractGameRepository gameRepository() {
        return gameRepository
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
                                new GameFeatureInfo.Detail(GameFeature.Grid20X20),
                                new GameFeatureInfo.Detail(GameFeature.Grid30X30),
                                new GameFeatureInfo.Detail(GameFeature.Grid40X40),
                                new GameFeatureInfo.Detail(GameFeature.Grid50X50),
                                new GameFeatureInfo.Detail(GameFeature.CircleX31),
                                new GameFeatureInfo.Detail(GameFeature.CircleX41),
                                new GameFeatureInfo.Detail(GameFeature.CircleX51),
                                new GameFeatureInfo.Detail(GameFeature.PyramidX40),
                                new GameFeatureInfo.Detail(GameFeature.PyramidX50),
                                new GameFeatureInfo.Detail(GameFeature.Diamond30X30),
                                new GameFeatureInfo.Detail(GameFeature.Diamond40X40),
                                new GameFeatureInfo.Detail(GameFeature.Diamond50X50),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.WordDifficulty,
                        [
                                new GameFeatureInfo.Detail(GameFeature.SimpleWords),
                                new GameFeatureInfo.Detail(GameFeature.StandardWords),
                                new GameFeatureInfo.Detail(GameFeature.HardWords),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.WordSpotting,
                        [
                                new GameFeatureInfo.Detail(GameFeature.EasiestDifficulty),
                                new GameFeatureInfo.Detail(GameFeature.StandardDifficulty),
                                new GameFeatureInfo.Detail(GameFeature.HarderDifficulty),
                                new GameFeatureInfo.Detail(GameFeature.FiendishDifficulty),
                        ]
                ),
                new GameFeatureInfo(
                        GameFeature.WordWrap,
                        [
                                new GameFeatureInfo.Detail(GameFeature.WordWrapNo),
                                new GameFeatureInfo.Detail(GameFeature.WordWrapYes),
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
        ]
    }

    @Test
    void testCreateNewGame() {
        def P3 = createPlayerAPITarget(TEST_PLAYER3)
        MaskedGame game = newGame(P3,
                new FeaturesAndPlayers(
                        features: [
                                GameFeature.Grid30X30,
                                GameFeature.StandardWords,
                                GameFeature.HarderDifficulty,
                                GameFeature.StrongOverlap,
                                GameFeature.WordWrapYes,
                        ] as Set,
                        players: [TEST_PLAYER3.md5],
                ))
        assert game
        assert [(TEST_PLAYER3.md5): TEST_PLAYER3.displayName] == game.players
        assert null != game.grid
        assert 0 < game.wordsToFind.size()
        assert [(TEST_PLAYER3.md5): [] as Set] == game.wordsFoundByPlayer
        (1..game.grid.length).each {
            int row ->
                (1..game.grid.length).each {
                    int col ->
                        def cell = game.grid[row - 1][col - 1]
                        assert cell >= ('A' as char) && cell <= ('Z' as char)
                }
        }
    }

    @Test
    void testFindingAWord() {
        def P3 = createPlayerAPITarget(TEST_PLAYER3)
        MaskedGame game = newGame(P3,
                new FeaturesAndPlayers(
                        features: [
                                GameFeature.Grid30X30,
                                GameFeature.HarderDifficulty,
                                GameFeature.StrongOverlap,
                                GameFeature.SimpleWords,
                                GameFeature.WordWrapYes,
                        ] as Set,
                        players: [TEST_PLAYER3.md5],
                ))
        TWSGame rawGame = gameRepository.findOne(new ObjectId(game.id))
        rawGame.grid.setGridCell(0, 0, 'A' as char)
        rawGame.grid.setGridCell(0, 1, 'T' as char)
        rawGame.grid.setGridCell(10, 10, 'F' as char)
        rawGame.grid.setGridCell(9, 10, 'U' as char)
        rawGame.grid.setGridCell(8, 10, 'R' as char)
        rawGame.wordsToFind.add('AT')
        rawGame.wordsToFind.add('FUR')
        gameRepository.save(rawGame)

        def P3G = createGameTarget(createPlayerAPITarget(TEST_PLAYER3), game)

        game = findWord(P3G, [new GridCoordinate(0, 1), new GridCoordinate(0, -1)])
        assert game != null
        assert game.wordsToFind.contains('FUR')
        assert !game.wordsToFind.contains('AT')
        assert [(TEST_PLAYER3.md5): ['AT'] as Set] == game.wordsFoundByPlayer
        assert ['AT': [new GridCoordinate(0, 0), new GridCoordinate(0, 1)] as Set] == game.foundWordLocations

        def response = P3G.path('find').request(MediaType.APPLICATION_JSON).put(Entity.entity([new GridCoordinate(0, 1), new GridCoordinate(0, -1)], MediaType.APPLICATION_JSON))
        assert response != null
        assert response.statusInfo.statusCode == 409
    }

    @Test
    void testGettingAHint() {
        def P3 = createPlayerAPITarget(TEST_PLAYER3)
        MaskedGame game = newGame(P3,
                new FeaturesAndPlayers(
                        features: [
                                GameFeature.Grid30X30,
                                GameFeature.HarderDifficulty,
                                GameFeature.StrongOverlap,
                                GameFeature.SimpleWords,
                                GameFeature.WordWrapYes,
                        ] as Set,
                        players: [TEST_PLAYER3.md5],
                ))
        assert game.hintsRemaining > 0
        def originalRemaining = game.hintsRemaining
        TWSGame rawGame = gameRepository.findOne(new ObjectId(game.id))
        assert rawGame.hintsRemaining > 0
        assert [] as Set == game.hints

        def P3G = createGameTarget(createPlayerAPITarget(TEST_PLAYER3), game)

        game = getHint(P3G)
        assert game != null
        assert game.scores[TEST_PLAYER3.md5] < 0
        assert originalRemaining - 1 == game.hintsRemaining
        assert [] as Set != game.hints
        assert 1 == game.hints.size()
    }

    @Override
    void testGetMultiplayerGames() {
        //  TODO - base method needs work to be more useful
    }

    protected MaskedGame newGame(WebTarget target, FeaturesAndPlayers featuresAndPlayers) {
        def entity = Entity.entity(
                featuresAndPlayers,
                MediaType.APPLICATION_JSON)
        target.path("new")
                .request(MediaType.APPLICATION_JSON)
                .post(entity, returnedGameClass())
    }

    protected MaskedGame findWord(WebTarget target, List<GridCoordinate> coordinates) {
        def find = Entity.entity(coordinates, MediaType.APPLICATION_JSON)
        target.path("find").request(MediaType.APPLICATION_JSON).put(find, returnedGameClass())
    }

    protected MaskedGame getHint(WebTarget target) {
        def request = target.path("hint").request(MediaType.APPLICATION_JSON)
        request.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true)
        request.put(Entity.entity(null, MediaType.APPLICATION_JSON), returnedGameClass())
    }
}

