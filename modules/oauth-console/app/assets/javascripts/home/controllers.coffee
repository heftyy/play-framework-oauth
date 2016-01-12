###*
# Scopes controllers.
###

define [], ->
  'use strict'

  # Scopes for webservice controller

  HomeCtrl = ($scope, $stateParams, $rootScope, $location, $log) ->
    $log.log('home')

  HomeCtrl.$inject = [
    '$scope'
    '$stateParams'
    '$rootScope'
    '$location'
    '$log'
  ]

  return {
    HomeCtrl: HomeCtrl
  }
