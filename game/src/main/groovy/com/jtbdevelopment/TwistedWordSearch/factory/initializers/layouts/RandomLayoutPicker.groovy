package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts

import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/25/16
 * Time: 7:13 AM
 */
@Component
@CompileStatic
class RandomLayoutPicker {
    private Random random = new Random()
    private List<WordLayout> layouts = WordLayout.values().toList()
    private int upperBound = layouts.size()

    public WordLayout getRandomLayout() {
        return layouts[random.nextInt(upperBound)]
    }
}
