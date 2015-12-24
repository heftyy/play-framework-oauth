###*
# Configure routes of user module.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('user.routes', [
    'user.services'
    'oauthConsole.common'
  ])
  mod.config [
    '$routeProvider'
    ($routeProvider) ->
      $routeProvider.when '/login',
        templateUrl: 'vassets/javascripts/user/login.html'
        controller: controllers.LoginCtrl
      #.when('/users', {templateUrl:'/assets/templates/user/users.html', controller:controllers.UserCtrl})
      #.when('/users/:id', {templateUrl:'/assets/templates/user/editUser.html', controller:controllers.UserCtrl});
  ]
  mod
