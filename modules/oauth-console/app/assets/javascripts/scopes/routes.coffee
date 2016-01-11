###*
# Scopes routes.
###

define [
  'angular'
  './controllers'
  './services'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('scopes.routes', [ 'oauthConsole.common', 'ui.router' ])

  mod.config [
    '$routeProvider'
    '$stateProvider'
    'modalStateProvider'
    ($routeProvider, $stateProvider, modalStateProvider) ->
      modalStateProvider.state('wsList.scopes',
        url: '/scopes/:wsId'
        templateUrl: 'assets/javascripts/scopes/ws.html'
        controller: controllers.WSScopesCtrl
      )

      $routeProvider.otherwise templateUrl: 'assets/javascripts/common/notFound.html'
  ]
  return mod
