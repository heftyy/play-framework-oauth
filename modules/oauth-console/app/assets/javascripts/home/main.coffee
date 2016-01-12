define [
  'angular'
  './routes'
  './controllers'
  'angular-bootstrap-confirm'
], (angular, routes, controllers) ->
  'use strict'
  mod = angular.module('oauthConsole.home', [
    'ngRoute',
    'home.routes',
    'ui.bootstrap'
  ])

  mod.controller 'HomeCtrl', controllers.HomeCtrl
  return mod
