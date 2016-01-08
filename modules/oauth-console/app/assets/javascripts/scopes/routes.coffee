###*
# Scopes routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('scopes.routes', [ 'oauthConsole.common', 'ui.router' ])
  mod.config [
    '$routeProvider'
    '$stateProvider'
    ($routeProvider, $stateProvider) ->
      $routeProvider.otherwise templateUrl: 'assets/javascripts/common/notFound.html'

      $stateProvider.state 'scopes.ws',
        url: '/scopes/ws/:wsId'
        onEnter: [
          '$stateParams'
          '$state'
          '$modal'
          '$resource'
          ($stateParams, $state, $modal, $resource) ->
            $modal.open(
              templateUrl: 'scopes/ws.html'
              resolve: item: ->
                new Item(123).get()
              controller: [
                '$scope'
                'item'
                ($scope, item) ->

                  $scope.dismiss = ->
                    $scope.$dismiss()

                  $scope.save = ->
                    item.update().then ->
                      $scope.$close true

              ]).result.finally ->
            $state.go '^'
        ]
  ]
  return mod
