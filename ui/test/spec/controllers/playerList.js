'use strict';

describe('Controller: PlayerListCtrl', function () {

    beforeEach(module('twsUI'));

    var expectedGameID;
    var $routeParams = {
        gameID: expectedGameID
    };
    var game;

    var jtbGameCache = {
        getGameForID: function (id) {
            expect(id).toEqual(expectedGameID);
            return game;
        }
    };
    var $scope, $rootScope, PlayerListCtrl, $q;
    var jtbBootstrapGameActions = {
        aHandler: 'X'
    };
    var expectedPlayer = {
        md5: 'md1'
    };
    var jtbPlayerService = {
        currentPlayer: function () {
            return expectedPlayer;
        }
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

    beforeEach(inject(function ($controller, _$rootScope_, _$q_) {
        game = {
            gameID: expectedGameID,
            players: {
                md1: 'Display 1',
                md2: 'Display 2',
                md3: 'Display 3',
                md4: 'Display 4',
                md5: 'Display 5'
            },
            playerImages: {
                md1: '/imgs/image1',
                md2: '/imgs/image2',
                md3: '/imgs/image3',
                md4: '/imgs/image4',
                md5: '/imgs/image5'
            },
            playerProfiles: {
                md1: '/profile/profile1',
                md2: '/profile/profile2',
                md3: '/profile/profile3',
                md4: '/profile/profile4',
                md5: '/profile/profile5'
            },
            playerStates: {
                md1: 'Pending',
                md2: 'Accepted',
                md3: 'Accepted',
                md4: 'Quit',
                md5: 'Rejected'
            }
        };
        $q = _$q_;
        $scope = _$rootScope_.$new();
        $rootScope = _$rootScope_;
        PlayerListCtrl = $controller('PlayerListCtrl', {
            $scope: $scope,
            $routeParams: $routeParams,
            jtbGameCache: jtbGameCache,
            jtbBootstrapGameActions: jtbBootstrapGameActions,
            jtbPlayerService: jtbPlayerService,
            featureDescriber: featureDescriber
        });
    }));

    it('basic initialization', function () {
        expect(PlayerListCtrl.groupGlyphicons).toEqual({
            Pending: 'question-sign',
            Accepted: 'thumbs-up',
            Declined: 'thumbs-down',
            Quit: 'flag'
        });
        expect(PlayerListCtrl.groups).toEqual(['Pending', 'Accepted', 'Rejected', 'Quit']);
        expect(PlayerListCtrl.actions).toEqual(jtbBootstrapGameActions);
        expect(PlayerListCtrl.groupCollapsed).toEqual({
            Pending: false,
            Accepted: false,
            Rejected: false,
            Quit: false
        });
        expect(PlayerListCtrl.game).toEqual(game);
        expect(PlayerListCtrl.players).toEqual({
            Pending: [Object({
                id: 'md1',
                current: true,
                displayName: 'Display 1',
                playerImage: '/imgs/image1',
                playerProfile: '/profile/profile1'
            })],
            Accepted: [Object({
                id: 'md2',
                current: false,
                displayName: 'Display 2',
                playerImage: '/imgs/image2',
                playerProfile: '/profile/profile2'
            }), Object({
                id: 'md3',
                current: false,
                displayName: 'Display 3',
                playerImage: '/imgs/image3',
                playerProfile: '/profile/profile3'
            })],
            Rejected: [Object({
                id: 'md5',
                current: false,
                displayName: 'Display 5',
                playerImage: '/imgs/image5',
                playerProfile: '/profile/profile5'
            })],
            Quit: [Object({
                id: 'md4',
                current: false,
                displayName: 'Display 4',
                playerImage: '/imgs/image4',
                playerProfile: '/profile/profile4'
            })]
        });
    });

    it('gets features from featureDescriber', function () {
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        var expectedDescription = {x: '1', t: 1};
        featurePromise.resolve(expectedDescription);
        $scope.$apply();
        expect(PlayerListCtrl.description).toEqual(expectedDescription);
    });

    it('it test show for Challenged/Pending', function () {
        game.gamePhase = 'Challenged';
        game.playerStates.md1 = 'Pending';
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(true);
        expect(PlayerListCtrl.showReject).toEqual(true);
        expect(PlayerListCtrl.showQuit).toEqual(false);
        expect(PlayerListCtrl.showRematch).toEqual(false);
    });

    it('it test show for Challenged/Accepted', function () {
        game.gamePhase = 'Challenged';
        game.playerStates.md1 = 'Accepted';
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(false);
        expect(PlayerListCtrl.showReject).toEqual(true);
        expect(PlayerListCtrl.showQuit).toEqual(false);
        expect(PlayerListCtrl.showRematch).toEqual(false);
    });

    it('it test show for Setup', function () {
        game.gamePhase = 'Setup';
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(false);
        expect(PlayerListCtrl.showReject).toEqual(false);
        expect(PlayerListCtrl.showQuit).toEqual(true);
        expect(PlayerListCtrl.showRematch).toEqual(false);
    });

    it('it test show for Playing', function () {
        game.gamePhase = 'Playing';
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(false);
        expect(PlayerListCtrl.showReject).toEqual(false);
        expect(PlayerListCtrl.showQuit).toEqual(true);
        expect(PlayerListCtrl.showRematch).toEqual(false);
    });

    it('it updates if game updates', function () {
        game.gamePhase = 'Challenged';
        game.playerStates.md1 = 'Pending';
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(true);
        expect(PlayerListCtrl.showReject).toEqual(true);
        expect(PlayerListCtrl.showQuit).toEqual(false);
        expect(PlayerListCtrl.showRematch).toEqual(false);
        var oldGame = angular.copy(game);
        game.gamePhase = 'Setup';
        $rootScope.$broadcast('gameUpdated', oldGame, oldGame);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(false);
        expect(PlayerListCtrl.showReject).toEqual(false);
        expect(PlayerListCtrl.showQuit).toEqual(true);
        expect(PlayerListCtrl.showRematch).toEqual(false);
    });

    it('it ignores if game update is for different game', function () {
        game.gamePhase = 'Challenged';
        game.playerStates.md1 = 'Pending';
        $rootScope.$broadcast('gameUpdated', game, game);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(true);
        expect(PlayerListCtrl.showReject).toEqual(true);
        expect(PlayerListCtrl.showQuit).toEqual(false);
        expect(PlayerListCtrl.showRematch).toEqual(false);
        var newGame = angular.copy(game);
        newGame.gamePhase = 'Setup';
        newGame.id = expectedGameID + 'X';
        $rootScope.$broadcast('gameUpdated', newGame, newGame);
        $rootScope.$apply();
        expect(PlayerListCtrl.showAccept).toEqual(true);
        expect(PlayerListCtrl.showReject).toEqual(true);
        expect(PlayerListCtrl.showQuit).toEqual(false);
        expect(PlayerListCtrl.showRematch).toEqual(false);
    });
});
