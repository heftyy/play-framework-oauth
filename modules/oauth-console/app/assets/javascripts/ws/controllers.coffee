###*
# WS controllers.
###

define [], ->
  'use strict'

  ###* Controls the ws page ###

  WSCtrl = ($scope, $rootScope, $location, $log, wsService, uiGridConstants) ->
    $log.log('ws list')

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
          cellTemplate: '<a href="#/ws/scopes/{{ row.entity.id }}"><button class="btn btn-sm btn-info">Scopes</button></a>'
        }
        {
          width: 65
          name: 'Delete',
          enableCellEdit: false,
          enableFiltering: false,
          enableSorting: false,
          cellTemplate: '<button class="btn btn-sm btn-danger" mwl-confirm title="Delete webservice" message="Are you really <b>sure</b> you want to do this?" confirm-text="Yes" cancel-text="No" placement="bottom" on-confirm="grid.appScope.removeRow(row)" confirm-button-type="danger" cancel-button-type="default">Delete</button>'
        }
      ]

    $scope.saveRow = (rowEntity) ->
      promise = wsService.update(rowEntity).then((response) ->
        if !rowEntity.id && response && response.length == 1
          rowEntity = response[0]
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

  return {
    WSCtrl: WSCtrl
  }
