###*
# WS controllers.
###

define [], ->
  'use strict'

  ###* Controls the ws page ###

  ClientsCtrl = ($scope, $rootScope, $location, $log, clientsService, wsService, uiGridConstants) ->
    $log.log('client list')

    clientsService.list({}).then((list) ->
      $scope.gridOptions.data = list
    )

    $scope.gridOptions =
      enableFiltering: false,
      enableRowSelection: true,
      expandableRowTemplate: 'assets/javascripts/clients/client_details.html',
#      expandableRowScope: expandableScope,
      onRegisterApi: (gridApi) ->
        $scope.gridApi = gridApi
        gridApi.rowEdit.on.saveRow($scope, $scope.saveRow)
        gridApi.expandable.on.rowExpandedStateChanged($scope, (row) ->
          if row.isExpanded
            row.entity.keyUrl = jsRoutes.oauth.controllers.COAuthClient.downloadKey(row.entity.accessorId).url

            row.entity.subGridOptions = {
              columnDefs: [
                { name: 'name'},
                { name: 'domain'},
                {
                  width: 65
                  name: 'Scopes',
                  enableCellEdit: false,
                  enableFiltering: false,
                  cellTemplate: '<a href="#/clients/{{ row.grid.appScope.row.entity.id }}/ws/{{ row.entity.id }}/scopes"><button class="btn btn-sm btn-info">Scopes</button></a>'
                }
              ]};

            wsService.list({}).then((response) ->
              row.entity.subGridOptions.clientId = row.entity.id
              row.entity.subGridOptions.data = response
              $scope.gridOptions.expandableRowHeight = response.length * 30 + 45;
            )
        )
      columnDefs: [
        {field: 'id', enableCellEdit: false, width: 30, enableFiltering: false}
        {field: 'name', enableCellEdit: true}
        {field: 'accessorId', enableCellEdit: false}
        {field: 'creationTime', enableCellEdit: false, type: 'date', cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"}
        {
          width: 65
          name: 'Delete',
          enableCellEdit: false,
          enableFiltering: false,
          enableSorting: false,
          cellTemplate: '<button class="btn btn-sm btn-danger" mwl-confirm title="Delete client" message="Are you really <b>sure</b> you want to do this?" confirm-text="Yes" cancel-text="No" placement="bottom" on-confirm="grid.appScope.removeRow(row)" confirm-button-type="danger" cancel-button-type="default">Delete</button>'
        }
      ]

    $scope.saveRow = (rowEntity) ->
      promise = clientsService.update(rowEntity).then((response) ->
        if !rowEntity.id && response && response.length == 1
          rowEntity.id = response[0].id
          rowEntity.accessorId = response[0].accessorId
          rowEntity.password = response[0].password
          rowEntity.publicKey = response[0].publicKey
          rowEntity.creationTime = response[0].creationTime
      )
      $scope.gridApi.rowEdit.setSavePromise(rowEntity, promise)

    $scope.addRow = ->
      $scope.gridOptions.data.push
        'id': ''
        'name': ''
        'accessorId': ''

    $scope.removeRow = (row) ->
      id = row.entity.id
      index = $scope.gridOptions.data.indexOf(row.entity)
      if id
        clientsService.delete(id).then(() ->
          $scope.gridOptions.data.splice(index, 1)
        )
      else
        $scope.gridOptions.data.splice(index, 1)

    $scope.toggleFiltering = ->
      $scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering
      $scope.gridApi.core.notifyDataChange uiGridConstants.dataChange.COLUMN

  ClientsCtrl.$inject = [
    '$scope'
    '$rootScope'
    '$location'
    '$log'
    'clientsService'
    'wsService'
    'uiGridConstants'
  ]

  return {
    ClientsCtrl: ClientsCtrl
  }
