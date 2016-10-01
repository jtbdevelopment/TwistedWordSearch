'use strict';

describe('Directive: describeGame', function () {
    beforeEach(module('twsUI'));

    var $rootScope, $compile, scope, $document, $http;
    var testElement;

    function createTestElement() {
        $http.expectGET('views/describeGame.html').respond('<div class="game-features"><div ng-repeat="feature in features" class="game-feature" uib-tooltip="{{feature.tooltip}}"><span class="{{feature.icon}}" ng-hide="angular.isUndefined(feature.icon)"></span><span ng-hide="angular.isUndefined(feature.text)">{{feature.text}}</span></div></div>');
        var element = angular.element('<describe-game features="testFeatures"></div>');
        var compiled = $compile(element)(scope);
        $http.flush();
        scope.$digest();
        return compiled;
    }

    beforeEach(inject(function (_$compile_, _$rootScope_, _$document_, $httpBackend) {
        $rootScope = _$rootScope_;
        $http = $httpBackend;
        scope = $rootScope.$new();
        scope.testFeatures = [
            {icon: 'icon1', tooltip: 'tt1'},
            {icon: 'icon2', text: 't2', tooltip: 'tt2'},
            {text: 't3', tooltip: 'tt3'}
        ];
        $compile = _$compile_;
        $document = _$document_;

        testElement = createTestElement();

    }));

    it('initializes', function () {
        expect(testElement.html()).toEqual('<div class="game-features"><!-- ngRepeat: feature in features --><div ng-repeat="feature in features" class="game-feature ng-scope" uib-tooltip="tt1"><span class="icon1" ng-hide="angular.isUndefined(feature.icon)"></span><span ng-hide="angular.isUndefined(feature.text)" class="ng-binding"></span></div><!-- end ngRepeat: feature in features --><div ng-repeat="feature in features" class="game-feature ng-scope" uib-tooltip="tt2"><span class="icon2" ng-hide="angular.isUndefined(feature.icon)"></span><span ng-hide="angular.isUndefined(feature.text)" class="ng-binding">t2</span></div><!-- end ngRepeat: feature in features --><div ng-repeat="feature in features" class="game-feature ng-scope" uib-tooltip="tt3"><span class="" ng-hide="angular.isUndefined(feature.icon)"></span><span ng-hide="angular.isUndefined(feature.text)" class="ng-binding">t3</span></div><!-- end ngRepeat: feature in features --></div>');
    });

});
