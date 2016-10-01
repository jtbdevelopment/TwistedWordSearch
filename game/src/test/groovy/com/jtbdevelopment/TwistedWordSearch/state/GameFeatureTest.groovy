package com.jtbdevelopment.TwistedWordSearch.state
/**
 * Date: 7/13/16
 * Time: 6:41 PM
 */
class GameFeatureTest extends GroovyTestCase {
    void testGetGroupedFeatures() {
        assert [
                (GameFeature.Grid)          : [
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
                        GameFeature.Diamond50X50],
                (GameFeature.WordDifficulty): [GameFeature.SimpleWords, GameFeature.StandardWords, GameFeature.HardWords],
                (GameFeature.WordSpotting)  : [GameFeature.EasiestDifficulty, GameFeature.StandardDifficulty, GameFeature.HarderDifficulty, GameFeature.FiendishDifficulty],
                (GameFeature.WordWrap)      : [GameFeature.WordWrapNo, GameFeature.WordWrapYes],
                (GameFeature.FillDifficulty): [GameFeature.RandomFill, GameFeature.SomeOverlap, GameFeature.StrongOverlap, GameFeature.WordChunks],
        ] == GameFeature.groupedFeatures
    }
}
