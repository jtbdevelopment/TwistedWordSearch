package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.factory.GameInitializer

/**
 * Date: 8/30/16
 * Time: 6:47 AM
 */
class WordChunkFillInitializerTest extends GroovyTestCase {
    WordChunkFillInitializer initializer = new WordChunkFillInitializer()

    void testGetOrder() {
        assert GameInitializer.DEFAULT_ORDER + 20 == initializer.order
    }

    void testReturnsEmptyListForNonWordChunkFillTypes() {
        GameFeature.findAll {
            GameFeature it ->
                it.group == GameFeature.FillDifficulty && it != GameFeature.WordChunks
        }.each {
            GameFeature it ->
                TWSGame game = new TWSGame(features: [it] as Set)
                game.grid = new Grid(20, 20)
                game.words = ['SOME', 'WORDS', 'ARE', 'HERE']

                assert [] == initializer.getWordsToPlace(game)
        }
    }

    void testWordsForWordChunks() {
        TWSGame game = new TWSGame(features: [GameFeature.WordChunks] as Set)
        game.grid = new Grid(50, 50)
        game.words = ['SOME', 'WORDS', 'ARE', 'ASTONISHINGLY', 'LONGER']

        Collection<String> wordChunks = initializer.getWordsToPlace(game)
        assert [] != wordChunks
        assert 1874 <= wordChunks.collect { it.size() }.sum()
        wordChunks.forEach {
            String word ->
                assert ACCEPTABLE_CHUNKS.contains(word)
        }
    }

    private static ACCEPTABLE_CHUNKS = [
            'A',
            'AS',
            'AST',
            'ASTO',
            'ASTON',
            'ASTONI',
            'ASTONIS',
            'D',
            'E',
            'G',
            'GE',
            'GL',
            'H',
            'HI',
            'HIN',
            'HING',
            'HINGL',
            'I',
            'IN',
            'ING',
            'INGL',
            'IS',
            'ISH',
            'ISHI',
            'ISHIN',
            'ISHING',
            'ISHINGL',
            'L',
            'LO',
            'LON',
            'M',
            'N',
            'NG',
            'NGE',
            'NGL',
            'NI',
            'NIS',
            'NISH',
            'NISHI',
            'NISHIN',
            'NISHING',
            'O',
            'OM',
            'ON',
            'ONG',
            'ONI',
            'ONIS',
            'ONISH',
            'ONISHI',
            'ONISHIN',
            'OR',
            'ORD',
            'R',
            'RD',
            'S',
            'SH',
            'SHI',
            'SHIN',
            'SHING',
            'SHINGL',
            'SO',
            'ST',
            'STO',
            'STON',
            'STONI',
            'STONIS',
            'STONISH',
            'T',
            'TO',
            'TON',
            'TONI',
            'TONIS',
            'TONISH',
            'TONISHI',
            'W',
            'WO',
            'WOR',
    ] as Set
}
