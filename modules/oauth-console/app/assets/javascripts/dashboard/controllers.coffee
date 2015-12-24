###*
# Dashboard controllers.
###

define [], ->
  'use strict'

  ###*
  # user is not a service, but stems from userResolve (Check ../user/services.js) object used by dashboard.routes.
  ###

  DashboardCtrl = ($scope, user) ->
    $scope.user = user

  DashboardCtrl.$inject = [
    '$scope'
    'user'
  ]

  AdminDashboardCtrl = ($scope, user) ->
    $scope.user = user

  AdminDashboardCtrl.$inject = [
    '$scope'
    'user'
  ]
  {
    DashboardCtrl: DashboardCtrl
    AdminDashboardCtrl: AdminDashboardCtrl
  }
