define [
  'angular'
  'common'
], (angular) ->
  'use strict'
  mod = angular.module('scopes.services', [
    'oauthConsole.common'
    'ngCookies'
  ])
  mod.factory 'scopesService', [
    '$http'
    '$q'
    'playRoutes'
    '$cookies'
    '$log'
    ($http, $q, playRoutes, $cookies, $log) ->

      return {
        list: (json) ->
          playRoutes.oauth.controllers.COAuthWS.getList(JSON.stringify(json)).get().then((response) ->
            return response.data
          )
      }
  ]

  handleRouteError = ($rootScope, $location) ->
    $rootScope.$on '$routeChangeError', ->
      $location.path '/'

  handleRouteError.$inject = [
    '$rootScope'
    '$location'
  ]
  mod.run handleRouteError
  return mod
