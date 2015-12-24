###*
# Home routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('home.routes', [ 'oauthConsole.common' ])
  mod.config [
    '$routeProvider'
    ($routeProvider) ->
      $routeProvider.when('/',
        templateUrl: 'vassets/javascripts/home/home.html'
        controller: controllers.HomeCtrl).otherwise templateUrl: 'vassets/javascripts/home/notFound.html'
  ]
  mod
