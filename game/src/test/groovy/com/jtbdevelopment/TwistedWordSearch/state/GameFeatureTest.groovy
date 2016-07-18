package com.jtbdevelopment.TwistedWordSearch.state
/**
 * Date: 7/13/16
 * Time: 6:41 PM
 */
class GameFeatureTest extends GroovyTestCase {
    void testGetGroupedFeatures() {
        assert [
                (GameFeature.Grid)             : [GameFeature.Grid40X40, GameFeature.Grid20X20, GameFeature.Grid10X10, GameFeature.CircleX20, GameFeature.CircleX40, GameFeature.PyramidX20, GameFeature.PyramidX40],
                (GameFeature.AverageWordLength): [],
                (GameFeature.WordWrap)         : [],
                (GameFeature.FillDifficulty)   : [GameFeature.RandomFill, GameFeature.SomeOverlap, GameFeature.StrongOverlap, GameFeature.WordChunks],
                (GameFeature.JumbleOnFind)     : [],
                (GameFeature.HideWordLetters)  : [],
        ] == GameFeature.groupedFeatures
    }

    void testGetGroupTypeValueTypes() {
        assert [
                (GameFeature.Grid)             : GameFeature.class,
                (GameFeature.AverageWordLength): Integer.class,
                (GameFeature.WordWrap)         : Boolean.class,
                (GameFeature.FillDifficulty)   : GameFeature.class,
                (GameFeature.JumbleOnFind)     : Boolean.class,
                (GameFeature.HideWordLetters)  : Boolean.class,
        ] == GameFeature.groupedFeatures.collectEntries {
            [(it.key): it.key.groupDefaultValueType]
        }
    }

    void testGetGroupTypeDefaultValues() {
        assert [
                (GameFeature.Grid)             : null,
                (GameFeature.AverageWordLength): 5,
                (GameFeature.WordWrap)         : Boolean.TRUE,
                (GameFeature.FillDifficulty)   : null,
                (GameFeature.JumbleOnFind)     : Boolean.FALSE,
                (GameFeature.HideWordLetters)  : Boolean.FALSE,
        ] == GameFeature.groupedFeatures.collectEntries {
            [(it.key): it.key.groupDefault]
        }
    }
}
