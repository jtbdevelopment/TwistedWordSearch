'use strict';

angular.module('twsUI.services').factory('fontSizeManager',
    [
        function () {
            //  TODO - make saveable/loadable
            var userFontSize = 11;

            return {
                fontSizeStyle: function() {
                     return {'font-size': userFontSize};
                },
                increaseFontSize: function(amount) {
                    userFontSize += amount;
                    return this.fontSizeStyle();
                },
                decreaseFontSize: function(amount) {
                    userFontSize -= amount;
                    return this.fontSizeStyle();
                }
            };
        }
    ]
);