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
    var i,
      j,
      vm = this,
      posY = 10,
      posX = 5,
      tile = 32,
      canvas = document.getElementById('canvas'),
      context = canvas.getContext('2d'),
      grass = new Image(),
      robot = new Image(),
      produk = new Image(),
      glavniot = new Image(),
      fateno = new Image(),
      pick = 2,
      mapArray = [
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 1, 0, 3, 0, 0, 0, 1, 0, 1, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0],
        [0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
      ];
    vm.ctrlName = 'MapCtrl';

    grass.src = '/images/grass.png';
    produk.src = '/images/searched.png';
    robot.src = '/images/dogeRight.png';
    glavniot.src = '/images/product.png';
    fateno.src = '/images/dogePacketRight.png';

    vm.drawMap2 = function () {
      console.log(robot.src);
      for (i = 0; i < mapArray.length; i++) {
        for (j = 0; j < mapArray[i].length; j++) {
          context.drawImage(grass, (j * tile), (i * tile), tile, tile);
          if (mapArray[i][j] === 1) {
            context.drawImage(glavniot, (j * tile), (i * tile), tile, tile);
          }
          if (mapArray[i][j] === 2) {
            context.drawImage(robot, (j * tile), (i * tile), tile, tile);
          }
          if (mapArray[i][j] === 3) {
            context.drawImage(produk, (j * tile), (i * tile), tile, tile);
          }
          if (mapArray[i][j] === 4) {
            context.drawImage(fateno, (j * tile), (i * tile), tile, tile);
          }
        }
      }
    };

    mapArray[posY][posX] = 2;

    vm.drawMap = function () {
      angular.element('#mapa').append('<div id="container"></div>');
      for (i = 0; i < mapArray.length; i++) {
        for (j = 0; j < mapArray[i].length; j++) {
          if (mapArray[i][j] === 0) {
            angular.element('#container').append('<div class="floor"></div>');
          }
          if (mapArray[i][j] === 1) {
            angular.element('#container').append('<div class="items"></div>');
          }
          if (mapArray[i][j] === 2) {
            angular.element('#container').append('<div class="robot"></div>');
          }
          if (mapArray[i][j] === 3) {
            angular.element('#container').append('<div class="item"></div>');
          }
          if (mapArray[i][j] === 4) {
            angular.element('#container').append('<div class="picked"></div>');
          }
        }
      }
    };

    vm.up = function () {
      angular.element('#container').remove();
      mapArray[posY][posX] = 0;
      if (posY > 0) {
        posY -= 1;
      }
      if (mapArray[posY][posX] === 3) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      vm.drawMap();
      vm.drawMap2();
    };

    vm.down = function () {
      angular.element('#container').remove();
      mapArray[posY][posX] = 0;
      if (posY < 10) {
        posY += 1;
      }
      if (mapArray[posY][posX] === 3) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      vm.drawMap();
      vm.drawMap2();
    };

    vm.left = function () {
      angular.element('#container').remove();
      mapArray[posY][posX] = 0;
      if (posX > 0) {
        posX -= 1;
      }
      if (mapArray[posY][posX] === 3) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      robot.src = '/images/dogeLeft.png';
      fateno.src = '/images/dogePacketLeft.png';
      vm.drawMap();
      vm.drawMap2();
    };

    vm.right = function () {
      angular.element('#container').remove();
      mapArray[posY][posX] = 0;
      if (posX < 10) {
        posX += 1;
      }
      if (mapArray[posY][posX] === 3) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      robot.src = '/images/dogeRight.png';
      fateno.src = '/images/dogePacketRight.png';
      vm.drawMap();
      vm.drawMap2();
    };
  }
}());
