package com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts

/**
 * Date: 8/22/16
 * Time: 6:51 AM
 */
class WordLayoutPatternMatcherCreatorTest extends GroovyTestCase {
    WordLayoutPatternMatcherCreator creator = new WordLayoutPatternMatcherCreator()

    String testWord = 'TEST'
    char[] testWordArray = testWord.toCharArray()

    void testHorizontalForward() {
        char[][] expected = new char[1][4]
        expected[0][0] = testWordArray[0]
        expected[0][1] = testWordArray[1]
        expected[0][2] = testWordArray[2]
        expected[0][3] = testWordArray[3]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.HorizontalForward)
    }

    void testHorizontalBackward() {
        char[][] expected = new char[1][4]
        expected[0][0] = testWordArray[3]
        expected[0][1] = testWordArray[2]
        expected[0][2] = testWordArray[1]
        expected[0][3] = testWordArray[0]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.HorizontalBackward)
    }

    void testVerticalDown() {
        char[][] expected = new char[4][1]
        expected[0][0] = testWordArray[0]
        expected[1][0] = testWordArray[1]
        expected[2][0] = testWordArray[2]
        expected[3][0] = testWordArray[3]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.VerticalDown)
    }

    void testVerticalUp() {
        char[][] expected = new char[4][1]
        expected[0][0] = testWordArray[3]
        expected[1][0] = testWordArray[2]
        expected[2][0] = testWordArray[1]
        expected[3][0] = testWordArray[0]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.VerticalUp)
    }

    void testSlopingDownForward() {
        char[][] expected = makeEmptyGrid()
        expected[0][0] = testWordArray[0]
        expected[1][1] = testWordArray[1]
        expected[2][2] = testWordArray[2]
        expected[3][3] = testWordArray[3]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.SlopingDownForward)

    }

    void testSlopingDownForwardBackward() {
        char[][] expected = makeEmptyGrid()
        expected[0][0] = testWordArray[3]
        expected[1][1] = testWordArray[2]
        expected[2][2] = testWordArray[1]
        expected[3][3] = testWordArray[0]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.SlopingDownBackward)

    }

    void testSlopingUpForward() {
        char[][] expected = makeEmptyGrid()
        expected[3][0] = testWordArray[0]
        expected[2][1] = testWordArray[1]
        expected[1][2] = testWordArray[2]
        expected[0][3] = testWordArray[3]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.SlopingUpForward)

    }

    void testSlopingUpForwardBackward() {
        char[][] expected = makeEmptyGrid()
        expected[3][0] = testWordArray[3]
        expected[2][1] = testWordArray[2]
        expected[1][2] = testWordArray[1]
        expected[0][3] = testWordArray[0]
        assert expected == creator.createMatchingArrayForLayout(testWord, WordLayout.SlopingUpBackward)

    }

    char[][] makeEmptyGrid() {
        char[][] grid = new char[4][4]
        (0..3).each {
            int row ->
                (0..3).each {
                    col ->
                        grid[row][col] = ' ' as char
                }
        }
        return grid
    }
}
