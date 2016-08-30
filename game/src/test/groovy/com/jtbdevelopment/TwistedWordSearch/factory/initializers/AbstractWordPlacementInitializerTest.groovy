package com.jtbdevelopment.TwistedWordSearch.factory.initializers

import com.jtbdevelopment.TwistedWordSearch.factory.initializers.layouts.RandomLayoutPicker
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame
import com.jtbdevelopment.TwistedWordSearch.state.grid.Grid

/**
 * Date: 8/24/16
 * Time: 6:46 PM
 */
class AbstractWordPlacementInitializerTest extends GroovyTestCase {
    AbstractWordPlacementInitializer initializer = new AbstractWordPlacementInitializer() {
        protected Collection<String> getWordsToPlace(final TWSGame game) {
            return ['YOU', 'OF', 'TON']
        }

        int getOrder() {
            return 0
        }
    }

    void testInitializeGameNoWordWrap() {
        TWSGame game = new TWSGame(features: [GameFeature.WordWrapNo])
        game.grid = new Grid(3, 3)

        game.grid.setGridCell(0, 0, Grid.SPACE)
        game.grid.setGridCell(game.grid.rowUpperBound, game.grid.columnUpperBound, Grid.SPACE)
        initializer.randomLayoutPicker = new RandomLayoutPicker()

        assert [
                ' ??',
                '???',
                '?? ',
        ] == getGridAsStrings(game)

        Set<List<String>> used = new HashSet<>()
        (1..500).each {

            game.grid.resetGridLetters()
            initializer.initializeGame(game)

            List<String> strings = getGridAsStrings(game)

            assert noWrapPossibilities.contains(strings)
            used.add(strings)
        }
        assert used.size() > 30
        assert used.size() < 500
    }

    void testInitializeGameWordWrap() {
        TWSGame game = new TWSGame(features: [GameFeature.WordWrapYes])
        game.grid = new Grid(3, 3)

        game.grid.setGridCell(0, 0, Grid.SPACE)
        game.grid.setGridCell(game.grid.rowUpperBound, game.grid.columnUpperBound, Grid.SPACE)
        initializer.randomLayoutPicker = new RandomLayoutPicker()

        assert [
                ' ??',
                '???',
                '?? ',
        ] == getGridAsStrings(game)

        int wrapCount = 0
        Set<List<String>> used = new HashSet<>()
        (1..500).each {

            game.grid.resetGridLetters()
            initializer.initializeGame(game)

            List<String> strings = getGridAsStrings(game)

            if (wrapPossibilities.contains(strings)) ++wrapCount
            assert noWrapPossibilities.contains(strings) || wrapPossibilities.contains(strings)
            used.add(strings)
        }
        assert wrapCount > 0 // at least some should be word wrapped examples
        assert used.size() > 50
        assert used.size() < 500
    }

    private static List<String> getGridAsStrings(TWSGame game) {
        (0..game.grid.rowUpperBound).collect {
            int row ->
                game.grid.getGridRow(row).toString()
        }
    }

