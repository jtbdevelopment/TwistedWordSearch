'use strict';

describe('Controller: AdminCtrl', function () {

    // load the controller's module
    beforeEach(module('twsUI'));

    var AdminCtrl, jtbPlayerService, $http, $scope, $rootScope;
    var overridePID, realPID;
    var playerCount = 10 + '';
    var gameCount = 100 + '';
    var gamesLast24 = 11 + '';
    var gamesLast7 = 24 + '';
    var gamesLast30 = 101 + '';
    var playerCreatedLast24 = 17 + '';
    var playerCreatedLast7 = 21 + '';
    var playerCreatedLast30 = 33 + '';
    var playerLastLogin24 = 111 + '';
    var playerLastLogin7 = 22 + '';
    var playerLastLogin30 = 55 + '';
    var expectedResults = {
        totalElements: 10,
        totalPages: 5,
        number: 3,
        content: [
            {
                id: '1',
                displayName: 'dn1'
            },
            {
                id: '2',
                displayName: 'dn2'
            },
            {
                id: '3',
                displayName: 'dn2'
            }
        ]
    };

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, _$rootScope_, $httpBackend) {
        $http = $httpBackend;
        realPID = 'REAL';
        overridePID = realPID;
        $rootScope = _$rootScope_;
        $scope = $rootScope.$new();

        jtbPlayerService = {
            realPID: function () {
                return realPID;
            },
            currentID: function () {
                return overridePID;
            },
            overridePID: function (pid) {
                overridePID = pid;
            }
        };
        AdminCtrl = $controller('AdminCtrl', {
            jtbPlayerService: jtbPlayerService,
            $scope: $scope
        });
    }));

    beforeEach(function () {
        var time = Math.floor((new Date()).getTime() / 1000);
        var dayInSeconds = 86400;
        var base;
        var time24, time7, time30;

        $http.expectGET('/api/player/admin/playerCount').respond(playerCount);
        $http.expectGET('/api/player/admin/gameCount').respond(gameCount);

        base = '\/api\/player\/admin\/playersCreated\/';
        time24 = new RegExp(base + (time - (dayInSeconds) + '').slice(0, -1) + '[0-9]');
        time7 = new RegExp(base + (time - (dayInSeconds * 7) + '').slice(0, -1) + '[0-9]');
        time30 = new RegExp(base + (time - (dayInSeconds * 30) + '').slice(0, -1) + '[0-9]');
        $http.expectGET(time24).respond(playerCreatedLast24);
        $http.expectGET(time7).respond(playerCreatedLast7);
        $http.expectGET(time30).respond(playerCreatedLast30);

        base = '\/api\/player\/admin\/playersLoggedIn\/';
        time24 = new RegExp(base + (time - (dayInSeconds) + '').slice(0, -1) + '[0-9]');
        time7 = new RegExp(base + (time - (dayInSeconds * 7) + '').slice(0, -1) + '[0-9]');
        time30 = new RegExp(base + (time - (dayInSeconds * 30) + '').slice(0, -1) + '[0-9]');
        $http.expectGET(time24).respond(playerLastLogin24);
        $http.expectGET(time7).respond(playerLastLogin7);
        $http.expectGET(time30).respond(playerLastLogin30);

        base = '\/api\/player\/admin\/gamesSince\/';
        time24 = new RegExp(base + (time - (dayInSeconds) + '').slice(0, -1) + '[0-9]');
        time7 = new RegExp(base + (time - (dayInSeconds * 7) + '').slice(0, -1) + '[0-9]');
        time30 = new RegExp(base + (time - (dayInSeconds * 30) + '').slice(0, -1) + '[0-9]');
        $http.expectGET(time24).respond(gamesLast24);
        $http.expectGET(time7).respond(gamesLast7);
        $http.expectGET(time30).respond(gamesLast30);

        $http.expectGET('/api/player/admin/playersLike/?pageSize=20&page=0&like=').respond(expectedResults);
    });

    it('initializes users', function () {
        expect(AdminCtrl.revertEnabled).toEqual(false);
        expect(AdminCtrl.revertText).toEqual('You are yourself.');

        expect(AdminCtrl.numberOfPages).toEqual(0);
        expect(AdminCtrl.currentPage).toEqual(1);
        expect(AdminCtrl.players).toEqual([]);
        expect(AdminCtrl.searchText).toEqual('');
        expect(AdminCtrl.pageSize).toEqual(20);
        expect(overridePID).toEqual(realPID);

        $http.flush();

        expect(AdminCtrl.totalItems).toEqual(expectedResults.totalElements);
        expect(AdminCtrl.numberOfPages).toEqual(expectedResults.totalPages);
        expect(AdminCtrl.currentPage).toEqual(expectedResults.number + 1);
        expect(AdminCtrl.players).toEqual(expectedResults.content);
        expect(AdminCtrl.searchText).toEqual('');
        expect(AdminCtrl.pageSize).toEqual(20);
    });

    it('changes pages', function () {
        $http.flush();

        expect(AdminCtrl.totalItems).toEqual(expectedResults.totalElements);
        expect(AdminCtrl.numberOfPages).toEqual(expectedResults.totalPages);
        expect(AdminCtrl.currentPage).toEqual(expectedResults.number + 1);
        expect(AdminCtrl.players).toEqual(expectedResults.content);
        expect(AdminCtrl.searchText).toEqual('');
        expect(AdminCtrl.pageSize).toEqual(20);

        AdminCtrl.searchText = 'aaa';
        AdminCtrl.currentPage = 7;
        $http.expectGET('/api/player/admin/playersLike/?pageSize=20&page=6&like=aaa').respond({
            totalElements: 0,
            totalPages: 0,
            number: 0,
            content: []
        });

        AdminCtrl.changePage();
        $http.flush();

        expect(AdminCtrl.totalItems).toEqual(0);
        expect(AdminCtrl.numberOfPages).toEqual(0);
        expect(AdminCtrl.currentPage).toEqual(1);
        expect(AdminCtrl.players).toEqual([]);
        expect(AdminCtrl.searchText).toEqual('aaa');
        expect(AdminCtrl.pageSize).toEqual(20);

    });

    it('refresh data', function () {
        $http.flush();

        expect(AdminCtrl.totalItems).toEqual(expectedResults.totalElements);
        expect(AdminCtrl.numberOfPages).toEqual(expectedResults.totalPages);
        expect(AdminCtrl.currentPage).toEqual(expectedResults.number + 1);
        expect(AdminCtrl.players).toEqual(expectedResults.content);
        expect(AdminCtrl.searchText).toEqual('');
        expect(AdminCtrl.pageSize).toEqual(20);

        AdminCtrl.searchText = 'a3';
        AdminCtrl.currentPage = 7;
        var refreshedResults = {
            totalElements: 1,
            totalPages: 1,
            number: 0,
            content: [{id: 'a3', displayName: 'a3'}]
        };
        $http.expectGET('/api/player/admin/playersLike/?pageSize=20&page=0&like=a3').respond(refreshedResults);

        AdminCtrl.refreshData();
        $http.flush();

        expect(AdminCtrl.totalItems).toEqual(1);
        expect(AdminCtrl.numberOfPages).toEqual(1);
        expect(AdminCtrl.currentPage).toEqual(1);
        expect(AdminCtrl.players).toEqual(refreshedResults.content);
        expect(AdminCtrl.searchText).toEqual('a3');
        expect(AdminCtrl.pageSize).toEqual(20);
    });

    it('initializes stats', function () {
        expect(AdminCtrl.playerCount).toEqual(0);
        expect(AdminCtrl.gameCount).toEqual(0);
        expect(AdminCtrl.playersCreated24hours).toEqual(0);
        expect(AdminCtrl.playersCreated7days).toEqual(0);
        expect(AdminCtrl.playersCreated30days).toEqual(0);
        expect(AdminCtrl.playersLastLogin24hours).toEqual(0);
        expect(AdminCtrl.playersLastLogin7days).toEqual(0);
        expect(AdminCtrl.playersLastLogin30days).toEqual(0);
        expect(AdminCtrl.gamesLast7days).toEqual(0);
        expect(AdminCtrl.gamesLast24hours).toEqual(0);
        expect(AdminCtrl.gamesLast30days).toEqual(0);

        $http.flush();
        expect(AdminCtrl.playerCount).toEqual(playerCount);
        expect(AdminCtrl.gameCount).toEqual(gameCount);
        expect(AdminCtrl.gamesLast24hours).toEqual(gamesLast24);
        expect(AdminCtrl.gamesLast7days).toEqual(gamesLast7);
        expect(AdminCtrl.gamesLast30days).toEqual(gamesLast30);
        expect(AdminCtrl.playersCreated24hours).toEqual(playerCreatedLast24);
        expect(AdminCtrl.playersCreated7days).toEqual(playerCreatedLast7);
        expect(AdminCtrl.playersCreated30days).toEqual(playerCreatedLast30);
        expect(AdminCtrl.playersLastLogin24hours).toEqual(playerLastLogin24);
        expect(AdminCtrl.playersLastLogin7days).toEqual(playerLastLogin7);
        expect(AdminCtrl.playersLastLogin30days).toEqual(playerLastLogin30);
    });

    it('changes to user', function () {
        expect(AdminCtrl.revertEnabled).toEqual(false);
        expect(AdminCtrl.revertText).toEqual('You are yourself.');

        AdminCtrl.switchToPlayer('33');
        expect(overridePID).toEqual('33');

        expect(AdminCtrl.revertEnabled).toEqual(false);
        expect(AdminCtrl.revertText).toEqual('You are yourself.');
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();

        expect(AdminCtrl.revertEnabled).toEqual(true);
        expect(AdminCtrl.revertText).toEqual('You are simulating another player.');
    });

    it('changes to user can revert back', function () {
        AdminCtrl.switchToPlayer('33');
        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();

        expect(AdminCtrl.revertEnabled).toEqual(true);
        expect(AdminCtrl.revertText).toEqual('You are simulating another player.');

        AdminCtrl.revertToNormal();
        expect(AdminCtrl.revertEnabled).toEqual(true);
        expect(AdminCtrl.revertText).toEqual('You are simulating another player.');

        $rootScope.$broadcast('playerLoaded');
        $rootScope.$apply();

        expect(AdminCtrl.revertEnabled).toEqual(false);
        expect(AdminCtrl.revertText).toEqual('You are yourself.');
    });
});

