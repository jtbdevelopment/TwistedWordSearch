'use strict';

angular.module('twsUI').controller('CreateGameCtrl',
    [
        '$http', 'jtbGameFeatureService', 'jtbGameCache', 'jtbPlayerService',
        function ($http, jtbGameFeatureService, jtbGameCache, jtbPlayerService) {
            var controller = this;

            controller.features = {};
            controller.choices = {};
            jtbGameFeatureService.features().then(
                function (features) {
                    angular.forEach(features, function (feature) {
                        var group = feature.feature.groupType;
                        if (angular.isUndefined(controller.features[group])) {
                            controller.features[group] = [];
                        }

                        var newFeature = {
                            feature: feature.feature.feature,
                            label: feature.feature.label,
                            description: feature.feature.description,
                            options: []
                        };

                        angular.forEach(feature.options, function (option) {
                            newFeature.options.push({
                                feature: option.feature,
                                label: option.label,
                                description: option.description
                            });
                        });

                        controller.features[group].push(newFeature);
                        controller.choices[newFeature.feature] = newFeature.options[0].feature;
                    });
                },
                function () {
                    //  TODO
                }
            );

            controller.createGame = function () {
                //  TODO - ads
                //  TODO - multi-player
                var featureSet = [];
                angular.forEach(controller.choices, function (value) {
                    featureSet.push(value);
                });
                var playersAndFeatures = {'players': [], 'features': featureSet};
                $http.post(jtbPlayerService.currentPlayerBaseURL() + '/new', playersAndFeatures).then(
                    function (response) {
                        console.log(JSON.stringify(response.data));
                        jtbGameCache.putUpdatedGame(response.data);
                    },
                    function (error) {
                        console.log(JSON.stringify(error));
                        //  TODO
                    }
                );
            };
        }
    ]
);