    Set<List<String>> wrapPossibilities = [
            [
                    ' ON',
                    'TFY',
                    'UO ',
            ],
            [
                    ' TU',
                    'OFO',
                    'NY ',
            ],
            [
                    ' YT',
                    'FOO',
                    'NU ',
            ],
            [
                    ' FT',
                    'YOU',
                    'NO ',
            ],
            [
                    ' YN',
                    'FOO',
                    'TU ',
            ],
            [
                    ' FN',
                    'YOU',
                    'TO ',
            ],
            [
                    ' UN',
                    'TY?',
                    'FO ',
            ],
            [
                    ' TF',
                    'UYO',
                    'N? ',
            ],
            [
                    ' UF',
                    'TNO',
                    'Y? ',
            ],
            [
                    ' TY',
                    'UN?',
                    'FO ',
            ],
            [
                    ' TF',
                    'UYO',
                    'NO ',
            ],
            [
                    ' UN',
                    'TYO',
                    'FO ',
            ],
            [
                    ' TY',
                    'UFO',
                    'NO ',
            ],
            [
                    ' UN',
                    'TFO',
                    'YO ',
            ],
            [
                    ' TY',
                    'UNO',
                    'FO ',
            ],
            [
                    ' UF',
                    'TNO',
                    'YO ',
            ],
            [
                    ' UT',
                    'NFO',
                    'YO ',
            ],
            [
                    ' NF',
                    'UYO',
                    'TO ',
            ],
            [
                    ' NY',
                    'UFO',
                    'TO ',
            ],
            [
                    ' NY',
                    'UTO',
                    'FO ',
            ],
            [
                    ' UT',
                    'NYO',
                    'FO ',
            ],
            [
                    ' UF',
                    'NTO',
                    'YO ',
            ],
            [
                    ' YF',
                    'TON',
                    'OU ',
            ],
            [
                    ' TO',
                    'YOU',
                    'FN ',
            ],
            [
                    ' YO',
                    'TON',
                    'FU ',
            ],
            [
                    ' TF',
                    'YOU',
                    'ON ',
            ],
            [
                    ' OT',
                    'NFY',
                    'UO ',
            ],
            [
                    ' NU',
                    'OFO',
                    'TY ',
            ],
            [
                    ' YN',
                    'OOF',
                    'TU ',
            ],
            [
                    ' ON',
                    'YOU',
                    'TF ',
            ],
            [
                    ' YT',
                    'OOF',
                    'NU ',
            ],
            [
                    ' OT',
                    'YOU',
                    'NF ',
            ],
            [
                    ' TU',
                    'YNO',
                    'FO ',
            ],
            [
                    ' TU',
                    'YFO',
                    'NO ',
            ],
            [
                    ' TU',
                    'FOO',
                    'YN ',
            ],
            [
                    ' TY',
                    'FOO',
                    'UN ',
            ],
            [
                    ' FY',
                    'TON',
                    'UO ',
            ],
            [
                    ' YN',
                    'TUO',
                    'FO ',
            ],
            [
                    ' TF',
                    'YUO',
                    'NO ',
            ],
            [
                    ' FU',
                    'TON',
                    'YO ',
            ],
            [
                    ' YF',
                    'TNO',
                    'UO ',
            ],
            [
                    ' YN',
                    'TFO',
                    'UO ',
            ],
            [
                    ' NY',
                    'UT?',
                    'FO ',
            ],
            [
                    ' UT',
                    'NY?',
                    'FO ',
            ],
            [
                    ' UF',
                    'NTO',
                    'Y? ',
            ],
            [
                    ' NF',
                    'UYO',
                    'T? ',
            ],
            [
                    ' UT',
                    'OFO',
                    'YN ',
            ],
            [
                    ' OY',
                    'UFN',
                    'TO ',
            ],
            [
                    ' OT',
                    'OFY',
                    'UN ',
            ],
            [
                    ' OT',
                    'OUY',
                    'FN ',
            ],
            [
                    ' OF',
                    'OUN',
                    'TY ',
            ],
            [
                    ' OU',
                    'OTN',
                    'FY ',
            ],
            [
                    ' OU',
                    'OFN',
                    'TY ',
            ],
            [
                    ' OF',
                    'OTY',
                    'UN ',
            ],
            [
                    ' ON',
                    'TFU',
                    'YO ',
            ],
            [
                    ' TY',
                    'OFO',
                    'NU ',
            ],
            [
                    ' YN',
                    'TU?',
                    'FO ',
            ],
            [
                    ' TF',
                    'YUO',
                    'N? ',
            ],
            [
                    ' YF',
                    'TNO',
                    'U? ',
            ],
            [
                    ' TU',
                    'YN?',
                    'FO ',
            ],
            [
                    ' OF',
                    '?TY',
                    'UN ',
            ],
            [
                    ' ?T',
                    'OUY',
                    'FN ',
            ],
            [
                    ' ?U',
                    'OTN',
                    'FY ',
            ],
            [
                    ' OF',
                    '?UN',
                    'TY ',
            ],
            [
                    ' UO',
                    'TNO',
                    'YF ',
            ],
            [
                    ' UN',
                    'TYF',
                    'OO ',
            ],
            [
                    ' TO',
                    'UYO',
                    'NF ',
            ],
            [
                    ' TY',
                    'UNF',
                    'OO ',
            ],
            [
                    ' ON',
                    'OUY',
                    'FT ',
            ],
            [
                    ' ON',
                    'OFY',
                    'UT ',
            ],
            [
                    ' OU',
                    'OFT',
                    'NY ',
            ],
            [
                    ' OF',
                    'ONY',
                    'UT ',
            ],
            [
                    ' OU',
                    'ONT',
                    'FY ',
            ],
            [
                    ' OF',
                    'OUT',
                    'NY ',
            ],
            [
                    ' TY',
                    'UNF',
                    '?O ',
            ],
            [
                    ' T?',
                    'UYO',
                    'NF ',
            ],
            [
                    ' TY',
                    'OOF',
                    'UN ',
            ],
            [
                    ' TU',
                    'OOF',
                    'YN ',
            ],
            [
                    ' OY',
                    'TON',
                    'UF ',
            ],
            [
                    ' OU',
                    'TON',
                    'YF ',
            ],
            [
                    ' U?',
                    'TNO',
                    'YF ',
            ],
            [
                    ' UN',
                    'TYF',
                    '?O ',
            ],
            [
                    ' NF',
                    'YOU',
                    'OT ',
            ],
            [
                    ' NO',
                    'YOU',
                    'FT ',
            ],
            [
                    ' YF',
                    'NOT',
                    'OU ',
            ],
            [
                    ' YO',
                    'NOT',
                    'FU ',
            ],
            [
                    ' ?N',
                    'OUY',
                    'FT ',
            ],
            [
                    ' ?U',
                    'ONT',
                    'FY ',
            ],
            [
                    ' OF',
                    '?NY',
                    'UT ',
            ],
            [
                    ' OF',
                    '?UT',
                    'NY ',
            ],
            [
                    ' YF',
                    'NTO',
                    'U? ',
            ],
            [
                    ' NU',
                    'YT?',
                    'FO ',
            ],
            [
                    ' NF',
                    'YUO',
                    'T? ',
            ],
            [
                    ' YT',
                    'NU?',
                    'FO ',
            ],
            [
                    ' OY',
                    'UFT',
                    'NO ',
            ],
            [
                    ' UN',
                    'OFO',
                    'YT ',
            ],
            [
                    ' NU',
                    'YTO',
                    'FO ',
            ],
            [
                    ' NU',
                    'YFO',
                    'TO ',
            ],
            [
                    ' YF',
                    'NTO',
                    'UO ',
            ],
            [
                    ' YT',
                    'NUO',
                    'FO ',
            ],
            [
                    ' YT',
                    'NFO',
                    'UO ',
            ],
            [
                    ' NF',
                    'YUO',
                    'TO ',
            ],
            [
                    ' OT',
                    'NFU',
                    'YO ',
            ],
            [
                    ' NY',
                    'OFO',
                    'TU ',
            ],
            [
                    ' FU',
                    'OTN',
                    '?Y ',
            ],
            [
                    ' FT',
                    'OUY',
                    '?N ',
            ],
            [
                    ' O?',
                    'FUN',
                    'TY ',
            ],
            [
                    ' O?',
                    'FTY',
                    'UN ',
            ],
            [
                    ' U?',
                    'NTO',
                    'YF ',
            ],
            [
                    ' N?',
                    'UYO',
                    'TF ',
            ],
            [
                    ' NY',
                    'UTF',
                    '?O ',
            ],
            [
                    ' UT',
                    'NYF',
                    '?O ',
            ],
            [
                    ' FY',
                    'NOT',
                    'UO ',
            ],
            [
                    ' NU',
                    'FOO',
                    'YT ',
            ],
            [
                    ' FU',
                    'NOT',
                    'YO ',
            ],
            [
                    ' NY',
                    'FOO',
                    'UT ',
            ],
            [
                    ' FT',
                    'OUY',
                    'ON ',
            ],
            [
                    ' OO',
                    'FTY',
                    'UN ',
            ],
            [
                    ' OO',
                    'FUN',
                    'TY ',
            ],
            [
                    ' FU',
                    'OTN',
                    'OY ',
            ],
            [
                    ' FT',
                    'UOY',
                    'NO ',
            ],
            [
                    ' FN',
                    'UOY',
                    'TO ',
            ],
            [
                    ' UT',
                    'FOO',
                    'NY ',
            ],
            [
                    ' UN',
                    'FOO',
                    'TY ',
            ],
            [
                    ' YN',
                    'TUF',
                    '?O ',
            ],
            [
                    ' TU',
                    'YNF',
                    '?O ',
            ],
            [
                    ' Y?',
                    'TNO',
                    'UF ',
            ],
            [
                    ' T?',
                    'YUO',
                    'NF ',
            ],
            [
                    ' NO',
                    'UYO',
                    'TF ',
            ],
            [
                    ' UT',
                    'NYF',
                    'OO ',
            ],
            [
                    ' NY',
                    'UTF',
                    'OO ',
            ],
            [
                    ' UO',
                    'NTO',
                    'YF ',
            ],
            [
                    ' O?',
                    'FUT',
                    'NY ',
            ],
            [
                    ' FN',
                    'OUY',
                    '?T ',
            ],
            [
                    ' FU',
                    'ONT',
                    '?Y ',
            ],
            [
                    ' O?',
                    'FNY',
                    'UT ',
            ],
            [
                    ' YT',
                    'OFO',
                    'UN ',
            ],
            [
                    ' OU',
                    'YFN',
                    'TO ',
            ],
            [
                    ' OT',
                    'OFU',
                    'YN ',
            ],
            [
                    ' OY',
                    'OTN',
                    'FU ',
            ],
            [
                    ' OY',
                    'OFN',
                    'TU ',
            ],
            [
                    ' OF',
                    'OYN',
                    'TU ',
            ],
            [
                    ' OF',
                    'OTU',
                    'YN ',
            ],
            [
                    ' OT',
                    'OYU',
                    'FN ',
            ],
            [
                    ' OO',
                    'FUT',
                    'NY ',
            ],
            [
                    ' FN',
                    'OUY',
                    'OT ',
            ],
            [
                    ' OO',
                    'FNY',
                    'UT ',
            ],
            [
                    ' FU',
                    'ONT',
                    'OY ',
            ],
            [
                    ' ?Y',
                    'OTN',
                    'FU ',
            ],
            [
                    ' ?T',
                    'OYU',
                    'FN ',
            ],
            [
                    ' OF',
                    '?YN',
                    'TU ',
            ],
            [
                    ' OF',
                    '?TU',
                    'YN ',
            ],
            [
                    ' NY',
                    'OOF',
                    'UT ',
            ],
            [
                    ' OU',
                    'NOT',
                    'YF ',
            ],
            [
                    ' NU',
                    'OOF',
                    'YT ',
            ],
            [
                    ' OY',
                    'NOT',
                    'UF ',
            ],
            [
                    ' OU',
                    'YFT',
                    'NO ',
            ],
            [
                    ' YN',
                    'OFO',
                    'UT ',
            ],
            [
                    ' TU',
                    'YNF',
                    'OO ',
            ],
            [
                    ' YO',
                    'TNO',
                    'UF ',
            ],
            [
                    ' YN',
                    'TUF',
                    'OO ',
            ],
            [
                    ' TO',
                    'YUO',
                    'NF ',
            ],
            [
                    ' UO',
                    'TON',
                    'FY ',
            ],
            [
                    ' UF',
                    'TON',
                    'OY ',
            ],
            [
                    ' TF',
                    'UOY',
                    'ON ',
            ],
            [
                    ' TO',
                    'UOY',
                    'FN ',
            ],
            [
                    ' OF',
                    'ONU',
                    'YT ',
            ],
            [
                    ' OF',
                    'OYT',
                    'NU ',
            ],
            [
                    ' OY',
                    'ONT',
                    'FU ',
            ],
            [
                    ' OY',
                    'OFT',
                    'NU ',
            ],
            [
                    ' ON',
                    'OFU',
                    'YT ',
            ],
            [
                    ' ON',
                    'OYU',
                    'FT ',
            ],
            [
                    ' ?N',
                    'OYU',
                    'FT ',
            ],
            [
                    ' OF',
                    '?NU',
                    'YT ',
            ],
            [
                    ' OF',
                    '?YT',
                    'NU ',
            ],
            [
                    ' ?Y',
                    'ONT',
                    'FU ',
            ],
            [
                    ' UT',
                    'OOF',
                    'NY ',
            ],
            [
                    ' ON',
                    'UOY',
                    'TF ',
            ],
            [
                    ' OT',
                    'UOY',
                    'NF ',
            ],
            [
                    ' UN',
                    'OOF',
                    'TY ',
            ],
            [
                    ' YO',
                    'NTO',
                    'UF ',
            ],
            [
                    ' NU',
                    'YTF',
                    'OO ',
            ],
            [
                    ' YT',
                    'NUF',
                    'OO ',
            ],
            [
                    ' NO',
                    'YUO',
                    'TF ',
            ],
            [
                    ' FY',
                    'ONT',
                    '?U ',
            ],
            [
                    ' O?',
                    'FNU',
                    'YT ',
            ],
            [
                    ' FN',
                    'OYU',
                    '?T ',
            ],
            [
                    ' O?',
                    'FYT',
                    'NU ',
            ],
            [
                    ' FY',
                    'OTN',
                    '?U ',
            ],
            [
                    ' N?',
                    'YUO',
                    'TF ',
            ],
            [
                    ' YT',
                    'NUF',
                    '?O ',
            ],
            [
                    ' NU',
                    'YTF',
                    '?O ',
            ],
            [
                    ' O?',
                    'FTU',
                    'YN ',
            ],
            [
                    ' O?',
                    'FYN',
                    'TU ',
            ],
            [
                    ' Y?',
                    'NTO',
                    'UF ',
            ],
            [
                    ' FT',
                    'OYU',
                    '?N ',
            ],
            [
                    ' OO',
                    'FNU',
                    'YT ',
            ],
            [
                    ' FN',
                    'OYU',
                    'OT ',
            ],
            [
                    ' OO',
                    'FYT',
                    'NU ',
            ],
            [
                    ' FY',
                    'ONT',
                    'OU ',
            ],
            [
                    ' NO',
                    'UOY',
                    'FT ',
            ],
            [
                    ' NF',
                    'UOY',
                    'OT ',
            ],
            [
                    ' UO',
                    'NOT',
                    'FY ',
            ],
            [
                    ' UF',
                    'NOT',
                    'OY ',
            ],
            [
                    ' OO',
                    'FTU',
                    'YN ',
            ],
            [
                    ' OO',
                    'FYN',
                    'TU ',
            ],
            [
                    ' FY',
                    'OTN',
                    'OU ',
            ],
            [
                    ' FT',
                    'OYU',
                    'ON ',
            ],

    ]

