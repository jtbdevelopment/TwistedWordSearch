'use strict';

angular.module('twsUI.services').factory('gridClassifier',
    [

        function () {
            return {
                getSizeForGrid: function (feature) {
                    var start = feature.indexOf(' x');
                    if (start >= 0) {
                        return feature.substring(start + 1);
                    }
                    return undefined;
                },
                getIconForGrid: function (feature) {
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
            };
        }
    ]
);
