(function () {
  'use strict';

  angular
    .module('editAngular')
    .config(config);

  function config($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');
  }
}());
