(function () {
  'use strict';

  var app = angular.module('home');



  app.controller('ProductCtrl', ['$scope', function($scope) {
    $scope.products = prod;
  }]);

}());
