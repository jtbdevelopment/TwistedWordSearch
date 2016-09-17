'use strict';

describe('Directive: gridMaster', function () {
    beforeEach(module('twsUI'));

    var $rootScope, $compile, scope, $interval, $document;
    var testElement;

    var initialHeight = 100;
    var initialWidth = 200;

    function createTestElement() {
        var element = angular.element('<div grid-master></div>');
        element.css('height', initialHeight + 'px');
        element.css('width', initialWidth + 'px');
        angular.element($document).find('body').append(element);
        var compiled = $compile(element)(scope);
        scope.$digest();
        return compiled;
    }

    beforeEach(inject(function (_$compile_, _$rootScope_, _$interval_, _$document_) {
        $rootScope = _$rootScope_;
        scope = $rootScope.$new();
        $compile = _$compile_;
        $interval = _$interval_;
        $document = _$document_;

        testElement = createTestElement();

    }));

    it('initializes', function () {
        expect(scope.gridCanvasStyle).toEqual({top: 0, left: 0, height: initialHeight, width: initialWidth});
    });

    it('picks up changes 500 millis after change', function() {
        testElement.css('width', initialWidth * 2 + 'px');
        testElement.css('height', initialHeight / 4 + 'px');
        $interval.flush(500);
        scope.$digest();
        expect(scope.gridCanvasStyle).toEqual({top: 0, left: 0, height: initialHeight / 4, width: initialWidth * 2});
    });

});
