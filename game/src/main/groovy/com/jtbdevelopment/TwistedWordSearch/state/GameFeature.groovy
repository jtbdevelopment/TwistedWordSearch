package com.jtbdevelopment.TwistedWordSearch.state

import groovy.transform.CompileStatic

/**
 * Date: 7/11/16
 * Time: 6:57 PM
 */
@CompileStatic
enum GameFeature {
    //  TODO - more - random jumble on timer
    Grid(1, GameFeatureGroupType.Difficulty, 'Grid', 'Type of grid to play on.'),
    Grid40X40(1, '40x40', '40 x 40 square grid.', Grid),
    Grid20X20(2, '20x20', '20 x 20 square grid.', Grid),
    Grid10X10(3, '10x10', '10 x 10 square grid.', Grid),
    CircleX20(3, 'CircleX20', '20 letter diameter circle.', Grid),
    CircleX40(4, 'CircleX40', '40 letter diameter circle.', Grid),
    PyramidX20(5, 'PyramidX20', 'Pyramid with 20 letter wide base.', Grid),
    PyramidX40(5, 'PyramidX20', 'Pyramid with 40 letter wide base.', Grid),

    WordWrap(2, GameFeatureGroupType.Difficulty, 'Word Wrap', 'Words can wrap around edges.'),
    WordWrapYes(1, 'Yes', 'Allows words to wrap around edges.', WordWrap),
    WordWrapNo(2, 'No', 'Prevents words from wrapping around edges.', WordWrap),

    JumbleOnFind(3, GameFeatureGroupType.Difficulty, 'Jumble', 'Finding a word causes the puzzle to re-jumble remaining letters.'),
    JumbleOnFindNo(1, 'No', 'Puzzle not rearranged after finding a word.', JumbleOnFind),
    JumbleOnFindYes(2, 'Yes', 'Puzzle is re-jumbled after each word find.', JumbleOnFind),

    AverageWordLength(4, GameFeatureGroupType.Difficulty, 'Word Length', 'Average word length.'),
    AverageOf5(1, '5', 'Average word length to be near 5 letters.', AverageWordLength),
    AverageOf4(2, '4', 'Average word length to be near 4 letters.', AverageWordLength),
    AverageOf3(3, '3', 'Average word length to be near 3 letters.', AverageWordLength),
    AverageOf6(4, '6', 'Average word length to be near 6 letters.', AverageWordLength),
    AverageOf7(5, '7+', 'Average word length to be near 7+ letters.', AverageWordLength),

    FillDifficulty(5, GameFeatureGroupType.Difficulty, 'Fill Difficulty', 'How random are fill letters vs words?'),
    RandomFill(1, 'Random', 'Fill letters are random', FillDifficulty),
    SomeOverlap(2, 'Less random', 'Fill letters will use word letters some what more often than randomly', FillDifficulty),
    StrongOverlap(3, 'Word Letters', 'Fill letters will fill mostly with letters from words', FillDifficulty),
    WordChunks(4, 'Word Chunks', 'Fill with chunks of words.', FillDifficulty),

    HideWordLetters(6, GameFeatureGroupType.Difficulty, 'Partial words.', 'Hide some of the letters in the words to find.'),
    HideWordLettersNone(1, 'None', 'Words to find are shown completely.', HideWordLetters),
    HideWordLettersSome(2, 'Some', 'Words to find show more than 75% of their letters.', HideWordLetters),
    HideWordLettersMany(3, 'Many', 'Words to find show less than 75% of their letters.', HideWordLetters),

    //  TODO - multi player options
    /*
    Competitiveness(2, GameFeatureGroupType.MultiPlayer, GameFeature.class, Competitive, 'Multi Player Mode', 'Do friends compete or help?'),
    Competitive(1, 'Competitive', 'Each friend searches independently, fastest friend wins.', Competitiveness),
    Cooperative(2, 'Cooperative', 'Work together, friend with most finds wins.', Grid),
    */

    final GameFeatureGroupType groupType
    final GameFeature group
    final String label
    final String description
    final int order

    //  Constructor for groups
    public GameFeature(
            final int order,
            final GameFeatureGroupType groupType,
            final String label,
            final String description
    ) {
        this.order = order
        this.label = label
        this.description = description
        this.group = this
        this.groupType = groupType
    }

    public GameFeature(
            final int order,
            final String label,
            final String description,
            final GameFeature group
    ) {
        this.order = order
        this.description = description
        this.group = group
        this.label = label
        this.groupType = group.groupType
    }

    static final Map<GameFeature, List<GameFeature>> groupedFeatures = [:]
    static {
        values().findAll {
            GameFeature it ->
                it.group == it
        }.each {
            GameFeature it ->
                groupedFeatures.put(it, [])
        }

        values().findAll {
            GameFeature it ->
                it.group != it
        }.each {
            GameFeature it ->
                groupedFeatures[it.group].add(it)
        }

        groupedFeatures.values().each {
            List<GameFeature> o ->
                o.sort { GameFeature a, GameFeature b -> a.order.compareTo(b.order) }
        }
    }
}