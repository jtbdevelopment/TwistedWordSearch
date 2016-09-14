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
                /*
                new GameFeatureInfo(
                        GameFeature.JumbleOnFind,
                        [
                                new GameFeatureInfo.Detail(GameFeature.JumbleOnFindNo),
                                new GameFeatureInfo.Detail(GameFeature.JumbleOnFindYes),
                        ]
                ),
                */
                new GameFeatureInfo(
                        GameFeature.WordDifficulty,
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
                /*
                new GameFeatureInfo(
                        GameFeature.HideWordLetters,
                        [
                                new GameFeatureInfo.Detail(GameFeature.HideWordLettersNone),
                                new GameFeatureInfo.Detail(GameFeature.HideWordLettersSome),
                                new GameFeatureInfo.Detail(GameFeature.HideWordLettersMany),
                        ]
                ),
                */
        ] == service.featuresAndDescriptions()
    }
}
