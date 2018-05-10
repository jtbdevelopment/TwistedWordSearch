package com.jtbdevelopment.TwistedWordSearch.json;

import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jtbdevelopment.TwistedWordSearch.player.TWSPlayerAttributes;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.players.GameSpecificPlayerAttributes;
import com.jtbdevelopment.games.state.masking.MaskedMultiPlayerGame;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Date: 7/26/16 Time: 11:34 PM
 */
public class TWSJacksonRegistrationTest {

  @Test
  public void testCustomizeModule() {
    SimpleModule module = Mockito.mock(SimpleModule.class);
    TWSJacksonRegistration registration = new TWSJacksonRegistration();
    registration.customizeModule(module);
    verify(module)
        .addAbstractTypeMapping(GameSpecificPlayerAttributes.class, TWSPlayerAttributes.class);
    verify(module).addAbstractTypeMapping(MaskedMultiPlayerGame.class, MaskedGame.class);
  }

}
