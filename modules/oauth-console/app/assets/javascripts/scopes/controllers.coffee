###*
# Scopes controllers.
###

define [], ->
  'use strict'

  # Scopes for webservice controller

  WSScopesCtrl = ($scope, $routeParams, $rootScope, $location, $log, wsService) ->
    $scope.params = $routeParams
    wsService.list({wsId: $routeParams.wsId}).then((response) ->
      $scope.ws = response[0]
    )
    $log.log($scope.params)

  WSScopesCtrl.$inject = [
    '$scope'
    '$routeParams'
    '$rootScope'
    '$location'
    '$log'
    'wsService'
  ]

  return {
    WSScopesCtrl: WSScopesCtrl
  }
