'use strict';

angular.module('twsUI.services').factory('featureDescriber',
    ['jtbGameFeatureService', '$q',
        function (jtbGameFeatureService, $q) {

            function getTextForGrid(feature) {
                var start = feature.indexOf(' x');
                if (start >= 0) {
                    return feature.substring(start + 1);
                }
                return undefined;
            }

            function getIconForGrid(feature) {
                if (feature.startsWith('Pyramid')) {
                    return 'icon-pyramid';
                }

                if (feature.startsWith('Circle')) {
                    return 'icon-circle';
                }

                if (feature.startsWith('Diamond')) {
                    return 'icon-diamond';
                }

                if (feature.startsWith('Square')) {
                    return 'icon-square';
                }
                return undefined;
            }

            var map = {};
            var sortedFeatures = [];

            function initialize() {
                var loaded = $q.defer();
                if (sortedFeatures.length > 0) {
                    loaded.resolve();
                } else {
                    jtbGameFeatureService.features().then(
                        function (features) {
                            angular.forEach(features, function (feature) {
                                map[feature.feature] = feature.feature;
                                angular.forEach(feature.options, function (option) {
                                    sortedFeatures.push(option.feature);
                                    map[option.feature] = option;
                                });
                            });
                            console.log(JSON.stringify(sortedFeatures));
                            console.log(JSON.stringify(map));
                            loaded.resolve();
                        },
                        function () {
                            loaded.reject();
                        });
                }
                return loaded.promise;
            }

            var service = {

                getTextForFeature: function (feature) {
                    switch (feature.group) {
                        case 'Grid':
                            return getTextForGrid(feature.label);
                        default:
                            return feature.label;
                    }
                },

                getIconForFeature: function (feature) {
                    switch (feature.group) {
                        case 'Grid':
                            return getIconForGrid(feature.label);
                        default:
                            return undefined;
                    }
                },

                getShortDescriptionForGame: function (game) {
                    var defer = $q.defer();
                    initialize().then(function () {
                        var features = angular.copy(game.features);
                        console.log(JSON.stringify(features));
                        features.sort(function (a, b) {
                            var ai = sortedFeatures.indexOf(a);
                            var bi = sortedFeatures.indexOf(b);
                            if (ai < bi) {
                                return -1;
                            }
                            if (ai > bi) {
                                return 1;
                            }
                            return 0;
                        });
                        console.log(JSON.stringify(features));

                        var describe = [];
                        angular.forEach(features, function (feature) {
                            describe.push({
                                icon: service.getIconForFeature(map[feature]),
                                text: service.getTextForFeature(map[feature])
                            });
                        });
                        console.log(JSON.stringify(describe));
                        defer.resolve(describe);
                    });
                    return defer.promise;
                }
            };
            return service;
        }
    ]
);
