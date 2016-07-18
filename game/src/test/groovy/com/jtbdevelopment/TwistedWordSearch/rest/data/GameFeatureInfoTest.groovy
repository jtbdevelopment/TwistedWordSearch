package com.jtbdevelopment.TwistedWordSearch.rest.data

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature

/**
 * Date: 7/18/16
 * Time: 4:26 PM
 */
class GameFeatureInfoTest extends GroovyTestCase {
    GameFeatureInfo test = new GameFeatureInfo(GameFeature.Grid,
            [
                    new GameFeatureInfo.Detail(GameFeature.Grid40X40),
                    new GameFeatureInfo.Detail(GameFeature.SomeOverlap)
            ]
    )

    void testEquals() {
        assert GameFeature.Grid.hashCode() == test.hashCode()
    }

    void testHashCode() {
        assertFalse new GameFeatureInfo(GameFeature.Grid, []) == test
        assertFalse new GameFeatureInfo(GameFeature.FillDifficulty, []) == test
        assert new GameFeatureInfo(GameFeature.Grid,
                [
                        new GameFeatureInfo.Detail(GameFeature.Grid40X40),
                        new GameFeatureInfo.Detail(GameFeature.SomeOverlap)
                ]
        ) == test
    }

    void testGetFeature() {
        GameFeatureInfo.Detail detail = test.feature
        assert GameFeature.Grid.description == detail.description
        assert GameFeature.Grid.groupType == detail.groupType
        assert GameFeature.Grid.groupDefault == detail.groupDefault
        assert GameFeature.Grid.groupDefaultValueType == detail.groupDefaultValueType
        assert GameFeature.Grid.label == detail.label
        assert GameFeature.Grid == detail.feature
    }

    void testGetOptions() {
        assert [
                new GameFeatureInfo.Detail(GameFeature.Grid40X40),
                new GameFeatureInfo.Detail(GameFeature.SomeOverlap)
        ] == test.options
    }

    void testGetOptionDetailOfOption() {
        GameFeatureInfo.Detail detail = test.options[0]
        assert GameFeature.Grid40X40 == detail.feature
        assert GameFeature.Grid40X40.description == detail.description
        assert GameFeature.Grid40X40.groupType == detail.groupType
        assert GameFeature.Grid40X40.groupDefaultValueType == detail.groupDefaultValueType
        assert GameFeature.Grid40X40.groupType == detail.groupType
        assert GameFeature.Grid40X40.label == detail.label
    }

    void testGetDetailHashCode() {
        assert GameFeature.Grid40X40.hashCode() == test.options[0].hashCode()
        assert GameFeature.SomeOverlap.hashCode() == test.options[1].hashCode()
    }

    void testGetDetailEquals() {
        assert new GameFeatureInfo.Detail(GameFeature.Grid40X40) == test.options[0]
        assert new GameFeatureInfo.Detail(GameFeature.SomeOverlap) == test.options[1]
        assertFalse new GameFeatureInfo.Detail(GameFeature.Grid40X40) == test.options[1]
        assertFalse new GameFeatureInfo.Detail(GameFeature.SomeOverlap) == test.options[0]
    }
}
