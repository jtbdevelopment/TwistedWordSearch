package com.jtbdevelopment.TwistedWordSearch.state.masking

import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
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

    void testMaskingAGameNotInChallengedOrSetupPhases() {
        GamePhase.values().findAll { it != GamePhase.Setup && it != GamePhase.Challenged}.each {
            TWSGame game = new TWSGame(players: [PONE, PFOUR], initiatingPlayer: PONE.id, gamePhase: it)
            game.grid = new Grid(10, 10)
            game.wordsToFind = ['A', 'SET', 'OF', 'WORDS']
            game.hintsGiven = ['A': new GridCoordinate(0, 3), 'OF': new GridCoordinate(5, 6)]
            game.scores = [(PONE.id): 10, (PFOUR.id): 32]
            game.wordsFoundByPlayer = [
                    (PONE.id) : ['I', 'FOUND', 'THESE'] as Set,
                    (PFOUR.id): [] as Set
            ]
            game.hintsRemaining = 4
            game.foundWordLocations = ['I': [new GridCoordinate(1, 1), new GridCoordinate(1, 2), new Grid(1, 3)] as Set]

            MaskedGame masked = masker.maskGameForPlayer(game, PONE)
            assert masked.grid.is(game.grid.gridCells)
            assert game.wordsToFind == new TreeSet(masked.wordsToFind)
            assert [(PONE.md5): new TreeSet(['I', 'FOUND', 'THESE'] as Set), (PFOUR.md5): [] as Set] == masked.wordsFoundByPlayer
            assert [(PONE.md5): 10, (PFOUR.md5): 32] == masked.scores
            assert game.foundWordLocations.is(masked.foundWordLocations)

            //  Minor proofs that overridden methods called base implementations
            assert [(PONE.md5): PONE.displayName, (PFOUR.md5): PFOUR.displayName] == masked.players
            assert it == masked.gamePhase
            assert game.hintsGiven.values() as Set == masked.hints
            assert game.hintsRemaining == masked.hintsRemaining
        }
    }

    void testMaskingAGameInChallengedOrSetupPhases() {
        GamePhase.values().findAll { it == GamePhase.Setup || it == GamePhase.Challenged}.each {
            TWSGame game = new TWSGame(players: [PONE, PFOUR], initiatingPlayer: PONE.id, gamePhase: it)
            game.grid = new Grid(10, 10)
            game.wordsToFind = ['A', 'SET', 'OF', 'WORDS']
            game.scores = [(PONE.id): 10, (PFOUR.id): 32]
            game.wordsFoundByPlayer = [
                    (PONE.id) : ['I', 'FOUND', 'THESE'] as Set,
                    (PFOUR.id): [] as Set
            ]
            game.hintsRemaining = 3
            game.foundWordLocations = ['I': [new GridCoordinate(1, 1), new GridCoordinate(1, 2), new Grid(1, 3)] as Set]

            MaskedGame masked = masker.maskGameForPlayer(game, PONE)
            assertNull masked.grid
            assertNull masked.wordsToFind
            assertNull masked.wordsFoundByPlayer
            assert [(PONE.md5): 10, (PFOUR.md5): 32] == masked.scores
            assertNull masked.foundWordLocations

            //  Minor proofs that overridden methods called base implementations
            assert [(PONE.md5): PONE.displayName, (PFOUR.md5): PFOUR.displayName] == masked.players
            assert it == masked.gamePhase
            assert game.hintsGiven.values() as Set == masked.hints
            assert game.hintsRemaining == masked.hintsRemaining
        }
    }

    void testGetIDClass() {
        assert ObjectId.class == masker.IDClass
    }
}
