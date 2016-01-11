define [
  'angular'
  './routes'
  './controllers'
  './services',
  'angular-bootstrap-confirm'
], (angular, routes, controllers, services, bootstrapConfirm) ->
  'use strict'
  mod = angular.module('oauthConsole.clients', [
    'ngRoute',
    'clients.routes',
    'clients.services',
    'ui.bootstrap',
    bootstrapConfirm
  ])

  mod.controller 'ClientsCtrl', controllers.ClientsCtrl
  return mod
