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
        downloadFromWs: (wsId) ->
          playRoutes.oauth.controllers.COAuthScope.downloadScopes(wsId).get().then((response) ->
            return response.data
          )
        getForWs: (wsId) ->
          playRoutes.oauth.controllers.COAuthScope.scopesForWS(wsId).get().then((response) ->
            return response.data
          )
        getForWsAndClient: (wsId, clientId) ->
          playRoutes.oauth.controllers.COAuthScope.scopesForWSAndClient(wsId, clientId).get().then((response) ->
            return response.data
          )
        update: (scopeId, clientId, value) ->
          json = {
            scopeId: scopeId,
            clientId: clientId,
            value: value
          }

          playRoutes.oauth.controllers.COAuthScope.update().post(JSON.stringify(json)).then((response) ->
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
