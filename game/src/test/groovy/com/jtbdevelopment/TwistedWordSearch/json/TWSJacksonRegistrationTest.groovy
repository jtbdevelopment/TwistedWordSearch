package com.jtbdevelopment.TwistedWordSearch.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedWordSearch.player.TWSPlayerAttributes
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame

/**
 * Date: 7/26/16
 * Time: 11:34 PM
 */
class TWSJacksonRegistrationTest extends GroovyTestCase {
    void testCustomizeModule() {
        TWSJacksonRegistration registration = new TWSJacksonRegistration()
        boolean registeredGameAttributes = false
        boolean registeredMaskedGame = false
        def module = [
                addAbstractTypeMapping: {
                    Class iface, Class impl ->
                        if (GameSpecificPlayerAttributes.class.is(iface)) {
                            assert TWSPlayerAttributes.class.is(impl)
                            registeredGameAttributes = true
                            return null
                        }
                        if (MaskedMultiPlayerGame.class.is(iface)) {
                            assert MaskedGame.class.is(impl)
                            registeredMaskedGame = true
                            return null
                        }
                        fail('unexpected attributes')
                }
        ] as SimpleModule
        registration.customizeModule(module)
        assert registeredGameAttributes
        assert registeredMaskedGame

    }
}
