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
    '$stateProvider'
    ($stateProvider) ->
      $stateProvider.state('wsList', {
        url: '/ws',
        templateUrl: 'assets/javascripts/ws/list.html'
        controller: controllers.WSCtrl
      })
  ]
  return mod
