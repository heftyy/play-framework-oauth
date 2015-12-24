###*
# Dashboard routes.
###

define [
  'angular'
  './controllers'
  'common'
], (angular, controllers) ->
  'use strict'
  mod = angular.module('dashboard.routes', [ 'oauthConsole.common' ])
  mod.config [
    '$routeProvider'
    'userResolve'
    ($routeProvider, userResolve) ->
      $routeProvider.when '/dashboard',
        templateUrl: 'vassets/javascripts/dashboard/dashboard.html'
        controller: controllers.DashboardCtrl
        resolve: userResolve
      #.when('/admin/dashboard',  {templateUrl: '/assets/templates/dashboard/admin.html',  controller:controllers.AdminDashboardCtrl})
  ]
  mod
