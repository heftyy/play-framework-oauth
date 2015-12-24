###*
# Home controllers.
###

define [], ->
  'use strict'

  ###* Controls the index page ###

  HomeCtrl = ($scope, $rootScope, $location, helper) ->
    console.log helper.sayHi()
    $rootScope.pageTitle = 'Welcome'

  HomeCtrl.$inject = [
    '$scope'
    '$rootScope'
    '$location'
    'helper'
  ]

  ###* Controls the header ###

  HeaderCtrl = ($scope, userService, helper, $location) ->
    # Wrap the current user from the service in a watch expression
    $scope.$watch (->
      user = userService.getUser()
      user
    ), ((user) ->
      $scope.user = user
    ), true

    $scope.logout = ->
      userService.logout()
      $scope.user = undefined
      $location.path '/'

  HeaderCtrl.$inject = [
    '$scope'
    'userService'
    'helper'
    '$location'
  ]

  ###* Controls the footer ###

  FooterCtrl = ->

  #FooterCtrl.$inject = ['$scope'];
  {
    HeaderCtrl: HeaderCtrl
    FooterCtrl: FooterCtrl
    HomeCtrl: HomeCtrl
  }
