'use strict';

angular.module('twsUI.services').factory('featureDescriber',
    ['jtbGameFeatureService', '$q',
        function (jtbGameFeatureService, $q) {


            function getTextForGrid(label) {
                if(angular.isDefined(label)) {
                    var start = label.indexOf(' x');
                    if (start >= 0) {
                        return label.substring(start + 1);
                    }
                }
                return undefined;
            }

            function getIconForGrid(label) {
                if(angular.isDefined(label)) {
                    if (label.indexOf('Pyramid') === 0) {
                        return 'icon-pyramid';
                    }

                    if (label.indexOf('Circle') === 0) {
                        return 'icon-circle';
                    }

                    if (label.indexOf('Diamond') === 0) {
                        return 'icon-diamond';
                    }

                    if (label.indexOf('Square') === 0) {
                        return 'icon-square';
                    }
                }
                return undefined;
            }

            function getIconForWordWrap(label) {
                if (label === 'Yes') {
                    return 'icon-wrap';
                }
                return 'icon-nowrap';
            }

            function getIconForWordDifficulty(label) {
                return 'icon-' + label.toLowerCase();
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
                        case 'FillDifficulty':
                            return feature.label;
                        default:
                            return undefined;
                    }
                },

                getIconForFeature: function (feature) {
                    switch (feature.group) {
                        case 'Grid':
                            return getIconForGrid(feature.label);
                        case 'WordWrap':
                            return getIconForWordWrap(feature.label);
                        case 'WordDifficulty':
                            return getIconForWordDifficulty(feature.label);
                        default:
                            return undefined;
                    }
                },

                getShortDescriptionForGame: function (game) {
                    var defer = $q.defer();
                    initialize().then(function () {
                        var features = angular.copy(game.features);
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

                        var describe = [];
                        angular.forEach(features, function (feature) {
                            describe.push({
                                icon: service.getIconForFeature(map[feature]),
                                text: service.getTextForFeature(map[feature])
                            });
                        });
                        defer.resolve(describe);
                    });
                    return defer.promise;
                }
            };
            return service;
        }
    ]
);

