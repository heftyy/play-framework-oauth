###*
# WS controllers.
###

define [], ->
  'use strict'

  ###* Controls the ws page ###

  WSCtrl = ($scope, $rootScope, $location, $log, wsService, uiGridConstants) ->
    wsService.list({}).then((list) ->
      $scope.gridOptions.data = list
    )

    $scope.gridOptions =
      enableFiltering: false
      onRegisterApi: (gridApi) ->
        $scope.gridApi = gridApi
        gridApi.rowEdit.on.saveRow($scope, $scope.saveRow)
      columnDefs: [
        {field: 'id', enableCellEdit: false, width: 30, enableFiltering: false}
        {field: 'name', enableCellEdit: true}
        {field: 'domain', enableCellEdit: true}
        {field: 'scopeRequestUrl', enableCellEdit: true}
        {field: 'enabled', enableCellEdit: true, type: 'boolean'}
        {
          width: 65
          name: 'Scopes',
          enableCellEdit: false,
          enableFiltering: false,
          cellTemplate: '<a href="#/scopes/ws/{{ row.entity.id }}"><button class="btn btn-sm btn-info" ng-click="grid.appScope.wsScopes(row)">Scopes</button></a>'
        }
        {
          width: 65
          name: 'Delete',
          enableCellEdit: false,
          enableFiltering: false,
          cellTemplate: '<button class="btn btn-sm btn-danger" mwl-confirm title="Delete webservice" message="Are you really <b>sure</b> you want to do this?" confirm-text="Yes" cancel-text="No" placement="bottom" on-confirm="grid.appScope.removeRow(row)" confirm-button-type="danger" cancel-button-type="default">Delete</button>'
        }
      ]

    $scope.saveRow = (rowEntity) ->
      promise = wsService.update(rowEntity).then((response) ->
        if !rowEntity.id && response && response.length == 1
          rowEntity.id = response[0].id
      )
      $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise)

    $scope.addRow = ->
      $scope.gridOptions.data.push
        'id': ''
        'name': ''
        'domain': 'http://'
        'scopeRequestUrl': '/'
        'enabled': true

    $scope.removeRow = (row) ->
      id = row.entity.id
      index = $scope.gridOptions.data.indexOf(row.entity)
      if id
        wsService.delete(id).then(() ->
          $scope.gridOptions.data.splice(index, 1)
        )
      else
        $scope.gridOptions.data.splice(index, 1)

    $scope.toggleFiltering = ->
      $scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering
      $scope.gridApi.core.notifyDataChange uiGridConstants.dataChange.COLUMN

  WSCtrl.$inject = [
    '$scope'
    '$rootScope'
    '$location'
    '$log'
    'wsService'
    'uiGridConstants'
  ]

  ScopesCtrl = ($scope, $routeParams, $rootScope, $location, $log) ->
    $scope.params = $routeParams
    $log.log($scope.params)


  ScopesCtrl.$inject = [
    '$scope'
    '$routeParams'
    '$rootScope'
    '$location'
    '$log'
  ]

  return {
    WSCtrl: WSCtrl
  }
