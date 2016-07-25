'use strict';

describe('Controller: CreateGameCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var CreateGameCtrl;
    var $http, $q, $rootScope;
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

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $httpBackend, _$q_, _$rootScope_) {
        $http = $httpBackend;
        $q = _$q_;
        $rootScope = _$rootScope_;
        CreateGameCtrl = $controller('CreateGameCtrl', {
            $http: $httpBackend,
            jtbPlayerService: jtbPlayerService,
            jtbGameCache: jtbGameCache,
            jtbGameFeatureService: jtbGameFeatures
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
            'Group1': [{
                'feature': 'Feature1',
                'label': 'FeatureLabel1',
                'description': 'Description 1',
                'options': [{
                    'feature': 'Feature1Option1',
                    'label': 'Feature1OptionLabel1',
                    'description': 'Description Option 1/1'
                }, {
                    'feature': 'Feature1Option2',
                    'label': 'Feature1OptionLabel2',
                    'description': 'Description Option 1/2'
                }]
            }, {
                'feature': 'Feature2',
                'label': 'FeatureLabel2',
                'description': 'Description 2',
                'options': [{
                    'feature': 'Feature2Option1',
                    'label': 'Feature2OptionLabel1',
                    'description': 'Description Option 2/1'
                }, {
                    'feature': 'Feature2Option2',
                    'label': 'Feature2OptionLabel2',
                    'description': 'Description Option 2/2'
                }, {
                    'feature': 'Feature2Option3',
                    'label': 'Feature2OptionLabel3',
                    'description': 'Description Option 2/3'
                }]
            }],
            'Group2': [{
                'feature': 'Feature3',
                'label': 'FeatureLabel3',
                'description': 'Description 3',
                'options': [{
                    'feature': 'Feature3Option1',
                    'label': 'Feature3OptionLabel1',
                    'description': 'Description Option 3/1'
                }, {
                    'feature': 'Feature3Option2',
                    'label': 'Feature3OptionLabel2',
                    'description': 'Description Option 3/2'
                }]
            }]
        });
        expect(CreateGameCtrl.choices).toEqual({
            'Feature1': 'Feature1Option1',
            'Feature2': 'Feature2Option1',
            'Feature3': 'Feature3Option1'
        });
    });
});
