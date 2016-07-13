package com.jtbdevelopment.TwistedWordSearch.state.masking

import org.bson.types.ObjectId

/**
 * Date: 7/13/16
 * Time: 7:09 PM
 */
class TWSGameMaskerTest extends GroovyTestCase {
    TWSGameMasker masker = new TWSGameMasker()

    void testNewMaskedGame() {
        TWSMaskedGame game = masker.newMaskedGame()
        assertNotNull game
    }

    void testGetIDClass() {
        assert ObjectId.class == masker.IDClass
    }
}
