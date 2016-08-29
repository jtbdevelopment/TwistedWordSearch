package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/29/16
 * Time: 6:30 PM
 */
class RandomFillInitializerTest extends GroovyTestCase {
    RandomFillInitializer initializer = new RandomFillInitializer()

    void testFillDoesNotFillNonQuestionMarks() {
        TWSGame game = new TWSGame(features: [GameFeature.RandomFill])
        game.grid = new Grid(10, 10)
        game.words = []

        game.grid.setGridCell(0, 0, Grid.SPACE)
        game.grid.setGridCell(5, 5, Grid.SPACE)
        game.grid.setGridCell(1, 1, 'X' as char)
        game.grid.setGridCell(1, 2, 'Y' as char)
        game.grid.setGridCell(1, 3, 'Z' as char)

        initializer.initializeGame(game)

        (0..game.grid.rowUpperBound).each {
            int row ->
                (0..game.grid.columnUpperBound).each {
                    int col ->
                        char letter = game.grid.getGridCell(row, col)
                        if ((row == 0 && col == 0) || (row == 5 && col == 5)) {
                            assert Grid.SPACE == letter
                        } else if (row == 1 && (col >= 1 && col <= 3)) {
                            assert 'XYZ'.toCharArray()[col - 1] == letter
                        } else {
                            assert letter >= ('A' as char)
                            assert letter <= ('Z' as char)
                        }
                }
        }
    }

    void testInitializeGameFullRandom() {
        TWSGame game = new TWSGame(features: [GameFeature.RandomFill])
        def (Set<Character> wordLetters, Map<Character, Integer> counts) = testGame(game)

        // average shold be around 384
        assert 0 == counts.findAll { it.value > 450 }.size()
    }

    void testInitializeGameWordChunks() {
        //  Same as full random - assumes chunks have been placed already
        TWSGame game = new TWSGame(features: [GameFeature.WordChunks])
        def (Set<Character> wordLetters, Map<Character, Integer> counts) = testGame(game)

        // average shold be around 384
        assert 0 == counts.findAll { it.value > 450 }.size()
    }

    void testInitializeGameSomeOverlap() {
        TWSGame game = new TWSGame(features: [GameFeature.SomeOverlap])
        def (Set<Character> wordLetters, Map<Character, Integer> counts) = testGame(game)

        //2500 should go to non-random + 288 (1100) to random
        def nonRandom = wordLetters.collect { counts[it] }.sum()
        assert 3300 < nonRandom

        wordLetters.each { counts.remove(it) }

        // average shold be around 288
        assert 0 == counts.findAll { it.value > 345 }.size()
    }

    void testInitializeGameStrongOverlap() {
        TWSGame game = new TWSGame(features: [GameFeature.StrongOverlap])
        def (Set<Character> wordLetters, Map<Character, Integer> counts) = testGame(game)

        //5000 should go to non-random + 192 (769) to random
        def nonRandom = wordLetters.collect { counts[it] }.sum()
        assert 5500 < nonRandom

        wordLetters.each { counts.remove(it) }

        // average shold be around 192
        assert 0 == counts.findAll { it.value > 250 }.size()
    }

    private List testGame(TWSGame game) {
        game.grid = new Grid(100, 100)
        game.words = ['TEXT', 'EXIT']

        Set<Character> wordLetters = [] as Set
        wordLetters.addAll(game.words[0].toCharArray().toList())
        wordLetters.addAll(game.words[1].toCharArray().toList())

        initializer.initializeGame(game)
        Map<Character, Integer> counts = initialCounts()

        (0..game.grid.rowUpperBound).each {
            int row ->
                (0..game.grid.columnUpperBound).each {
                    int col ->
                        char letter = game.grid.getGridCell(row, col)
                        counts[letter] += 1
                }
        }
        [wordLetters, counts]
    }

    private static Map<Character, Integer> initialCounts() {
        def counts = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.toCharArray().toList().collectEntries {
            char c ->
                [(c): 0]
        }
        counts
    }

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER + 30 == initializer.order
    }
}
