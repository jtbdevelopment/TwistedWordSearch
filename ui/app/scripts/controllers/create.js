'use strict';

angular.module('twsUI').controller('CreateGameCtrl',
    [
        '$http', 'jtbGameFeatureService',
        function ($http, jtbGameFeatureService) {
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
                            options: [],
                            yesNo: false
                        };

                        angular.forEach(feature.options, function (option) {
                            newFeature.options.push({
                                feature: option.feature,
                                label: option.label,
                                description: option.description
                            });
                            if (option.feature.indexOf('Yes') >= 0) {
                                newFeature.yesNo = true;
                            }
                        });

                        controller.features[group].push(newFeature);
                        controller.choices[newFeature.feature] = newFeature.options[0].feature;
                    });
                },
                function () {
                    //  TODO
                }
            );
        }
    ]
);