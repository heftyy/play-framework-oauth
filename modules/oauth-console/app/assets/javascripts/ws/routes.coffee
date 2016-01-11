###*
# WS routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('ws.routes', [ 'oauthConsole.common', 'ui.router' ])
  mod.config [
    '$routeProvider'
    '$stateProvider'
    ($routeProvider, $stateProvider) ->
      $stateProvider.state('wsList', {
        url: '/ws',
        templateUrl: 'assets/javascripts/ws/list.html'
        controller: controllers.WSCtrl
      })

      $routeProvider.otherwise templateUrl: 'assets/javascripts/common/notFound.html'
  ]
  return mod
