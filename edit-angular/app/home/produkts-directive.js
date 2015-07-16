(function () {
  'use strict';

  /**
   * @ngdoc directive
   * @name home.directive:produkts
   * @restrict EA
   * @element
   *
   * @description
   *
   * @example
     <example module="home">
       <file name="index.html">
        <produkts></produkts>
       </file>
     </example>
   *
   */
  angular
    .module('home')
    .directive('produkts', produkts);

  function produkts() {
    return {
      restrict: 'EA',
      scope: {},
      templateUrl: 'home/produkts-directive.tpl.html',
      replace: false,
      controllerAs: 'produkts',
      controller: function ($scope) {
        $scope.products = prod;
      },
      link: function (scope, element, attrs) {
        /*jshint unused:false */
        /*eslint "no-unused-vars": [2, {"args": "none"}]*/
      }
    };
  }

  var prod = [
    {
      name: 'Black',
      size: 3,
      description: 'Very nice!',
      price: 110.50,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
        "images/green_doge_01"
      ]
    },
    {
      name: 'Red',
      size: 1,
      description: 'Very nice!'
    },
    {
      name: 'Green',
      size: 2,
      description: 'Such green. Much doge. WOW!',
      price: 1.75,
      images: [
        "../images/black_ball_01.jpg",
        "https://i.imgur.com/nAeeF8W.jpg",
        "../images/black_ball_01.jpg"
      ]
    },
    {
      name: 'Black',
      size: 5,
      description: 'Its round and black! Very nice. How much?'
    },
    {
      name: 'Red',
      size: 2,
      description: 'Very nice!'
    },
    {
      name: 'Green',
      size: 4,
      description: 'Very nice!'
    }
  ];

}());
