'use strict';

describe('Controller: CreateGameCtrl', function () {
    beforeEach(module('twsUI'));

    var CreateGameCtrl;
    var $q, $rootScope;
    var playerSource;
    var longName = 'a long name';
    var jtbPlayerService = {
        currentPlayer: function () {
            return {source: playerSource};
        },

        initializeFriendsForController: jasmine.createSpy('initializeFriendsForController')
    };

    var featurePromise;
    var standardFeatures = [
        {
            feature: {
                groupType: 'Group1',
                feature: 'Feature1',
                label: 'FeatureLabel1',
                description: 'Description 1'
            },
            options: [
                {
                    feature: 'Feature1Option1',
                    label: 'Feature1OptionLabel1',
                    description: 'Description Option 1/1'
                },
                {
                    feature: 'Feature1Option2',
                    label: 'Feature1OptionLabel2',
                    description: 'Description Option 1/2'
                }
            ]
        },
        {
            feature: {
                groupType: 'Group1',
                feature: 'Feature2',
                label: 'FeatureLabel2',
                description: 'Description 2'
            },
            options: [
                {
                    feature: 'Feature2Option1',
                    label: 'Feature2OptionLabel1',
                    description: 'Description Option 2/1'
                },
                {
                    feature: 'Feature2Option2',
                    label: 'Feature2OptionLabel2',
                    description: 'Description Option 2/2'
                },
                {
                    feature: 'Feature2Option3',
                    label: 'Feature2OptionLabel3',
                    description: 'Description Option 2/3'
                }
            ]
        },
        {
            feature: {
                groupType: 'Group2',
                feature: 'Feature3',
                label: 'FeatureLabel3',
                description: 'Description 3'
            },
            options: [
                {
                    feature: 'Feature3Option1',
                    label: 'Feature3OptionLabel1',
                    description: 'Description Option 3/1'
                },
                {
                    feature: 'Feature3Option2',
                    label: 'Feature3OptionLabel2',
                    description: 'Description Option 3/2'
                }
            ]
        }
    ];

    var modalOpened, expectedFriends;
    var uibModal = {
        open: function (params) {
            expect(params.controller).toEqual('CoreBootstrapInviteCtrl');
            expect(params.templateUrl).toEqual('views/core-bs/friends/invite-friends.html');
            expect(params.controllerAs).toEqual('invite');
            expect(params.size).toEqual('lg');
            expect(params.resolve.invitableFriends()).toEqual(expectedFriends);
            expect(params.resolve.message()).toEqual('Come play ' + longName + ' with me!');
            modalOpened = true;
        }
    };

    var jtbGameFeatures = {
        features: function () {
            featurePromise = $q.defer();
            return featurePromise.promise;
        }
    };

    var jtbGameActions = {
        new: jasmine.createSpy('new')
    };

    var featureDescriber = {
        getIconForFeature: function (option) {
            if (option.feature === 'Feature3Option1') {
                return 'star';
            }
            if (option.feature === 'Feature2Option2') {
                return 'ship';
            }
            return undefined;
        },
        getTextForFeature: function (option) {
            if (option.feature === 'Feature3Option2') {
                return 'F3O2';
            }
        }

    };

    beforeEach(inject(function ($controller, _$q_, _$rootScope_) {
        $q = _$q_;
        modalOpened = false;
        expectedFriends = [];
        playerSource = 'facebook';
        $rootScope = _$rootScope_;
        jtbPlayerService.initializeFriendsForController.calls.reset();
        CreateGameCtrl = $controller('CreateGameCtrl', {
            jtbAppLongName: longName,
            jtbPlayerService: jtbPlayerService,
            jtbGameFeatureService: jtbGameFeatures,
            featureDescriber: featureDescriber,
            $uibModal: uibModal,
            jtbBootstrapGameActions: jtbGameActions
        });
    }));

    it('initializes to empty options and choices', function () {
        expect(CreateGameCtrl.features).toEqual([]);
        expect(CreateGameCtrl.choices).toEqual({});
        expect(CreateGameCtrl.disableCreate).toEqual(false);
        expect(CreateGameCtrl.createGameButtonText).toEqual('Create Game');
        expect(jtbPlayerService.initializeFriendsForController).toHaveBeenCalledWith(CreateGameCtrl);
    });

    it('invite friends modal', function () {
        expectedFriends = [{id: 1}, {id: 'x'}];
        CreateGameCtrl.invitableFBFriends = expectedFriends;
        CreateGameCtrl.inviteFriends();
        expect(modalOpened).toEqual(true);
    });

    it('turns feature data into usable features for ui', function () {
        featurePromise.resolve(standardFeatures);
        $rootScope.$apply();
        expect(CreateGameCtrl.features).toEqual(
            [
                {
                    group: 'Group1',
                    features: [
                        {
                            feature: 'Feature1',
                            label: 'FeatureLabel1',
                            description: 'Description 1',
                            options: [
                                {
                                    feature: 'Feature1Option1',
                                    label: 'Feature1OptionLabel1',
                                    description: 'Description Option 1/1',
                                    icon: undefined
                                },
                                {
                                    feature: 'Feature1Option2',
                                    label: 'Feature1OptionLabel2',
                                    description: 'Description Option 1/2',
                                    icon: undefined
                                }
                            ]
                        },
                        {
                            feature: 'Feature2',
                            label: 'FeatureLabel2',
                            description: 'Description 2',
                            options: [
                                {
                                    feature: 'Feature2Option1',
                                    label: 'Feature2OptionLabel1',
                                    description: 'Description Option 2/1',
                                    icon: undefined
                                },
                                {
                                    feature: 'Feature2Option2',
                                    label: 'Feature2OptionLabel2',
                                    description: 'Description Option 2/2',
                                    icon: 'ship'
                                },
                                {
                                    feature: 'Feature2Option3',
                                    label: 'Feature2OptionLabel3',
                                    description: 'Description Option 2/3',
                                    icon: undefined
                                }
                            ]
                        }
                    ]
                },
                {
                    group: 'Group2',
                    features: [
                        {
                            feature: 'Feature3',
                            label: 'FeatureLabel3',
                            description: 'Description 3',
                            options: [
                                {
                                    feature: 'Feature3Option1',
                                    label: 'Feature3OptionLabel1',
                                    description: 'Description Option 3/1',
                                    icon: 'star'
                                },
                                {
                                    feature: 'Feature3Option2',
                                    label: 'F3O2',
                                    description: 'Description Option 3/2',
                                    icon: undefined
                                }
                            ]
                        }
                    ]
                }
            ]
        );
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option1',
            'Feature2': 'Feature2Option1',
            'Feature3': 'Feature3Option1'
        });
    });

    it('submit game passes in choices and no players when none chosen', function () {
        featurePromise.resolve(standardFeatures);
        $rootScope.$apply();
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option1',
            'Feature2': 'Feature2Option1',
            'Feature3': 'Feature3Option1'
        });
        CreateGameCtrl.choices.Feature1 = 'Feature1Option2';
        CreateGameCtrl.choices.Feature2 = 'Feature2Option3';
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option2',
            'Feature2': 'Feature2Option3',
            'Feature3': 'Feature3Option1'
        });
        CreateGameCtrl.chosenFriends = [];

        CreateGameCtrl.createGame();
        expect(CreateGameCtrl.disableCreate).toEqual(true);
        expect(CreateGameCtrl.createGameButtonText).toEqual('Creating game...');
        expect(jtbGameActions.new).toHaveBeenCalledWith({
            'players': [],
            'features': ['Feature1Option2', 'Feature2Option3', 'Feature3Option1']
        });
    });

    it('submit game passes in choices and players when chosen', function () {
        featurePromise.resolve(standardFeatures);
        $rootScope.$apply();
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option1',
            'Feature2': 'Feature2Option1',
            'Feature3': 'Feature3Option1'
        });
        CreateGameCtrl.choices.Feature1 = 'Feature1Option2';
        CreateGameCtrl.choices.Feature2 = 'Feature2Option3';
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option2',
            'Feature2': 'Feature2Option3',
            'Feature3': 'Feature3Option1'
        });

        CreateGameCtrl.chosenFriends = [{md5: 'md51'}, {md5: 'md52'}];
        CreateGameCtrl.createGame();
        expect(jtbGameActions.new).toHaveBeenCalledWith({
            'players': ['md51', 'md52'],
            'features': ['Feature1Option2', 'Feature2Option3', 'Feature3Option1']
        });
    });
});
