### License: MIT - https://github.com/angular-ui/bootstrap/blob/master/LICENSE ###

angular.module('common.modalState', [
  'ui.router'
  'ui.bootstrap'
]).provider('modalState', ($stateProvider) ->
  provider = this

  this.$get = ->
    provider

  this.state = (stateName, options) ->
    modalInstance = undefined
    $stateProvider.state stateName,
      url: options.url
      onEnter: ($uibModal, $state) ->
        modalInstance = $uibModal.open(options)
        modalInstance.result['finally'] ->
          modalInstance = null
          if $state.$current.name == stateName
            $state.go '^'
      onExit: ->
        if modalInstance
          modalInstance.close()

  return this

).directive 'modalScope', (modalState) ->
  {
  restrict: 'A'
  link: (scope, elem, attrs) ->
    # Don't have to worry about precedence here because ui-sref uses $timeout.
    elem.bind 'click', ->
      modalState.setParentScope scope

  }
  #end module