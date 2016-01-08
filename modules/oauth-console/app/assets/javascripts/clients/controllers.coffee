###*
# Clients controllers.
###

define [], ->
  'use strict'

  ###* Controls the index page ###

  ClientCtrl = ($scope, $rootScope, $location, helper) ->
    console.log helper.sayHi()
    $rootScope.pageTitle = 'Welcome'

  ClientCtrl.$inject = [
    '$scope'
    '$rootScope'
    '$location'
    'helper'
  ]

  ###* Controls the header ###

  HeaderCtrl = ->

  HeaderCtrl.$inject = [
  ]

  ###* Controls the footer ###

  FooterCtrl = ->

  #FooterCtrl.$inject = ['$scope'];
  {
    HeaderCtrl: HeaderCtrl
    FooterCtrl: FooterCtrl
    ClientCtrl: ClientCtrl
  }
