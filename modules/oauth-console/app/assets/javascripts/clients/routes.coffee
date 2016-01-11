###*
# WS routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('clients.routes', [ 'oauthConsole.common', 'ui.router' ])
  mod.config [
    '$routeProvider'
    '$stateProvider'
    ($routeProvider, $stateProvider) ->
      $stateProvider.state('clientList', {
        url: '/clients',
        templateUrl: 'assets/javascripts/clients/list.html'
        controller: controllers.ClientsCtrl
      })

      $routeProvider.otherwise templateUrl: 'assets/javascripts/common/notFound.html'
  ]
  return mod
