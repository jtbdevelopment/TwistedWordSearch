'use strict';

angular.module('twsUI.services').factory('featureDescriber',
    [

        function () {

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

            return {

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
                }
            };
        }
    ]
);
