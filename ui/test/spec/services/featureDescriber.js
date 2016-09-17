'use strict';

describe('Service: featureDescriber', function () {
    beforeEach(module('twsUI.services'));

    var $q, featurePromise;
    var jtbGameFeatureService = {
        features: function () {
            featurePromise = $q.defer();
            return featurePromise.promise;
        }
    };
    beforeEach(module(function ($provide) {
        $provide.factory('jtbGameFeatureService', [function () {
            return jtbGameFeatureService;
        }]);
    }));

    var standardFeatures = [
        {
            feature: {
                groupType: '',
                feature: 'Grid',
                label: 'Grid',
                description: 'Grid'
            },
            options: [
                {
                    feature: 'Squarex10',
                    label: 'Square x10',
                    description: 'A 10x10 Grid',
                    group: 'Grid'
                },
                {
                    feature: 'Circlex11',
                    label: 'Circle x11',
                    description: 'A circle with 11 diameter',
                    group: 'Grid'
                },
                {
                    feature: 'Pyramidx15',
                    label: 'Pyramid x11',
                    description: 'x15',
                    group: 'Grid'
                },
                {
                    feature: 'Diamondx9',
                    label: 'Diamond x9',
                    description: 'D 9',
                    group: 'Grid'
                }
            ]
        },
        {
            feature: {
                groupType: '',
                feature: 'WordDifficulty',
                label: 'FeatureLabel2',
                description: 'Description 2'
            },
            options: [
                {
                    feature: 'WDO1',
                    label: 'Option1',
                    group: 'WordDifficulty'
                },
                {
                    feature: 'WD02',
                    label: 'AnOption',
                    group: 'WordDifficulty'
                }
            ]
        },
        {
            feature: {
                groupType: '',
                feature: 'FillDifficulty',
                label: 'FeatureLabel3',
                description: 'Description 3'
            },
            options: [
                {
                    feature: 'FDO1',
                    label: 'Label 1',
                    description: 'Description Option 3/1',
                    group: 'FillDifficulty'
                },
                {
                    feature: 'FDO2',
                    label: 'FD Label 2',
                    description: 'Description Option 3/2',
                    group: 'FillDifficulty'
                }
            ]
        },
        {
            feature: {
                groupType: '',
                feature: 'WordWrap',
                label: 'Wrap',
                description: 'Description 3'
            },
            options: [
                {
                    feature: 'WWYes',
                    label: 'Yes',
                    description: 'Description Option 3/1',
                    group: 'WordWrap'
                },
                {
                    feature: 'WWNo',
                    label: 'No',
                    description: 'Description Option 3/2',
                    group: 'WordWrap'
                }
            ]
        }
    ];

    var service;
    beforeEach(inject(function ($injector, _$q_) {
        $q = _$q_;
        service = $injector.get('featureDescriber');
    }));

    it('get icon for grid', function() {
        expect(service.getIconForFeature(standardFeatures[0].options[0])).toEqual('icon-square');
        expect(service.getIconForFeature(standardFeatures[0].options[1])).toEqual('icon-circle');
        expect(service.getIconForFeature(standardFeatures[0].options[2])).toEqual('icon-pyramid');
        expect(service.getIconForFeature(standardFeatures[0].options[3])).toEqual('icon-diamond');
        expect(service.getIconForFeature({group: 'Grid'})).toBeUndefined();
        expect(service.getIconForFeature({group: 'Grid', label: 'space x30'})).toBeUndefined();
    });

    it('get text for grid', function() {
        expect(service.getTextForFeature(standardFeatures[0].options[0])).toEqual('x10');
        expect(service.getTextForFeature(standardFeatures[0].options[1])).toEqual('x11');
        expect(service.getTextForFeature(standardFeatures[0].options[2])).toEqual('x11');
        expect(service.getTextForFeature(standardFeatures[0].options[3])).toEqual('x9');
        expect(service.getTextForFeature({group: 'Grid'})).toBeUndefined();
        expect(service.getTextForFeature({group: 'Grid', label: 'space x30'})).toEqual('x30');
        expect(service.getTextForFeature({group: 'Grid', label: 'spacex30'})).toBeUndefined();
    });

    it('get text for word wrap is undefined', function () {
        angular.forEach(standardFeatures[3].options, function (option) {
            expect(service.getTextForFeature(option)).toBeUndefined();
        });
    });

    it('get icon for word wrap', function () {
        expect(service.getIconForFeature(standardFeatures[3].options[0])).toEqual('icon-wrap');
        expect(service.getIconForFeature(standardFeatures[3].options[1])).toEqual('icon-nowrap');
    });

    it('get icon for fill difficulty wrap is undefined', function () {
        angular.forEach(standardFeatures[2].options, function (option) {
            expect(service.getIconForFeature(option)).toBeUndefined();
        });
    });

    it('get text for fill difficulty wrap is label', function () {
        angular.forEach(standardFeatures[2].options, function (option) {
            expect(service.getTextForFeature(option)).toEqual(option.label);
        });
    });

    it('get text for word difficulty is undefined', function () {
        angular.forEach(standardFeatures[1].options, function (option) {
            expect(service.getTextForFeature(option)).toBeUndefined();
        });
    });

    it('get icon for word difficulty is icon-labellowercase', function () {
        angular.forEach(standardFeatures[1].options, function (option) {
            expect(service.getIconForFeature(option)).toEqual('icon-' + option.label.toLowerCase());
        });
    });
});