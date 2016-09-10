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
                        GameFeature.CircleX30,
                        GameFeature.CircleX40,
                        GameFeature.CircleX50,
                        GameFeature.PyramidX40,
                        GameFeature.PyramidX50,
                        GameFeature.Diamond30x30,
                        GameFeature.Diamond40x40,
                        GameFeature.Diamond50x50],
                (GameFeature.WordDifficulty): [GameFeature.BeginnerDifficulty, GameFeature.ExperiencedDifficulty, GameFeature.ExpertDifficulty, GameFeature.ProfessionalDifficulty],
                (GameFeature.WordWrap)      : [GameFeature.WordWrapNo, GameFeature.WordWrapYes],
                (GameFeature.FillDifficulty): [GameFeature.RandomFill, GameFeature.SomeOverlap, GameFeature.StrongOverlap, GameFeature.WordChunks],
                //(GameFeature.JumbleOnFind)  : [GameFeature.JumbleOnFindNo, GameFeature.JumbleOnFindYes],
                //(GameFeature.HideWordLetters): [GameFeature.HideWordLettersNone, GameFeature.HideWordLettersSome, GameFeature.HideWordLettersMany],
        ] == GameFeature.groupedFeatures
    }
}
