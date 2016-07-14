package com.jtbdevelopment.TwistedWordSearch.state
/**
 * Date: 7/13/16
 * Time: 6:41 PM
 */
class TWSGameFeatureTest extends GroovyTestCase {
    void testGetGroupedFeatures() {
        assert [
                (TWSGameFeature.Grid)             : [TWSGameFeature.Grid40X40, TWSGameFeature.Grid20X20, TWSGameFeature.CircleX20, TWSGameFeature.CircleX40, TWSGameFeature.PyramidX20, TWSGameFeature.PyramidX40],
                (TWSGameFeature.AverageWordLength): [],
                (TWSGameFeature.WordWrap)         : [],
                (TWSGameFeature.FillDifficulty)   : [TWSGameFeature.RandomFill, TWSGameFeature.SomeOverlap, TWSGameFeature.StrongOverlap, TWSGameFeature.WordChunks],
                (TWSGameFeature.JumbleOnFind)     : [],
        ] == TWSGameFeature.groupedFeatures
    }

    void testGetGroupTypeValueTypes() {
        assert [
                (TWSGameFeature.Grid)             : TWSGameFeature.class,
                (TWSGameFeature.AverageWordLength): Integer.class,
                (TWSGameFeature.WordWrap)         : Boolean.class,
                (TWSGameFeature.FillDifficulty)   : TWSGameFeature.class,
                (TWSGameFeature.JumbleOnFind)     : Boolean.class,
        ] == TWSGameFeature.groupedFeatures.collectEntries {
            [(it.key): it.key.valueType]
        }
    }

    void testGetGroupTypeDefaultValues() {
        assert [
                (TWSGameFeature.Grid)             : null,
                (TWSGameFeature.AverageWordLength): 5,
                (TWSGameFeature.WordWrap)         : Boolean.TRUE,
                (TWSGameFeature.FillDifficulty)   : null,
                (TWSGameFeature.JumbleOnFind)     : Boolean.FALSE,
        ] == TWSGameFeature.groupedFeatures.collectEntries {
            [(it.key): it.key.groupDefault]
        }
    }
}
