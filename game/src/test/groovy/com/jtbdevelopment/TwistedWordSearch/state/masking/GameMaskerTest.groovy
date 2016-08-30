package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import com.jtbdevelopment.games.state.GamePhase
import org.bson.types.ObjectId

/**
 * Date: 7/13/16
 * Time: 7:09 PM
 */
class GameMaskerTest extends MongoGameCoreTestCase {
    GameMasker masker = new GameMasker()

    void testNewMaskedGame() {
        MaskedGame game = masker.newMaskedGame()
        assertNotNull game
    }

    void testMaskingAGame() {
        TWSGame game = new TWSGame(players: [PONE, PFOUR], initiatingPlayer: PONE.id, gamePhase: GamePhase.Playing)
        game.grid = new Grid(10, 10)
        game.wordsToFind = ['A', 'SET', 'OF', 'WORDS']
        game.wordsFoundByPlayer = [
                (PONE.id) : ['I', 'FOUND', 'THESE'] as Set,
                (PFOUR.id): [] as Set
        ]

        MaskedGame masked = masker.maskGameForPlayer(game, PONE)
        assert masked.grid.is(game.grid.gridCells)
        assert game.wordsToFind == masked.wordsToFind
        assert [(PONE.md5): ['I', 'FOUND', 'THESE'] as Set, (PFOUR.md5): [] as Set] == masked.wordsFoundByPlayer

        //  Minor proofs that overridden methods called base implementations
        assert [(PONE.md5): PONE.displayName, (PFOUR.md5): PFOUR.displayName] == masked.players
        assert GamePhase.Playing == masked.gamePhase
    }

    void testGetIDClass() {
        assert ObjectId.class == masker.IDClass
    }
}
