'use strict';

angular.module('twsUI').controller('CreateGameCtrl',
    [
        '$location', '$http', 'jtbGameFeatureService', 'jtbGameCache', 'jtbPlayerService', 'featureDescriber',
        function ($location, $http, jtbGameFeatureService, jtbGameCache, jtbPlayerService, featureDescriber) {
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
                            var item = {
                                feature: option.feature,
                                label: option.label,
                                description: option.description,
                                icon: ''
                            };
                            var icon = featureDescriber.getIconForFeature(option);
                            if (angular.isDefined(icon)) {
                                item.icon = icon;
                            }
                            var text = featureDescriber.getTextForFeature(option);
                            if (angular.isDefined(text)) {
                                item.label = text;
                            }

                            newFeature.options.push(item);
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
                //  TODO - invite friends
                var featureSet = [];
                angular.forEach(controller.choices, function (value) {
                    featureSet.push(value);
                });
                var playersAndFeatures = {'players': [], 'features': featureSet};
                $http.post(jtbPlayerService.currentPlayerBaseURL() + '/new', playersAndFeatures).then(
                    function (response) {
                        var game = response.data;
                        jtbGameCache.putUpdatedGame(game);
                        $location.path('/game/' + game.gamePhase.toLowerCase() + '/' + game.id);
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