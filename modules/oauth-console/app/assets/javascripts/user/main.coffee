###*
# User package module.
# Manages all sub-modules so other RequireJS modules only have to import the package.
###

define [
  'angular'
  './routes'
  './services'
], (angular) ->
  'use strict'
  angular.module 'oauthConsole.user', [
    'ngCookies'
    'ngRoute'
    'user.routes'
    'user.services'
  ]
