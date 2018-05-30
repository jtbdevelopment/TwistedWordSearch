'use strict';

angular.module('twsUI').controller('CreateGameCtrl',
  ['jtbAppLongName', 'jtbGameFeatureService', 'jtbPlayerService',
    'featureDescriber', 'jtbBootstrapGameActions', 'jtbFacebook',
    function (jtbAppLongName, jtbGameFeatureService, jtbPlayerService,
      featureDescriber, jtbBootstrapGameActions, jtbFacebook) {
      var controller = this;

      controller.features = [];
      controller.choices = {};

      controller.createGameButtonText = 'Create Game';
      controller.disableCreate = false;

      //  sets chosenFriends and friends
      jtbPlayerService.initializeFriendsForController(controller);

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
        });

      controller.inviteFriends = function () {
        jtbFacebook.inviteFriends(
          [],
          'Come play ' + jtbAppLongName + ' with me!');
      };

      controller.createGame = function () {
        controller.createGameButtonText = 'Creating game...';
        controller.disableCreate = true;
        //  TODO - ads
        var featureSet = [];
        angular.forEach(controller.choices, function (value) {
          featureSet.push(value);
        });

        var players = controller.chosenFriends.map(function (player) {
          return player.md5;
        });
        var playersAndFeatures = {'players': players, 'features': featureSet};
        jtbBootstrapGameActions.new(playersAndFeatures);
      };
    }
  ]
);
