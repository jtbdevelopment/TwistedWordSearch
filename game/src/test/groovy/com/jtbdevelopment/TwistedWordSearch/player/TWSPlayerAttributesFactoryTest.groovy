package com.jtbdevelopment.TwistedWordSearch.player

/**
 * Date: 7/27/16
 * Time: 6:39 PM
 */
class TWSPlayerAttributesFactoryTest extends GroovyTestCase {
    TWSPlayerAttributesFactory factory = new TWSPlayerAttributesFactory()

    void testNewPlayerAttributes() {
        assert factory.newPlayerAttributes() instanceof TWSPlayerAttributes
    }

    void testNewManualPlayerAttributes() {
        assert factory.newManualPlayerAttributes() instanceof TWSPlayerAttributes
    }

    void testNewSystemPlayerAttributes() {
        assertNull(factory.newSystemPlayerAttributes())
    }
}
