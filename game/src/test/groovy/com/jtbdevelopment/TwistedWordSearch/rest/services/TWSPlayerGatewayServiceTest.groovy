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
                                new GameFeatureInfo.Detail(GameFeature.Grid30X30),
                                new GameFeatureInfo.Detail(GameFeature.Grid40X40),
                                new GameFeatureInfo.Detail(GameFeature.Grid50X50),
                                new GameFeatureInfo.Detail(GameFeature.CircleX40),
                                new GameFeatureInfo.Detail(GameFeature.CircleX50),
                                new GameFeatureInfo.Detail(GameFeature.PyramidX40),
                                new GameFeatureInfo.Detail(GameFeature.PyramidX50),
                                new GameFeatureInfo.Detail(GameFeature.Diamond40x40),
                                new GameFeatureInfo.Detail(GameFeature.Diamond50x50),
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
        ] == service.featuresAndDescriptions()
    }
}
