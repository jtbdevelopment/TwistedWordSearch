'use strict';

//  TODO - check start base for change to features
angular.module('twsUI').controller('CreateGameCtrl',
    [
        '$location', '$http', 'jtbGameFeatureService', 'jtbGameCache', 'jtbPlayerService', 'featureDescriber', 'jtbBootstrapGameActions', '$uibModal',
        function ($location, $http, jtbGameFeatureService, jtbGameCache, jtbPlayerService, featureDescriber, jtbBootstrapGameActions, $uibModal) {
            var controller = this;

            controller.features = [];
            controller.choices = {};

            controller.chosenFriends = [];
            controller.invitableFBFriends = [];
            controller.friends = [];
            controller.createGameButtonText = 'Create Game';
            controller.disableCreate = false;

            //  TODO - should this be service?  moved to starter base
            jtbPlayerService.currentPlayerFriends().then(function (data) {
                angular.forEach(data.maskedFriends, function (displayName, hash) {
                    var friend = {
                        md5: hash,
                        displayName: displayName
                    };
                    controller.friends.push(friend);
                });
                if (jtbPlayerService.currentPlayer().source === 'facebook') {
                    angular.forEach(data.invitableFriends, function (friend) {
                        var invite = {
                            id: friend.id,
                            name: friend.name
                        };
                        if (angular.isDefined(friend.picture) && angular.isDefined(friend.picture.url)) {
                            invite.url = friend.picture.url;
                        }
                        controller.invitableFBFriends.push(invite);
                    });
                }
            });

            jtbGameFeatureService.features().then(
                function (features) {
                    var trackingGroup = {};
                    angular.forEach(features, function (feature) {
                        var group = feature.feature.groupType;
                        if (angular.isUndefined(trackingGroup[group])) {
                            var groupDetails = {group: group, features: []};
                            trackingGroup[group] = groupDetails;
                            controller.features.push(groupDetails);
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
                                icon: undefined
                            };
                            item.icon = featureDescriber.getIconForFeature(option);
                            var text = featureDescriber.getTextForFeature(option);
                            if (angular.isDefined(text)) {
                                item.label = text;
                            }

                            newFeature.options.push(item);
                        });

                        trackingGroup[group].features.push(newFeature);
                        controller.choices[newFeature.feature] = newFeature.options[0].feature;
                    });
                },
                function () {
                    //  TODO
                }
            );

            //  TODO - move to starter base?  create common
            controller.inviteFriends = function () {
                $uibModal.open({
                    templateUrl: 'views/inviteDialog.html',
                    controller: 'CoreBootstrapInviteCtrl',
                    controllerAs: 'invite',
                    size: 'lg',
                    resolve: {
                        invitableFriends: function () {
                            return controller.invitableFBFriends;
                        },
                        message: function () {
                            return 'Come play Twisted Wordsearch with me!';
                        }
                    }
                });
            };

            controller.createGame = function () {
                controller.createGameButtonText = 'Creating game...';
                controller.disableCreate = true;
                //  TODO - ads
                var featureSet = [];
                angular.forEach(controller.choices, function (value) {
                    featureSet.push(value);
                });

                //  TODO - move to starter base
                var players = controller.chosenFriends.map(function (player) {
                    return player.md5;
                });
                var playersAndFeatures = {'players': players, 'features': featureSet};
                jtbBootstrapGameActions.new(playersAndFeatures);
            };
        }
    ]
);