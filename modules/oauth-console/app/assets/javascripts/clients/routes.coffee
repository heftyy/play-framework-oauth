###*
# Home routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('clients.routes', [ 'oauthConsole.common' ])
  mod.config [
    '$routeProvider'
    ($routeProvider) ->
      $routeProvider.when('/',
        templateUrl: 'vassets/javascripts/clients/list.html'
        controller: controllers.ClientCtrl).otherwise templateUrl: 'vassets/javascripts/clients/notFound.html'
  ]
  mod