    Set<List<String>> noWrapPossibilities = [
            [
                    ' ?U',
                    'TON',
                    'YF ',
            ],
            [
                    ' UN',
                    'FO?',
                    'TY ',
            ],
            [
                    ' FT',
                    'YOU',
                    'N? ',
            ],
            [
                    ' U?',
                    'NOT',
                    'FY ',
            ],
            [
                    ' FU',
                    'NOT',
                    'Y? ',
            ],
            [
                    ' TF',
                    'YOU',
                    '?N ',
            ],
            [
                    ' TU',
                    '?OF',
                    'YN ',
            ],
            [
                    ' UF',
                    'TON',
                    '?Y ',
            ],
            [
                    ' TY',
                    '?OF',
                    'UN ',
            ],
            [
                    ' FN',
                    'UOY',
                    'T? ',
            ],
            [
                    ' Y?',
                    'TON',
                    'FU ',
            ],
            [
                    ' TY',
                    'FO?',
                    'UN ',
            ],
            [
                    ' T?',
                    'UOY',
                    'FN ',
            ],
            [
                    ' UT',
                    'FO?',
                    'NY ',
            ],
            [
                    ' TF',
                    'UOY',
                    '?N ',
            ],
            [
                    ' YN',
                    'FO?',
                    'TU ',
            ],
            [
                    ' YN',
                    '?OF',
                    'TU ',
            ],
            [
                    ' NF',
                    'YOU',
                    '?T ',
            ],
            [
                    ' ?N',
                    'UOY',
                    'TF ',
            ],
            [
                    ' NY',
                    '?OF',
                    'UT ',
            ],
            [
                    ' UT',
                    '?OF',
                    'NY ',
            ],
            [
                    ' FY',
                    'NOT',
                    'U? ',
            ],
            [
                    ' NU',
                    'FO?',
                    'YT ',
            ],
            [
                    ' NF',
                    'UOY',
                    '?T ',
            ],
            [
                    ' U?',
                    'TON',
                    'FY ',
            ],
            [
                    ' ?T',
                    'UOY',
                    'NF ',
            ],
            [
                    ' T?',
                    'YOU',
                    'FN ',
            ],
            [
                    ' FN',
                    'YOU',
                    'T? ',
            ],
            [
                    ' TU',
                    'FO?',
                    'YN ',
            ],
            [
                    ' UN',
                    '?OF',
                    'TY ',
            ],
            [
                    ' ?N',
                    'YOU',
                    'TF ',
            ],
            [
                    ' YF',
                    'TON',
                    '?U ',
            ],
            [
                    ' N?',
                    'YOU',
                    'FT ',
            ],
            [
                    ' N?',
                    'UOY',
                    'FT ',
            ],
            [
                    ' NU',
                    '?OF',
                    'YT ',
            ],
            [
                    ' UF',
                    'NOT',
                    '?Y ',
            ],
            [
                    ' YT',
                    '?OF',
                    'NU ',
            ],
            [
                    ' FT',
                    'UOY',
                    'N? ',
            ],
            [
                    ' Y?',
                    'NOT',
                    'FU ',
            ],
            [
                    ' YT',
                    'FO?',
                    'NU ',
            ],
            [
                    ' ?T',
                    'YOU',
                    'NF ',
            ],
            [
                    ' ?Y',
                    'NOT',
                    'UF ',
            ],
            [
                    ' YF',
                    'NOT',
                    '?U ',
            ],
            [
                    ' FU',
                    'TON',
                    'Y? ',
            ],
            [
                    ' FY',
                    'NOT',
                    'U? ',
            ],
            [
                    ' NY',
                    'FO?',
                    'UT ',
            ],
            [
                    ' FY',
                    'TON',
                    'U? ',
            ],
            [
                    ' ?U',
                    'NOT',
                    'YF ',
            ],
            [
                    ' ?Y',
                    'TON',
                    'UF ',
            ]
    ]

}
