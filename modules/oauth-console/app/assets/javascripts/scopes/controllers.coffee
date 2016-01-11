###*
# Scopes controllers.
###

define [], ->
  'use strict'

  # Scopes for webservice controller

  WSScopesCtrl = ($scope, $routeParams, $rootScope, $location, $log, $uibModal, wsService, scopesService) ->
    $log.log('ws scopes')

    $scope.params = $routeParams
    wsService.list({wsId: $routeParams.wsId}).then((response) ->
      $scope.ws = response[0]

      scopesService.getForWs($scope.ws.id).then((response) ->
        $scope.scopes = response
      )
    )

    $scope.refreshing = false;
    $scope.refresh = ->
      $scope.refreshing = true
      scopesService.downloadFromWs($scope.ws.id).then((response) ->
        $scope.scopes = response
        $scope.refreshing = false
      )

    $log.log($scope.params)

  WSScopesCtrl.$inject = [
    '$scope'
    '$routeParams'
    '$rootScope'
    '$location'
    '$log'
    '$uibModal'
    'wsService'
    'scopesService'
  ]

  return {
    WSScopesCtrl: WSScopesCtrl
  }
