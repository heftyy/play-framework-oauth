define [
  'angular'
  './routes'
  './controllers'
  './services',
  'angular-bootstrap-confirm'
], (angular, routes, controllers, services, bootstrapConfirm) ->
  'use strict'
  mod = angular.module('oauthConsole.scopes', [
    'ngRoute',
    'scopes.routes',
    'scopes.services',
    'ui.bootstrap',
    bootstrapConfirm
  ])

  mod.controller 'WSScopesCtrl', controllers.WSScopesCtrl
  return mod
