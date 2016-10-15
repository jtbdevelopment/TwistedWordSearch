'use strict';

describe('Controller: PlayCtrl',
    function () {

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
            wordsToFind: ['DA', 'DEF', 'DEC'],
            players: {'md51': 'Player1'}
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
        var gtmStyles = angular.copy([
            ['', '', ''],
            ['', '', ''],
            ['', '', ''],
            ['', '', '']
        ]);
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
            wrapActionOnGame: jasmine.createSpy('waog')
        };

        var jtbGameCache = {
            getGameForID: function (id) {
                expect(id).toEqual(gameID);
                return expectedGame;
            }
        };

        var gameAnimations = {
            animateGameUpdate: jasmine.createSpy('agu')
        };

        var targetCalculator = {
            calculateTargetCell: jasmine.createSpy('calcTarget')
        };

        var PlayCtrl, $scope, $rootScope, $http, $q, elementSpy;

        beforeEach(function () {
            gameAnimations.animateGameUpdate.calls.reset();
            expectedGame = angular.copy(baseGame);
            foundWordsCanvasManager.updateForGame.calls.reset();
            jtbBootstrapActions.wrapActionOnGame.calls.reset();
            gridOffsetTracker.reset.calls.reset();
            gridOffsetTracker.gridSize.calls.reset();
            gridTableManager.addSelectedStyleToCoordinates.calls.reset();
            gridTableManager.removeSelectedStyleFromCoordinates.calls.reset();
            gridTableManager.calculateSelected.calls.reset();
            canvasLineDrawer.drawLine.calls.reset();
            targetCalculator.calculateTargetCell.calls.reset();
        });

        var context = {
            clearRect: jasmine.createSpy('clearRect'),
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
                targetCalculator: targetCalculator,
                fontSizeManager: fontSizeManager,
                foundWordsCanvasManager: foundWordsCanvasManager,
                canvasLineDrawer: canvasLineDrawer,
                featureDescriber: featureDescriber,
                jtbGameCache: jtbGameCache,
                jtbBootstrapGameActions: jtbBootstrapActions,
                gameAnimations: gameAnimations,
                jtbPlayerService: jtbPlayerService
            });
        }));

        afterEach(function () {
            elementSpy.and.callThrough();
        });

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
            expect(PlayCtrl.playerColors).toEqual({'md51': '#C1D37F'});
            expect(PlayCtrl.playerFontColors).toEqual({'md51': 'black'});
            expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3, PlayCtrl.playerColors);
            expect(PlayCtrl.description).toEqual([]);

            var expectedDescription = {a: 1, b: '32'};
            featurePromise.resolve(expectedDescription);
            $scope.$apply();
            expect(PlayCtrl.description).toEqual(expectedDescription);
        });

        describe('multiplayer settings', function () {
            beforeEach(function () {
                expectedGame.players = {'md51': 'P1', 'md52': 'P2', 'md53': 'P3', 'md54': 'P4', 'md55': 'P5'};
            });
            it('initializes colors for multi-player game', function () {
                foundWordsCanvasManager.updateForGame.calls.reset();
                $scope.$broadcast('gameUpdated', expectedGame);
                $scope.$apply();
                expect(PlayCtrl.playerColors).toEqual({
                    'md51': '#C1D37F',
                    'md52': '#BE5B63',
                    'md53': '#F0E2A3',
                    'md54': '#FFFFFF',
                    'md55': '#A9A9A9'
                });
                expect(PlayCtrl.playerFontColors).toEqual({
                    'md51': 'black',
                    'md52': 'white',
                    'md53': 'black',
                    'md54': 'black',
                    'md55': 'white'
                });
                expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3, PlayCtrl.playerColors);
            });
        });

        it('zoom in', function () {
            PlayCtrl.zoomIn(4);
            expect(PlayCtrl.fontSize).toEqual({original: 4});
        });

        it('zoom out', function () {
            PlayCtrl.zoomOut(2);
            expect(PlayCtrl.fontSize).toEqual({original: 2});
        });

        it('mouse move when we are not tracking does nothing', function () {
            PlayCtrl.onMouseMove(undefined);
        });

        it('game update on different game does nothing', function () {
            var newGame = angular.copy(expectedGame);
            newGame.id += '32';
            newGame.gamePhase = 'NotPlaying';
            $scope.$broadcast('gameUpdated', newGame, newGame);
            $scope.$apply();
            expect(PlayCtrl.showQuit).toBeTruthy();
            expect(PlayCtrl.showRematch).not.toBeTruthy();
        });

        it('game update on same game updates', function () {
            var original = angular.copy(expectedGame);
            expectedGame.gamePhase = 'NotPlaying';
            $scope.$broadcast('gameUpdated', original, expectedGame);
            $scope.$apply();
            expect(PlayCtrl.showQuit).not.toBeTruthy();
            expect(PlayCtrl.showRematch).not.toBeTruthy();
            expect(gameAnimations.animateGameUpdate).toHaveBeenCalledWith(PlayCtrl, original, expectedGame);
        });

        function resetExpectations(nextReturnValueFromCalculateSelected) {
            gridTableManager.calculateSelected.calls.reset();
            gridTableManager.calculateSelected.and.returnValue(nextReturnValueFromCalculateSelected);
            gridTableManager.removeSelectedStyleFromCoordinates.calls.reset();
            gridTableManager.addSelectedStyleToCoordinates.calls.reset();
            canvasLineDrawer.drawLine.calls.reset();
            context.clearRect.calls.reset();
            gridTableManager.calculateSelected.calls.reset();
            targetCalculator.calculateTargetCell.calls.reset();
            if (angular.isDefined(nextReturnValueFromCalculateSelected)) {
                targetCalculator.calculateTargetCell.and.returnValue(
                    nextReturnValueFromCalculateSelected.selectedCoordinates[
                    nextReturnValueFromCalculateSelected.selectedCoordinates.length - 1
                        ]);
            }
        }

        function checkNoSelectionCalls() {
            expect(jtbBootstrapActions.wrapActionOnGame).not.toHaveBeenCalled();
            expect(gridTableManager.removeSelectedStyleFromCoordinates).not.toHaveBeenCalled();
            expect(gridTableManager.addSelectedStyleToCoordinates).not.toHaveBeenCalled();
            expect(context.clearRect).not.toHaveBeenCalled();
            expect(canvasLineDrawer.drawLine).not.toHaveBeenCalled();
            expect(targetCalculator.calculateTargetCell).not.toHaveBeenCalled();
        }

        it('clicking on game starts selection', function () {
            var selectedCoordinates = [{row: 1, column: 0}];
            var selectedReturnValue = {
                selectedCoordinates: selectedCoordinates,
                originalCells: selectedCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            resetExpectations(selectedReturnValue);
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
            expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
                context,
                selectedCoordinates[0],
                selectedCoordinates[0],
                canvas.height / 4,
                canvas.width / 3,
                '#9DC4B5'
            );
        });

        it('clicking on space does not start selection', function () {
            var selectedCoordinates = [{row: 2, column: 1}];
            var selectedReturnValue = {
                selectedCoordinates: selectedCoordinates,
                originalCells: selectedCoordinates,
                wordForward: ' ',
                wordReversed: ' '
            };
            resetExpectations(selectedReturnValue);
            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 2;
                        }
                        if (name === 'data-ws-column') {
                            return 1;
                        }
                        throw 'error';
                    }
                }

            });
            checkNoSelectionCalls();
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
        });

        it('moving after initial click updates word', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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
            expect(targetCalculator.calculateTargetCell).not.toHaveBeenCalled();
            var moveCoordinates = [{row: 1, column: 0}, {row: 0, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DA',
                wordReversed: 'AD'
            };
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(clickCoordinates);
            expect(gridTableManager.addSelectedStyleToCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual(moveReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(moveReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(true);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
            expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
                context,
                moveCoordinates[0],
                moveCoordinates[1],
                canvas.height / 4,
                canvas.width / 3,
                '#9DC4B5'
            );
        });

        it('game update with current word selected being found unsets flags', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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
            var moveCoordinates = [{row: 1, column: 0}, {row: 0, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DA',
                wordReversed: 'AD'
            };
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(PlayCtrl.currentWordBackward).toEqual(moveReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(moveReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(true);

            resetExpectations(moveReturnValue);
            expectedGame.wordsToFind = [];
            $scope.$broadcast('gameUpdated', expectedGame);
            $scope.$apply();
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(gridTableManager.addSelectedStyleToCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual(moveReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(moveReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
            expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
                context,
                moveCoordinates[0],
                moveCoordinates[1],
                canvas.height / 4,
                canvas.width / 3,
                '#9DC4B5'
            );
        });

        it('game update to non playing phase clears selection and flags', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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
            var moveCoordinates = [{row: 1, column: 0}, {row: 0, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DA',
                wordReversed: 'AD'
            };
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(PlayCtrl.currentWordBackward).toEqual(moveReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(moveReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(true);

            resetExpectations(moveReturnValue);
            expectedGame.gamePhase = 'RoundOver';
            $scope.$broadcast('gameUpdated', expectedGame);
            $scope.$apply();
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);

            //  checked above
            gridTableManager.removeSelectedStyleFromCoordinates.calls.reset();
            context.clearRect.calls.reset();
            checkNoSelectionCalls();
        });

        it('moving in and out of table temporarily disables and enables tracking', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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

            PlayCtrl.onMouseExitsTable();

            var moveCoordinates = [{row: 1, column: 0}, {row: 0, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DA',
                wordReversed: 'AD'
            };
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(PlayCtrl.currentWordBackward).toEqual(clickReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(clickReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            checkNoSelectionCalls();

            PlayCtrl.onMouseEntersTable();
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });

            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(clickCoordinates);
            expect(gridTableManager.addSelectedStyleToCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual(moveReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(moveReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(true);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
            expect(canvasLineDrawer.drawLine).toHaveBeenCalledWith(
                context,
                moveCoordinates[0],
                moveCoordinates[1],
                canvas.height / 4,
                canvas.width / 3,
                '#9DC4B5'
            );
        });

        it('grid offset change while word selected clears it', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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
            var moveCoordinates = [{row: 1, column: 0}, {row: 0, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DA',
                wordReversed: 'AD'
            };
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });

            resetExpectations(undefined);
            $scope.$broadcast('GridOffsetsChanged');
            $scope.$apply();
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);

            resetExpectations(undefined);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            checkNoSelectionCalls();
        });

        it('moving but without changing cell does nothing', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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
            var moveCoordinates = [{row: 1, column: 0}, {row: 0, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DA',
                wordReversed: 'AD'
            };
            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });

            resetExpectations(moveReturnValue);
            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(PlayCtrl.currentWordBackward).toEqual(moveReturnValue.wordReversed);
            expect(PlayCtrl.currentWordForward).toEqual(moveReturnValue.wordForward);
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(true);
            expect(targetCalculator.calculateTargetCell.calls.reset());
            checkNoSelectionCalls();
        });

        it('clicking after not highlighting word clears selection', function () {
            var clickCoordinates = [{row: 1, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

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
            var moveCoordinates = [{row: 1, column: 0}, {row: 2, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCells: moveCoordinates,
                wordForward: 'DG',
                wordReversed: 'DG'
            };
            resetExpectations(moveReturnValue);

            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 2;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });

            resetExpectations(undefined);

            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 2;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        });

        it('clicking after highlighting word submits selection', function () {
            var clickCoordinates = [{row: 2, column: 0}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'D',
                wordReversed: 'D'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 2;
                        }
                        if (name === 'data-ws-column') {
                            return 0;
                        }
                        throw 'error';
                    }
                }
            });
            var moveCoordinates = [{row: 2, column: 0}, {row: 1, column: 1}, {row: 0, column: 2}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCoordinates: moveCoordinates,
                wordForward: 'DEC',
                wordReversed: 'CED'
            };
            resetExpectations(moveReturnValue);

            PlayCtrl.onMouseMove({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 2;
                        }
                        throw 'error';
                    }
                }
            });

            resetExpectations(moveReturnValue);

            var updatedGame = angular.copy(expectedGame);
            updatedGame.wordsToFind = [];
            $http.expectPUT(playerBaseURL + '/game/' + gameID + '/find', [
                {row: 2, column: 0},
                {row: -1, column: 1},
                {row: -1, column: 1}
            ]).respond(updatedGame);

            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 2;
                        }
                        throw 'error';
                    }
                }
            });
            expect(jtbBootstrapActions.wrapActionOnGame).toHaveBeenCalled();
            $http.flush();
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        });

        it('clicking after highlighting backward word submits selection, including offset adjustment', function () {
            var clickCoordinates = [{row: 1, column: 2}];
            var clickReturnValue = {
                selectedCoordinates: clickCoordinates,
                originalCells: clickCoordinates,
                wordForward: 'F',
                wordReversed: 'F'
            };
            gridTableManager.calculateSelected.and.returnValue(clickReturnValue);

            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 1;
                        }
                        if (name === 'data-ws-column') {
                            return 2;
                        }
                        throw 'error';
                    }
                }
            });
            var moveCoordinates = [{row: 1, column: 2}, {row: 1, column: 1}, {row: 1, column: 0}];
            var moveReturnValue = {
                selectedCoordinates: moveCoordinates,
                originalCoordinates: [{row: 2, column: 2}, {row: 2, column: 1}, {row: 2, column: 0}],
                wordForward: 'FED',
                wordReversed: 'DEF'
            };
            resetExpectations(moveReturnValue);

            PlayCtrl.onMouseMove({
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

            resetExpectations(moveReturnValue);

            var updatedGame = angular.copy(expectedGame);
            updatedGame.wordsToFind = [];
            $http.expectPUT(playerBaseURL + '/game/' + gameID + '/find', [
                {row: 2, column: 2},
                {row: 0, column: -1},
                {row: 0, column: -1}
            ]).respond(updatedGame);

            PlayCtrl.onMouseClick({
                currentTarget: {
                    getAttribute: function (name) {
                        if (name === 'data-ws-row') {
                            return 0;
                        }
                        if (name === 'data-ws-column') {
                            return 2;
                        }
                        throw 'error';
                    }
                }
            });
            expect(jtbBootstrapActions.wrapActionOnGame).toHaveBeenCalled();
            $http.flush();
            expect(gridTableManager.removeSelectedStyleFromCoordinates).toHaveBeenCalledWith(moveCoordinates);
            expect(PlayCtrl.currentWordBackward).toEqual('');
            expect(PlayCtrl.currentWordForward).toEqual('');
            expect(PlayCtrl.backwardIsWord).toEqual(false);
            expect(PlayCtrl.forwardIsWord).toEqual(false);
            expect(context.clearRect).toHaveBeenCalledWith(0, 0, canvas.width, canvas.height);
        });

        describe('game that is over, but rematch available', function () {
            beforeEach(inject(function ($controller) {
                expectedGame.gamePhase = 'RoundOver';
                PlayCtrl = $controller('PlayCtrl', {
                    $scope: $scope,
                    $routeParams: $routeParams,
                    gridOffsetTracker: gridOffsetTracker,
                    gridTableManager: gridTableManager,
                    targetCalculator: targetCalculator,
                    fontSizeManager: fontSizeManager,
                    foundWordsCanvasManager: foundWordsCanvasManager,
                    canvasLineDrawer: canvasLineDrawer,
                    featureDescriber: featureDescriber,
                    jtbGameCache: jtbGameCache,
                    jtbBootstrapGameActions: jtbBootstrapActions,
                    gameAnimations: gameAnimations,
                    jtbPlayerService: jtbPlayerService
                });
            }));

            it('initializes round over game', function () {
                expect(PlayCtrl.showQuit).toEqual(false);
                expect(PlayCtrl.showRematch).toEqual(true);
                expect(gridOffsetTracker.reset).toHaveBeenCalled();
                expect(gridOffsetTracker.gridSize).toHaveBeenCalledWith(4, 3);
                expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3, PlayCtrl.playerColors);
            });

            it('mouse click does nothing in this case', function () {
                PlayCtrl.onMouseClick(undefined);
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
                    targetCalculator: targetCalculator,
                    fontSizeManager: fontSizeManager,
                    foundWordsCanvasManager: foundWordsCanvasManager,
                    canvasLineDrawer: canvasLineDrawer,
                    featureDescriber: featureDescriber,
                    jtbGameCache: jtbGameCache,
                    jtbBootstrapGameActions: jtbBootstrapActions,
                    gameAnimations: gameAnimations,
                    jtbPlayerService: jtbPlayerService
                });
            }));

            it('initializes older game', function () {
                expect(PlayCtrl.showQuit).toEqual(false);
                expect(PlayCtrl.showRematch).toEqual(false);
                expect(gridOffsetTracker.reset).toHaveBeenCalled();
                expect(gridOffsetTracker.gridSize).toHaveBeenCalledWith(4, 3);
                expect(foundWordsCanvasManager.updateForGame).toHaveBeenCalledWith(expectedGame, 4, 3, PlayCtrl.playerColors);
            });

            it('mouse click does nothing in this case', function () {
                PlayCtrl.onMouseClick(undefined);
            });
        });
    }
);
