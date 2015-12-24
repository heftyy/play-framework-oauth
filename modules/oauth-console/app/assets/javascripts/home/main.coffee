###*
# Main, shows the start page and provides controllers for the header and the footer.
# This the entry module which serves as an entry point so other modules only have to include a
# single module.
###

define [
  'angular'
  './routes'
  './controllers'
], (angular, routes, controllers) ->
  'use strict'
  mod = angular.module('oauthConsole.home', [
    'ngRoute'
    'home.routes'
  ])
  mod.controller 'HeaderCtrl', controllers.HeaderCtrl
  mod.controller 'FooterCtrl', controllers.FooterCtrl
  mod
