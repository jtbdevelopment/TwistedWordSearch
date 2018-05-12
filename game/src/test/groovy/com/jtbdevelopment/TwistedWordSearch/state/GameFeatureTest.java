package com.jtbdevelopment.TwistedWordSearch.state;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

/**
 * Date: 7/13/16 Time: 6:41 PM
 */
public class GameFeatureTest {

  @Test
  public void testGetGroupedFeatures() {
    Map<GameFeature, List<GameFeature>> features = GameFeature.getGroupedFeatures();
    Map<GameFeature, List<GameFeature>> map = new HashMap<>();
    map.put(GameFeature.Grid, Arrays.asList(
        GameFeature.Grid20X20,
        GameFeature.Grid30X30,
        GameFeature.Grid40X40,
        GameFeature.Grid50X50,
        GameFeature.CircleX31,
        GameFeature.CircleX41,
        GameFeature.CircleX51,
        GameFeature.PyramidX40,
        GameFeature.PyramidX50,
        GameFeature.Diamond30X30,
        GameFeature.Diamond40X40,
        GameFeature.Diamond50X50));
    map.put(GameFeature.WordDifficulty, Arrays.asList(
        GameFeature.SimpleWords,
        GameFeature.StandardWords,
        GameFeature.HardWords));
    map.put(GameFeature.WordSpotting, Arrays.asList(
        GameFeature.EasiestDifficulty,
        GameFeature.StandardDifficulty,
        GameFeature.HarderDifficulty,
        GameFeature.FiendishDifficulty));
    map.put(GameFeature.WordWrap, Arrays.asList(
        GameFeature.WordWrapNo,
        GameFeature.WordWrapYes));
    map.put(GameFeature.FillDifficulty, Arrays.asList(
        GameFeature.RandomFill,
        GameFeature.SomeOverlap,
        GameFeature.StrongOverlap,
        GameFeature.WordChunks));
    Assert.assertEquals(map, features);
  }

}
