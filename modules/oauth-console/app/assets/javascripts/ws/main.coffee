define [
  'angular'
  './routes'
  './controllers'
  './services',
  'angular-bootstrap-confirm'
], (angular, routes, controllers, services, bootstrapConfirm) ->
  'use strict'
  mod = angular.module('oauthConsole.ws', [
    'ngRoute',
    'ws.routes',
    'ws.services',
    'ui.grid',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.cellNav',
    'ui.bootstrap',
    bootstrapConfirm
  ])

  mod.controller 'WSCtrl', controllers.WSCtrl
  return mod
