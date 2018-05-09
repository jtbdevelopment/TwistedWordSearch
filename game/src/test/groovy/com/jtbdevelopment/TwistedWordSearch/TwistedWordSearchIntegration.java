package com.jtbdevelopment.TwistedWordSearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.jtbdevelopment.TwistedWordSearch.dao.GameRepository;
import com.jtbdevelopment.TwistedWordSearch.rest.data.FeaturesAndPlayers;
import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo;
import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo.Detail;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.dao.AbstractGameRepository;
import com.jtbdevelopment.games.dev.utilities.integrationtesting.AbstractGameIntegration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Date: 7/13/16 Time: 6:57 PM
 */
public class TwistedWordSearchIntegration extends AbstractGameIntegration<TWSGame, MaskedGame> {

  private static GameRepository gameRepository;

  @BeforeClass
  public static void setup() {
    gameRepository = context.getBean(GameRepository.class);
  }

  public Class<MaskedGame> returnedGameClass() {
    return MaskedGame.class;
  }

  public Class<TWSGame> internalGameClass() {
    return TWSGame.class;
  }

  public TWSGame newGame() {
    return new TWSGame();
  }

  public AbstractGameRepository gameRepository() {
    return gameRepository;
  }

  @Test
  public void testGetFeatures() {
    WebTarget client = AbstractGameIntegration.createAPITarget(TEST_PLAYER2);
    List<GameFeatureInfo> features = client.path("features")
        .request(
            MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<GameFeatureInfo>>() {
        });
    assertEquals(Arrays.asList(
        new GameFeatureInfo(GameFeature.Grid,
            Arrays.asList(new Detail(GameFeature.Grid20X20), new Detail(GameFeature.Grid30X30),
                new Detail(GameFeature.Grid40X40), new Detail(GameFeature.Grid50X50),
                new Detail(GameFeature.CircleX31), new Detail(GameFeature.CircleX41),
                new Detail(GameFeature.CircleX51), new Detail(GameFeature.PyramidX40),
                new Detail(GameFeature.PyramidX50), new Detail(GameFeature.Diamond30X30),
                new Detail(GameFeature.Diamond40X40), new Detail(GameFeature.Diamond50X50))),
        new GameFeatureInfo(GameFeature.WordDifficulty, Arrays
            .asList(new Detail(GameFeature.SimpleWords),
                new Detail(GameFeature.StandardWords),
                new Detail(GameFeature.HardWords))),
        new GameFeatureInfo(
            GameFeature.WordSpotting,
            Arrays.asList(new Detail(GameFeature.EasiestDifficulty),
                new Detail(GameFeature.StandardDifficulty),
                new Detail(GameFeature.HarderDifficulty),
                new Detail(GameFeature.FiendishDifficulty))),
        new GameFeatureInfo(GameFeature.WordWrap, Arrays
            .asList(new Detail(GameFeature.WordWrapNo),
                new Detail(GameFeature.WordWrapYes))),
        new GameFeatureInfo(GameFeature.FillDifficulty,
            Arrays.asList(new Detail(GameFeature.RandomFill), new Detail(GameFeature.SomeOverlap),
                new Detail(GameFeature.StrongOverlap), new Detail(GameFeature.WordChunks)))),
        features);
  }

