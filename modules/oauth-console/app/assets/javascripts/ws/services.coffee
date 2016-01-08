define [
  'angular'
  'common'
], (angular) ->
  'use strict'
  mod = angular.module('ws.services', [
    'oauthConsole.common'
    'ngCookies'
  ])
  mod.factory 'wsService', [
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
        update: (json) ->
          if $.isArray(json)
            json = {models: json}
          else if typeof json.models == 'undefined'
            json = {models: [json]}

          playRoutes.oauth.controllers.COAuthWS.update().post(JSON.stringify(json)).then((response) ->
            return response.data
          )
        delete: (id) ->
          playRoutes.oauth.controllers.COAuthWS.delete(id).delete().then((response) ->
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
