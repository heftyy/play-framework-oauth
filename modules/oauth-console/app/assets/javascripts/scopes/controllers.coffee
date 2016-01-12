###*
# Scopes controllers.
###

define [], ->
  'use strict'

  # Scopes for webservice controller

  WSScopesCtrl = ($scope, $stateParams, $rootScope, $location, $log, $uibModal, wsService, scopesService) ->
    $log.log('ws scopes')

    $scope.params = $stateParams
    wsService.list({wsId: $stateParams.wsId}).then((response) ->
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
    '$stateParams'
    '$rootScope'
    '$location'
    '$log'
    '$uibModal'
    'wsService'
    'scopesService'
  ]

  ClientScopesCtrl = (
    $scope, $stateParams, $rootScope, $location, $log, $uibModal, $q,
    wsService, scopesService, clientsService
  ) ->
    $log.log('client scopes')

    $scope.params = $stateParams

    $q.all([
      wsService.list({wsId: $stateParams.wsId}),
      clientsService.list({clientId: $stateParams.clientId})
    ]).then((results) ->
      $scope.ws = results[0][0]
      $scope.client = results[1][0]

      $scope.scopeCheckboxes = []

      $q.all([
        scopesService.getForWs($scope.ws.id)
        scopesService.getForWsAndClient($scope.ws.id, $scope.client.id)
      ]).then((results) ->
        $scope.scopes = results[0]

        angular.forEach(results[0], (value) ->
          $scope.scopeCheckboxes[value.id] = false
        )

        angular.forEach(results[1], (value) ->
          $scope.scopeCheckboxes[value.id] = true
        )
      )
    )

    $scope.toggleCheckbox = (scopeId) ->
      value = $scope.scopeCheckboxes[scopeId]
      scopesService.update(scopeId, $scope.client.id, value)

    $log.log($scope.params)

  ClientScopesCtrl.$inject = [
    '$scope'
    '$stateParams'
    '$rootScope'
    '$location'
    '$log'
    '$uibModal'
    '$q'
    'wsService'
    'scopesService'
    'clientsService'
  ]

  return {
    WSScopesCtrl: WSScopesCtrl
    ClientScopesCtrl: ClientScopesCtrl
  }
