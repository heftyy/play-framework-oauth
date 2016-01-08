###*
# WS routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('ws.routes', [ 'oauthConsole.common' ])
  mod.config [
    '$routeProvider'
    ($routeProvider) ->
      $routeProvider.when(
        '/ws',
        templateUrl: 'assets/javascripts/ws/list.html'
        controller: controllers.WSCtrl
      ).otherwise templateUrl: 'assets/javascripts/common/notFound.html'
  ]
  return mod
