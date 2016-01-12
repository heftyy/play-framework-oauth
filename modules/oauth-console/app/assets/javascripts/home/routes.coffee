###*
# Scopes routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('home.routes', ['oauthConsole.common', 'ui.router'])

  mod.config [
    '$stateProvider'
    ($stateProvider) ->
      $stateProvider.state('home', {
        url: '/',
        templateUrl: 'assets/javascripts/home/home.html'
        controller: controllers.HomeCtrl
      })
  ]
  return mod
