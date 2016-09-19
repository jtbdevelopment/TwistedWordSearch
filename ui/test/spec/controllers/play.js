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
                ['D', ' ', 'F'],
                ['G', 'H', 'Z']
            ],
            wordsToFind: ['DA', 'DEF', 'DEC']
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
            addSelectedStyleToCoordinates: jasmine.createSpy('gtm-addstyle'),
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
        var PlayCtrl, $scope, $rootScope, $http, $q, elementSpy;

        beforeEach(function () {
            expectedGame = angular.copy(baseGame);
            foundWordsCanvasManager.updateForGame.calls.reset();
            gridOffsetTracker.reset.calls.reset();
            gridOffsetTracker.gridSize.calls.reset();
            gridTableManager.addSelectedStyleToCoordinates.calls.reset();
            gridTableManager.removeSelectedStyleFromCoordinates.calls.reset();
            gridTableManager.calculateSelected.calls.reset();
            canvasLineDrawer.drawLine.calls.reset();
        });

        var context = {
            clearRect: jasmine.createSpy('clearRect'),
            beginPath: jasmine.createSpy('beginPath'),
            closePath: jasmine.createSpy('closePath'),
            aVariable: 'X'
        };

        var canvas = {
            width: 322,
            height: 408,
            getContext: function (type) {
                expect(type).toEqual('2d');
                return context;
            }
        };
        var ngElementFake = function (name) {
            expect(name).toEqual('#select-canvas');
            return [canvas];
        };

        beforeEach(inject(function ($controller, _$rootScope_, $httpBackend, _$q_) {
            $rootScope = _$rootScope_;
            $http = $httpBackend;
            $q = _$q_;
            spyOn($rootScope, '$broadcast').and.callThrough();
            elementSpy = spyOn(angular, 'element').and.callFake(ngElementFake);

            $scope = $rootScope.$new();
            PlayCtrl = $controller('PlayCtrl', {
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

        it('initializes playing game', function () {
            expect($scope.gridCanvasStyle).toEqual({top: 0, left: 0, height: 0, width: 0});
            expect(PlayCtrl.actions).toEqual(jtbBootstrapActions);
            expect(PlayCtrl.offsetTracker).toEqual(gridOffsetTracker);
            expect(PlayCtrl.grid === gtmCells).toBeTruthy();
            expect(PlayCtrl.cellStyles === gtmStyles).toBeTruthy();
            expect(PlayCtrl.showQuit).toBeTruthy();
            expect(PlayCtrl.showRematch).not.toBeTruthy();
            expect(PlayCtrl.forwardIsWord).not.toBeTruthy();
            expect(PlayCtrl.backwardIsWord).not.toBeTruthy();
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(gridOffsetTracker.reset).toHaveBeenCalled();
            expect(gridOffsetTracker.gridSize).toHaveBeenCalledWith(4, 3);
            expect(PlayCtrl.game).toEqual(expectedGame);
            expect(PlayCtrl.fontSize).toEqual(originalStyle);
            expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3);

            expect(PlayCtrl.description).toEqual([]);

            var expectedDescription = {a: 1, b: '32'};
            featurePromise.resolve(expectedDescription);
            $scope.$apply();
            expect(PlayCtrl.description).toEqual(expectedDescription);
        });

        afterEach(function () {
            elementSpy.and.callThrough();
        });

        it('zoom in', function () {
            PlayCtrl.zoomIn(4);
            expect(PlayCtrl.fontSize).toEqual({original: 4});
        });

        it('zoom out', function () {
            PlayCtrl.zoomOut(2);
            expect(PlayCtrl.fontSize).toEqual({original: 2});
        });

        /*
         grid: [
         ['A', 'B', 'C'],
         ['D', 'E', 'F'],
         ['D', ' ', 'F'],
         ['G', 'H', 'Z']
         ]
         */
        it('clicking on game starts selection', function () {
            var selectedCoordinates = [{row: 1, column: 0}];
            var selectedReturnValue = {
                selectedCoordinates: selectedCoordinates,
                originalSelectedCells: selectedCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(selectedReturnValue);
            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 1;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }

            });
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith([]);
            expect(gridTableManager.addSelectedStyleToCoordinates).toHaveBeenCalledWith(selectedCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual(selectedReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(selectedReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
            expect(context.beginPath).toHaveBeenCalled();
            expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
                context,
                selectedCoordinates[0],
                selectedCoordinates[0],
                canvas.height / 4,
                canvas.width / 3,
                '#9DC4B5'
            );
            expect(context.closePath).toHaveBeenCalled();
        });

        describe('game that is over, but rematch available', function () {
            beforeEach(inject(function ($controller) {
                expectedGame.gamePhase = 'RoundOver';
                PlayCtrl = $controller('PlayCtrl', {
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
            it('initializes round over game', function () {
                expect(PlayCtrl.showQuit).toEqual(false);
                expect(PlayCtrl.showRematch).toEqual(true);
                expect(gridOffsetTracker.reset).toHaveBeenCalled();
                expect(gridOffsetTracker.gridSize).toHaveBeenCalledWith(4, 3);
                expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3);
            });
        });

        describe('game that is over, but rematch available', function () {
            beforeEach(inject(function ($controller) {
                expectedGame.gamePhase = 'VeryOld';
                PlayCtrl = $controller('PlayCtrl', {
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
            it('initializes older game', function () {
                expect(PlayCtrl.showQuit).toEqual(false);
                expect(PlayCtrl.showRematch).toEqual(false);
                expect(gridOffsetTracker.reset).toHaveBeenCalled();
                expect(gridOffsetTracker.gridSize).toHaveBeenCalledWith(4, 3);
                expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3);
            });
        });
    }
);
