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
    Grid20X20(2, '20x20', '20 x 20 square grid.', Grid),  // 400 squares
    Grid30X30(3, '30x30', '30 x 30 square grid.', Grid),  // 900 squares
    Grid40X40(4, '40x40', '40 x 40 square grid.', Grid),  // 1600 squares
    Grid50X50(5, '50x50', '50 x 50 square grid.', Grid),  // 2500 squares
    CircleX30(7, 'Circle x30', '30 letter diameter circle.', Grid),  // really 31!
    CircleX40(8, 'Circle x40', '40 letter diameter circle.', Grid),  // really 41!
    CircleX50(9, 'Circle x50', '50 letter diameter circle.', Grid),  // really 51!
    PyramidX40(10, 'Pyramid x40', 'Pyramid with 40 letter wide base.', Grid),  // 420 squares
    PyramidX50(11, 'Pyramid x50', 'Pyramid with 50 letter wide base.', Grid),  // 650 squares
    Diamond30x30(12, 'Diamond x30', '30x30 diamond.', Grid),
    Diamond40x40(13, 'Diamond x40', '40x40 diamond.', Grid),  // 840
    Diamond50x50(14, 'Diamond x50', '50x50 diamond.', Grid),  // 1300 squares

    WordWrap(2, GameFeatureGroupType.Difficulty, 'Word Wrap', 'Words can wrap around edges.'),
    WordWrapNo(1, 'No', 'Prevents words from wrapping around edges.', WordWrap),
    WordWrapYes(2, 'Yes', 'Allows words to wrap around edges.', WordWrap),

    //  TODO - implement
            /*
    JumbleOnFind(3, GameFeatureGroupType.Difficulty, 'Jumble', 'Finding a word causes the puzzle to re-jumble remaining letters.'),
    JumbleOnFindNo(1, 'No', 'Puzzle not rearranged after finding a word.', JumbleOnFind),
    JumbleOnFindYes(2, 'Yes', 'Puzzle is re-jumbled after each word find.', JumbleOnFind),
    */

            WordDifficulty(4, GameFeatureGroupType.Difficulty, 'Word Difficulty', 'How hard to find?'),
    BeginnerDifficulty(1, 'Beginner', 'Easiest - Fewer and longer words.', WordDifficulty),
    ExperiencedDifficulty(2, 'Experienced', 'Easier - More words that are a little shorter on average.', WordDifficulty),
    ExpertDifficulty(3, 'Expert', 'Harder - Even more words that are a little shorter on average.', WordDifficulty),
    ProfessionalDifficulty(4, 'Professional', 'Hardest - Most and shortest words.', WordDifficulty),

    FillDifficulty(5, GameFeatureGroupType.Difficulty, 'Fill Difficulty', 'How random are fill letters vs words?'),
    RandomFill(1, 'Random', 'Fill letters are random', FillDifficulty),
    SomeOverlap(2, 'Less random', 'Fill letters will use word letters some what more often than randomly', FillDifficulty),
    StrongOverlap(3, 'Word Letters', 'Fill letters will fill mostly with letters from words', FillDifficulty),
    WordChunks(4, 'Word Chunks', 'Fill with chunks of words.', FillDifficulty),

    //  TODO - implement
    /*
HideWordLetters(6, GameFeatureGroupType.Difficulty, 'Partial words.', 'Hide some of the letters in the words to find.'),
HideWordLettersNone(1, 'None', 'Words to find are shown completely.', HideWordLetters),
HideWordLettersSome(2, 'Some', 'Words to find show more than 75% of their letters.', HideWordLetters),
HideWordLettersMany(3, 'Many', 'Words to find show less than 75% of their letters.', HideWordLetters),
*/

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