# `main.js` is the file that sbt-web will use as an entry point
((requirejs) ->
  'use strict'

  # -- RequireJS config --
  requirejs.config({
    # Packages = top-level folders; loads a contained file named 'main.js"
    packages: ['common', 'home', 'user', 'dashboard'],
    shim: {
      'jsRoutes': {
        deps: [],
        # it's not a RequireJS module, so we have to tell it what var is returned
        exports: 'jsRoutes'
      }
    },
    paths: {
      'jsRoutes': ['/oauth/console/js/routes?noext']
    }
  });

  requirejs.onError = (err) ->
    console.log(err)

  # Load the app. This is kept minimal so it doesn't need much updating.
  require(['angular', 'angular-cookies', 'angular-route', 'jquery', 'bootstrap', './app'],
    (angular) ->
      angular.bootstrap(document, ['app'])
  ))(requirejs);