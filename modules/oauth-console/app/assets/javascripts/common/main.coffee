###*
# Common functionality.
###

define [
  'angular'
  './services/helper'
  './services/playRoutes'
  './filters'
  './directives/example'
  './modal_state'
], (angular) ->
  'use strict'
  angular.module 'oauthConsole.common', [
    'common.helper'
    'common.playRoutes'
    'common.filters'
    'common.directives.example'
    'common.modalState'
  ]
