package com.jtbdevelopment.TwistedWordSearch.state

import groovy.transform.CompileStatic

/**
 * Date: 7/11/16
 * Time: 6:57 PM
 */
@CompileStatic
enum TWSGameFeature {
    //  TODO - more - random jumble on timer
    Grid(1, TWSGameFeatureGroupType.Difficulty, TWSGameFeature.class, null, 'Grid', 'Type of grid to play on.'),
    Grid40X40(1, '40x40', '40 x 40 square grid.', Grid),
    Grid20X20(2, '20x20', '20 x 20 square grid.', Grid),
    CircleX20(3, 'CircleX20', '20 letter diameter circle.', Grid),
    CircleX40(4, 'CircleX40', '40 letter diameter circle.', Grid),
    PyramidX20(5, 'PyramidX20', 'Pyramid with 20 letter wide base.', Grid),
    PyramidX40(5, 'PyramidX20', 'Pyramid with 40 letter wide base.', Grid),

    WordWrap(2, TWSGameFeatureGroupType.Difficulty, Boolean.class, Boolean.TRUE, 'Word Wrap', 'Words can wrap around edges.'),

    JumbleOnFind(3, TWSGameFeatureGroupType.Difficulty, Boolean.class, Boolean.FALSE, 'Jumble', 'Finding a word causes the puzzle to re-jumble remaining letters.'),

    AverageWordLength(4, TWSGameFeatureGroupType.Difficulty, Integer.class, 5, 'Word Length', 'Average word length.'),

    FillDifficulty(5, TWSGameFeatureGroupType.Difficulty, TWSGameFeature.class, null, 'Fill Difficulty', 'How random are fill letters vs words?'),
    RandomFill(1, 'Random', 'Fill letters are random', FillDifficulty),
    SomeOverlap(2, 'Less random', 'Fill letters will use word letters some what more often than randomly', FillDifficulty),
    StrongOverlap(3, 'Word Letters', 'Fill letters will fill mostly with letters from words', FillDifficulty),
    WordChunks(4, 'Word Chunks', 'Fill with chunks of words.', FillDifficulty)

    //  TODO - multi player options
    /*
    Competitiveness(2, GameFeatureGroupType.MultiPlayer, GameFeature.class, Competitive, 'Multi Player Mode', 'Do friends compete or help?'),
    Competitive(1, 'Competitive', 'Each friend searches independently, fastest friend wins.', Competitiveness),
    Cooperative(2, 'Cooperative', 'Work together, friend with most finds wins.', Grid),
    */

    final TWSGameFeatureGroupType groupType
    final TWSGameFeature group
    final String label
    final String description
    final Class<?> valueType
    final Object groupDefault  // only populated when type is not GameFeature, otherwise assume first item is default
    final int order

    //  Constructor for groups
    public TWSGameFeature(
            final int order,
            final TWSGameFeatureGroupType groupType,
            final Class<?> valueType,
            final Object groupDefault,
            final String label,
            final String description
    ) {
        this.order = order
        this.label = label
        this.description = description
        this.group = this
        this.groupDefault = groupDefault
        this.groupType = groupType
        this.valueType = valueType
    }

    public TWSGameFeature(
            final int order,
            final String label,
            final String description,
            final TWSGameFeature group
    ) {
        this.order = order
        this.description = description
        this.group = group
        this.groupDefault = null
        this.valueType = null
        this.label = label
        this.groupType = group.groupType
    }

    static final Map<TWSGameFeature, List<TWSGameFeature>> groupedFeatures = [:]
    static {
        values().findAll {
            TWSGameFeature it ->
                it.group == it
        }.each {
            TWSGameFeature it ->
                groupedFeatures.put(it, [])
        }

        values().findAll {
            TWSGameFeature it ->
                it.group != it
        }.each {
            TWSGameFeature it ->
                groupedFeatures[it.group].add(it)
        }

        groupedFeatures.values().each {
            List<TWSGameFeature> o ->
                o.sort { TWSGameFeature a, TWSGameFeature b -> a.order.compareTo(b.order) }
        }
    }
}