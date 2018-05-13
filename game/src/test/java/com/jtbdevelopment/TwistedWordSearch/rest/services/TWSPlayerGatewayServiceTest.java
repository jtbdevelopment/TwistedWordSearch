package com.jtbdevelopment.TwistedWordSearch.rest.services;

import static org.junit.Assert.assertEquals;

import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo;
import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo.Detail;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import java.util.Arrays;
import org.junit.Test;

/**
 * Date: 7/18/16 Time: 4:14 PM
 */
public class TWSPlayerGatewayServiceTest {

  private TWSPlayerGatewayService service = new TWSPlayerGatewayService(null);

  @Test
  public void testFeaturesAndDescriptions() {
    assertEquals(
        Arrays.asList(
            new GameFeatureInfo(
                GameFeature.Grid,
                Arrays.asList(
                    new Detail(GameFeature.Grid20X20),
                    new Detail(GameFeature.Grid30X30),
                    new Detail(GameFeature.Grid40X40),
                    new Detail(GameFeature.Grid50X50),
                    new Detail(GameFeature.CircleX31),
                    new Detail(GameFeature.CircleX41),
                    new Detail(GameFeature.CircleX51),
                    new Detail(GameFeature.PyramidX40),
                    new Detail(GameFeature.PyramidX50),
                    new Detail(GameFeature.Diamond30X30),
                    new Detail(GameFeature.Diamond40X40),
                    new Detail(GameFeature.Diamond50X50))),
            new GameFeatureInfo(
                GameFeature.WordDifficulty,
                Arrays.asList(
                    new Detail(GameFeature.SimpleWords),
                    new Detail(GameFeature.StandardWords),
                    new Detail(GameFeature.HardWords))),
            new GameFeatureInfo(
                GameFeature.WordSpotting,
                Arrays.asList(
                    new Detail(GameFeature.EasiestDifficulty),
                    new Detail(GameFeature.StandardDifficulty),
                    new Detail(GameFeature.HarderDifficulty),
                    new Detail(GameFeature.FiendishDifficulty))),
            new GameFeatureInfo(
                GameFeature.WordWrap,
                Arrays.asList(
                    new Detail(GameFeature.WordWrapNo),
                    new Detail(GameFeature.WordWrapYes))),
            new GameFeatureInfo(
                GameFeature.FillDifficulty,
                Arrays.asList(
                    new Detail(GameFeature.RandomFill),
                    new Detail(GameFeature.SomeOverlap),
                    new Detail(GameFeature.StrongOverlap),
                    new Detail(GameFeature.WordChunks)))),
        service.featuresAndDescriptions());
  }
}
