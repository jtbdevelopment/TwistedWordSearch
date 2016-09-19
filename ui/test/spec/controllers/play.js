'use strict';

describe('Controller: PlayCtrl',
    function () {

        // load the controller's module
        beforeEach(module('twsUI'));

        var gameID = 'X123';
        var baseGame = {
            id: gameID,
            gamePhase: 'Playing',
            grid: [
                ['A', 'B', 'C'],
                ['D', 'E', 'F'],
                ['G', 'H', 'Z']
            ]
        };
        var expectedGame;
        var $routeParams = {
            gameID: gameID
        };

        var originalStyle = {original: 1};
        var fontSizeManager = {
            fontSizeStyle: function () {
                return originalStyle;
            },
            increaseFontSize: function (amount) {
                return {original: amount};
            },
            decreaseFontSize: function (amount) {
                return {original: amount};
            }
        };
        var gridOffsetTracker = {
            reset: jasmine.createSpy('reset'),
            gridSize: jasmine.createSpy('gridSpy')
        };

        var gtmCells = angular.copy(baseGame.grid);
        var gtmStyles = [
            ['', '', ''],
            ['', '', ''],
            ['', '', '']
        ];
        var gridTableManager = {
            updateForGame: function (game) {
                expect(game).toEqual(expectedGame);
                return {
                    cells: gtmCells,
                    styles: gtmStyles
                };
            },
            removeSelectedStyleFromCoordinates: jasmine.createSpy('gtm-removestyle'),
            addSelectedStyleFromCoordinates: jasmine.createSpy('gtm-addstyle'),
            calculateSelected: jasmine.createSpy('gtm-calcSelected')
        };

        var playerBaseURL = 'http://test.com/123';
        var jtbPlayerService = {
            currentPlayerBaseURL: function () {
                return playerBaseURL;
            }
        };

        var foundWordsCanvasManager = {
            updateForGame: jasmine.createSpy('fwcm-updateForGame')
        };

        var canvasLineDrawer = {
            drawLine: jasmine.createSpy('drawLine')
        };

        var featurePromise;
        var featureDescriber = {
            game: undefined,
            getShortDescriptionForGame: function (game) {
                featurePromise = $q.defer();
                this.game = game;
                return featurePromise.promise;
            }
        };

        var jtbBootstrapActions = {
            something: function () {
            }
        };

        var jtbGameCache = {
            getGameForID: function (id) {
                expect(id).toEqual(gameID);
                return expectedGame;
            }
        };

        // Initialize the controller and a mock scope
        var MainCtrl, $scope, $rootScope, $http, $q;

        beforeEach(function () {
            expectedGame = angular.copy(baseGame);
            foundWordsCanvasManager.updateForGame.calls.reset();
            gridOffsetTracker.reset.calls.reset();
            gridOffsetTracker.gridSize.calls.reset();
            gridTableManager.addSelectedStyleFromCoordinates.calls.reset();
            gridTableManager.removeSelectedStyleFromCoordinates.calls.reset();
            gridTableManager.calculateSelected.calls.reset();
            canvasLineDrawer.drawLine.calls.reset();
        });

        beforeEach(inject(function ($controller, _$rootScope_, $httpBackend, _$q_) {
            $rootScope = _$rootScope_;
            $http = $httpBackend;
            $q = _$q_;
            spyOn($rootScope, '$broadcast').and.callThrough();

            $scope = $rootScope.$new();
            MainCtrl = $controller('PlayCtrl', {
                $scope: $scope,
                $routeParams: $routeParams,
                gridOffsetTracker: gridOffsetTracker,
                gridTableManager: gridTableManager,
                fontSizeManager: fontSizeManager,
                foundWordsCanvasManager: foundWordsCanvasManager,
                canvasLineDrawer: canvasLineDrawer,
                featureDescriber: featureDescriber,
                jtbGameCache: jtbGameCache,
                jtbBootstrapGameActions: jtbBootstrapActions,
                jtbPlayerService: jtbPlayerService
            });
        }));

        it('dummy test', function () {

        });
    }
);
