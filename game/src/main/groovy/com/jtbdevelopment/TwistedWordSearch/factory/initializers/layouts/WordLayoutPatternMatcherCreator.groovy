package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts

import groovy.transform.CompileStatic
import org.springframework.stereotype.Component

/**
 * Date: 8/22/16
 * Time: 6:50 AM
 */
@Component
@CompileStatic
class WordLayoutPatternMatcherCreator {

    @SuppressWarnings("GrMethodMayBeStatic")
    char[][] createMatchingArrayForLayout(String word, WordLayout layout) {
        char[][] cells;
        char[] charArray = word.toCharArray()
        char[] reverseArray = word.reverse().toCharArray()
        int size = word.size()
        switch (layout) {
            case WordLayout.HorizontalForward:
                cells = [charArray]
                break
            case WordLayout.HorizontalBackward:
                cells = [reverseArray]
                break
            case WordLayout.VerticalDown:
                cells = (char[][]) charArray.collect {
                    [it]
                }.toArray()
                break
            case WordLayout.VerticalUp:
                cells = (char[][]) reverseArray.collect {
                    [it]
                }.toArray()
                break
            default:
                cells = new char[size][size]
                (0..(size - 1)).each {
                    int r ->
                        (0..(size - 1)).each {
                            int c ->
                                cells[r][c] = ' ' as char
                        }
                }
                switch (layout) {
                    case WordLayout.SlopingDownForward:
                        (0..(size - 1)).each {
                            int c ->
                                cells[c][c] = charArray[c]
                        }
                        break;
                    case WordLayout.SlopingDownBackward:
                        (0..(size - 1)).each {
                            int c ->
                                cells[c][c] = reverseArray[c]
                        }
                        break;
                    case WordLayout.SlopingUpForward:
                        (0..(size - 1)).each {
                            int c ->
                                cells[size - c - 1][c] = charArray[c]
                        }
                        break;
                    case WordLayout.SlopingUpBackward:
                        (0..(size - 1)).each {
                            int c ->
                                cells[size - c - 1][c] = reverseArray[c]
                        }
                        break;
                }
                break;
        }
        return cells
    }
}
