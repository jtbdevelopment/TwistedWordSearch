package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature

/**
 * Date: 7/18/16
 * Time: 4:14 PM
 */
class TWSPlayerGatewayServiceTest extends GroovyTestCase {
    TWSPlayerGatewayService service = new TWSPlayerGatewayService()

    void testFeaturesAndDescriptions() {
        assert [
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
                        []
                ),
                new GameFeatureInfo(
                        GameFeature.JumbleOnFind,
                        []
                ),
                new GameFeatureInfo(
                        GameFeature.AverageWordLength,
                        []
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
                        []
                ),
        ] == service.featuresAndDescriptions()
    }
}
