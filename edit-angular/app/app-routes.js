(function () {
  'use strict';

  angular
    .module('editAngular')
    .config(config);

  function config($routeProvider) {
    $routeProvider.otherwise({
      redirectTo: '/home'
    });
  }
}());
