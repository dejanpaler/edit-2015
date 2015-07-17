(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name map.controller:MapCtrl
   *
   * @description
   *
   */
  angular
    .module('map')
    .controller('MapCtrl', MapCtrl);

  function MapCtrl() {
    var i;
    var j;
    var vm = this;
    vm.ctrlName = 'MapCtrl';

    var canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');

    var grass = new Image();
    var sand = new Image();

    grass.src = 'grass.png';
    sand.src = 'sand.png';

    var mapArray = [
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 3, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      [0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
      [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
    ];

    var posY = 20;
    var posX = 10;

    var possX = 0;
    var possY = 0;

    vm.drawMap2 = function () {
      for (i = 0; i < mapArray.length; i++) {
        for (j = 0; j < mapArray[i].length; j++) {
          if (mapArray[i][j] === 0) {
            context.drawImage(grass, possX, possY, 32, 32);
          }
          if (mapArray[i][j] === 1) {
            context.drawImage(sand, possX, possY, 32, 32);
          }
          possX += 32;
        }
        possX = 0;
        possY += 32;
      }
    };

    mapArray[posY][posX] = 2;

    vm.drawMap = function () {
      angular.element('#mapa').append('<div id="container"></div>');
      for (i=0; i < mapArray.length; i++) {
        for (j=0; j < mapArray[i].length; j++) {
          if (parseInt(mapArray[i][j]) === 0) {
            angular.element('#container').append('<div class="floor"></div>');
          }
          if (parseInt(mapArray[i][j]) === 1) {
            angular.element('#container').append('<div class="items"></div>');
          }
          if (parseInt(mapArray[i][j]) === 2) {
            angular.element('#container').append('<div class="robot"></div>');
          }
          if (parseInt(mapArray[i][j]) === 3) {
            angular.element('#container').append('<div class="item"></div>');
          }
        }
      }
    };

    vm.up = function () {
      posY -= 1;
      mapArray[posY][posX] = 2;
      vm.drawMap();
    };

    vm.down = function () {
      posY += 1;
      mapArray[posY][posX] = 2;
      vm.drawMap();
    };

    vm.left = function () {
      posX -= 1;
      mapArray[posY][posX] = 2;
      vm.drawMap();
    };

    vm.right = function () {
      posX += 1;
      mapArray[posY][posX] = 2;
      vm.drawMap();
    };
  }
}());
