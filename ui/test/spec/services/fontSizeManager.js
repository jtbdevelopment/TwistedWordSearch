'use strict';

describe('Service: fontSizeManager', function () {
    // load the controller's module
    beforeEach(module('twsUI.services'));
    var service;
    beforeEach(inject(function ($injector) {
        service = $injector.get('fontSizeManager');
    }));

    it('defaults to 11 font size', function() {
        expect(service.fontSizeStyle()).toEqual({'font-size': 11});
    });

    it('increases font size by amount', function() {
        expect(service.increaseFontSize(2)).toEqual({'font-size': 13});
        expect(service.fontSizeStyle()).toEqual({'font-size': 13});
    });

    it('decreases font size by amount', function() {
        expect(service.decreaseFontSize(2)).toEqual({'font-size': 9});
        expect(service.fontSizeStyle()).toEqual({'font-size': 9});
    });

    it('multiple adjustments work correctly', function() {
        expect(service.decreaseFontSize(2)).toEqual({'font-size': 9});
        expect(service.decreaseFontSize(2)).toEqual({'font-size': 7});
        expect(service.increaseFontSize(3)).toEqual({'font-size': 10});
        expect(service.fontSizeStyle()).toEqual({'font-size': 10});
    });
});

