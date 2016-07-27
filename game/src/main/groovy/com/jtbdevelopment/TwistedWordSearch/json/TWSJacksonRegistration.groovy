package com.jtbdevelopment.TwistedWordSearch.json

import com.fasterxml.jackson.databind.module.SimpleModule
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame
import com.jtbdevelopment.spring.jackson.JacksonModuleCustomization
import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 2/8/15
 * Time: 4:08 PM
 */
@Component
@CompileStatic
class TWSJacksonRegistration implements JacksonModuleCustomization {
    @Override
    void customizeModule(final SimpleModule module) {
        //module.addAbstractTypeMapping(GameSpecificPlayerAttributes.class, TwistedHangmanPlayerAttributes.class)
        module.addAbstractTypeMapping(MaskedMultiPlayerGame.class, MaskedGame.class)
        module.registerSubtypes(MaskedGame.class)
    }
}
