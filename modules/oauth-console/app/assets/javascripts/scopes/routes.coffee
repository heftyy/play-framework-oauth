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
    '$stateProvider'
    'modalStateProvider'
    ($stateProvider, modalStateProvider) ->
      modalStateProvider.state('wsList.scopes',
        url: '/:wsId/scopes'
        templateUrl: 'assets/javascripts/scopes/ws.html'
        controller: controllers.WSScopesCtrl
      )

      modalStateProvider.state('clientList.scopes',
        url: '/:clientId/ws/:wsId/scopes'
        templateUrl: 'assets/javascripts/scopes/client.html'
        controller: controllers.ClientScopesCtrl
      )
  ]
  return mod
