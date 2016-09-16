'use strict';

describe('Controller: CreateGameCtrl', function () {
    // load the controller's module
    beforeEach(module('twsUI'));

    var CreateGameCtrl;
    var $http, $q, $rootScope, $location;
    var expectedBaseURL = 'http://myserver.com/something/x';
    var jtbPlayerService = {
        currentPlayerBaseURL: function () {
            return expectedBaseURL;
        }
    };
    var jtbGameCache = {
        putUpdatedGame: jasmine.createSpy()
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

    var jtbGameFeatures = {
        features: function () {
            featurePromise = $q.defer();
            return featurePromise.promise;
        }
    };

    var jtbGameActions = {
        new: jasmine.createSpy()
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
                return 'F3O2'
            }
        }

    };
    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $httpBackend, _$q_, _$rootScope_) {
        $http = $httpBackend;
        $q = _$q_;
        $location = {
            path: jasmine.createSpy()
        };
        $rootScope = _$rootScope_;
        CreateGameCtrl = $controller('CreateGameCtrl', {
            $location: $location,
            jtbPlayerService: jtbPlayerService,
            jtbGameCache: jtbGameCache,
            jtbGameFeatureService: jtbGameFeatures,
            featureDescriber: featureDescriber,
            jtbBootstrapGameActions: jtbGameActions
        });
    }));

    it('initializes to empty options and choices', function () {
        expect(CreateGameCtrl.features).toEqual({});
        expect(CreateGameCtrl.choices).toEqual({});
    });

    it('turns feature data into usable features for ui', function () {
        featurePromise.resolve(standardFeatures);
        $rootScope.$apply();
        expect(CreateGameCtrl.features).toEqual({
            Group1: [Object({
                feature: 'Feature1',
                label: 'FeatureLabel1',
                description: 'Description 1',
                options: [Object({
                    feature: 'Feature1Option1',
                    label: 'Feature1OptionLabel1',
                    description: 'Description Option 1/1',
                    icon: undefined
                }), Object({
                    feature: 'Feature1Option2',
                    label: 'Feature1OptionLabel2',
                    description: 'Description Option 1/2',
                    icon: undefined
                })]
            }), Object({
                feature: 'Feature2',
                label: 'FeatureLabel2',
                description: 'Description 2',
                options: [Object({
                    feature: 'Feature2Option1',
                    label: 'Feature2OptionLabel1',
                    description: 'Description Option 2/1',
                    icon: undefined
                }), Object({
                    feature: 'Feature2Option2',
                    label: 'Feature2OptionLabel2',
                    description: 'Description Option 2/2',
                    icon: 'ship'
                }), Object({
                    feature: 'Feature2Option3',
                    label: 'Feature2OptionLabel3',
                    description: 'Description Option 2/3',
                    icon: undefined
                })]
            })],
            Group2: [Object({
                feature: 'Feature3',
                label: 'FeatureLabel3',
                description: 'Description 3',
                options: [Object({
                    feature: 'Feature3Option1',
                    label: 'Feature3OptionLabel1',
                    description: 'Description Option 3/1',
                    icon: 'star'
                }), Object({
                    feature: 'Feature3Option2',
                    label: 'F3O2',
                    description: 'Description Option 3/2',
                    icon: undefined
                })]
            })]
        });
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option1',
            'Feature2': 'Feature2Option1',
            'Feature3': 'Feature3Option1'
        });
    });

    it('submit game passes in choices and players', function () {
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

        CreateGameCtrl.createGame();
        expect(jtbGameActions.new).toHaveBeenCalledWith({
            'players': [],
            'features': ['Feature1Option2', 'Feature2Option3', 'Feature3Option1']
        });
    });
});