  @Test
  public void testCreateNewGame() {
    WebTarget P3 = AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER3);
    FeaturesAndPlayers players = new FeaturesAndPlayers();
    players.setFeatures(
        new HashSet<>(Arrays.asList(GameFeature.Grid30X30, GameFeature.StandardWords,
            GameFeature.HarderDifficulty,
            GameFeature.StrongOverlap, GameFeature.WordWrapYes)));
    players.setPlayers(Collections.singletonList(TEST_PLAYER3.getMd5()));
    final MaskedGame game = newGame(P3, players);
    assertNotNull(game);
    Map<String, String> expectedPlayers = new HashMap<>();
    expectedPlayers.put(TEST_PLAYER3.getMd5(), TEST_PLAYER3.getDisplayName());
    assertEquals(expectedPlayers, game.getPlayers());
    assertNotNull(game.getGrid());
    assertTrue(0 < game.getWordsToFind().size());
    Map<String, Set> expectedFoundWords = new HashMap<>();
    expectedFoundWords.put(TEST_PLAYER3.getMd5(), new HashSet<>());
    assertEquals(expectedFoundWords, game.getWordsFoundByPlayer());
    char[][] grid = game.getGrid();
    for (int row = 0; row < grid.length; ++row) {
      for (int col = 0; col < grid[row].length; ++col) {
        char cell = game.getGrid()[row][col];
        assertTrue(cell >= 'A' && cell <= 'Z');
      }
    }
  }

  @Test
  public void testFindingAWord() {
    WebTarget P3 = AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER3);
    FeaturesAndPlayers players = new FeaturesAndPlayers();
    players.setFeatures(
        new HashSet<>(Arrays.asList(GameFeature.Grid30X30, GameFeature.HarderDifficulty,
            GameFeature.StrongOverlap, GameFeature.SimpleWords, GameFeature.WordWrapYes)));
    players.setPlayers(Collections.singletonList(TEST_PLAYER3.getMd5()));
    MaskedGame game = newGame(P3, players);
    TWSGame rawGame = gameRepository.findById(new ObjectId(game.getId())).get();
    rawGame.getGrid().setGridCell(0, 0, 'A');
    rawGame.getGrid().setGridCell(0, 1, 'T');
    rawGame.getGrid().setGridCell(10, 10, 'F');
    rawGame.getGrid().setGridCell(9, 10, 'U');
    rawGame.getGrid().setGridCell(8, 10, 'R');
    rawGame.getWordsToFind().add("AT");
    rawGame.getWordsToFind().add("FUR");
    gameRepository.save(rawGame);

    WebTarget P3G = AbstractGameIntegration
        .createGameTarget(AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER3), game);

    game = findWord(P3G, Arrays.asList(new GridCoordinate(0, 1), new GridCoordinate(0, -1)));
    assertNotNull(game);
    assertTrue(game.getWordsToFind().contains("FUR"));
    assertFalse(game.getWordsToFind().contains("AT"));
    Map<String, Set<String>> expectedFoundWords = new HashMap<>();
    expectedFoundWords.put(TEST_PLAYER3.getMd5(), new HashSet<>(Collections.singletonList("AT")));
    assertEquals(expectedFoundWords, game.getWordsFoundByPlayer());

    Map<String, Set<GridCoordinate>> expectedFoundLocations = new HashMap<>();
    expectedFoundLocations.put("AT",
        new HashSet<>(Arrays.asList(new GridCoordinate(0, 0), new GridCoordinate(0, 1))));
    assertEquals(expectedFoundLocations, game.getFoundWordLocations());

    Response response = P3G.path("find").request(MediaType.APPLICATION_JSON).put(Entity.entity(
        Arrays.asList(new GridCoordinate(0, 1), new GridCoordinate(0, -1)),
        MediaType.APPLICATION_JSON));
    assertNotNull(response);
    assertEquals(409, response.getStatusInfo().getStatusCode());
  }

  @Test
  public void testGettingAHint() {
    WebTarget P3 = AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER3);
    FeaturesAndPlayers players = new FeaturesAndPlayers();
    players.setFeatures(
        new HashSet<>(Arrays.asList(GameFeature.Grid30X30, GameFeature.HarderDifficulty,
            GameFeature.StrongOverlap, GameFeature.SimpleWords, GameFeature.WordWrapYes)));
    players.setPlayers(Collections.singletonList(TEST_PLAYER3.getMd5()));
    MaskedGame game = newGame(P3, players);
    assertTrue(game.getHintsRemaining() > 0);
    int originalRemaining = game.getHintsRemaining();
    TWSGame rawGame = gameRepository.findById(new ObjectId(game.getId())).get();
    assertTrue(rawGame.getHintsRemaining() > 0);
    assertTrue(game.getHints().isEmpty());

    WebTarget P3G = AbstractGameIntegration
        .createGameTarget(AbstractGameIntegration.createPlayerAPITarget(TEST_PLAYER3), game);

    game = getHint(P3G);
    assertNotNull(game);
    assertTrue(game.getScores().get(TEST_PLAYER3.getMd5()) < 0);
    assertEquals(originalRemaining - 1, game.getHintsRemaining());
    assertEquals(1, game.getHints().size());
  }

  @Override
  public void testGetMultiplayerGames() {
    //  TODO - base method needs work to be more useful
  }

  private MaskedGame newGame(WebTarget target, FeaturesAndPlayers featuresAndPlayers) {
    Entity<FeaturesAndPlayers> entity = Entity
        .entity(featuresAndPlayers, MediaType.APPLICATION_JSON);
    return target.path("new").request(MediaType.APPLICATION_JSON).post(entity, returnedGameClass());
  }

  private MaskedGame findWord(WebTarget target, List<GridCoordinate> coordinates) {
    Entity<List<GridCoordinate>> find = Entity.entity(coordinates, MediaType.APPLICATION_JSON);
    return target.path("find").request(MediaType.APPLICATION_JSON).put(find, returnedGameClass());
  }

  private MaskedGame getHint(WebTarget target) {
    Builder request = target.path("hint").request(MediaType.APPLICATION_JSON);
    request.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
    return request.put(Entity.entity(null, MediaType.APPLICATION_JSON), returnedGameClass());
  }
}
