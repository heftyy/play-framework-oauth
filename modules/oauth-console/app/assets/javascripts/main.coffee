# `main.js` is the file that sbt-web will use as an entry point
((requirejs) ->
  'use strict'

  # -- RequireJS config --
  requirejs.config({
    # Packages = top-level folders; loads a contained file named 'main.js"
    packages: ['common', 'ws', 'scopes', 'clients'],
    shim: {
      'jsRoutes': {
        deps: [],
        # it's not a RequireJS module, so we have to tell it what var is returned
        exports: 'jsRoutes'
      }
    },
    paths: {
      'jsRoutes': ['/oauth/console/js/routes?noext'],
      'angular-bootstrap-confirm': ['angular-bootstrap-confirm.min']
    }
  });

  requirejs.onError = (err) ->
    console.log(err)

  # Load the app. This is kept minimal so it doesn't need much updating.
  require(['angular', 'angular-cookies', 'angular-route', 'jquery', 'ui-bootstrap', 'ui-grid', 'angular-ui-router', './app'],
    (angular) ->
      angular.bootstrap(document, ['app'])
  ))(requirejs);