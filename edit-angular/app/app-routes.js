(function () {
  'use strict';

  angular
    .module('editAngular')
    .config(config);

  function config($urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');
  }
}());
