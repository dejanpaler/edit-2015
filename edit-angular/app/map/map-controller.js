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

  function MapCtrl($websocket) {
    var i,
      j,
      vm = this,
      socket,
      commands,
      posY = 8,
      posX = 5,
      corX = 0,
      corY = 0,
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
        [0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0],
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
    // produk.src = '/images/searched.png';
    robot.src = '/images/dogeRight.png';
    glavniot.src = '/images/product.png';
    fateno.src = '/images/dogePacketRight.png';

    mapArray[posY][posX] = 2;

    vm.listen = function (msg) {
      try {
        commands = angular.fromJson(msg.data);
        if (commands.coordinate) {
          if (parseInt(commands.coordinate.split(',')[0], 10) - corY === 1) {
            setTimeout(function () {
              vm.up();
            }, 500);
            setTimeout(function () {
              vm.up();
            }, 1000);
            setTimeout(function () {
              vm.up();
            }, 1500);
            corY += 1;
            alert(corY);
          } else if (parseInt(commands.coordinate.split(',')[0], 10) - corY === -1) {
            setTimeout(function () {
              vm.down();
            }, 500);
            setTimeout(function () {
              vm.down();
            }, 1000);
            setTimeout(function () {
              vm.down();
            }, 1500);
            corY -= 1;
            alert(corY);
          } else if (parseInt(commands.coordinate.split(',')[1], 10) - corX === -1) {
            setTimeout(function () {
              vm.left();
            }, 500);
            setTimeout(function () {
              vm.left();
            }, 1000);
            corX -= 1;
            alert(corX);
          } else if (parseInt(commands.coordinate.split(',')[1], 10) - corX === 1) {
            setTimeout(function () {
              vm.right();
            }, 500);
            setTimeout(function () {
              vm.right();
            }, 1000);
            corX += 1;
            alert(corX);
          }
        }
      } catch(err) {
        console.log("Error: %s", err);
      } finally {
        console.log(msg);
      }
    };

    init();

    vm.drawMap2 = function () {
      setTimeout(function () {
        for (i = 0; i < mapArray.length; i++) {
          for (j = 0; j < mapArray[i].length; j++) {
            context.drawImage(grass, (j * tile), (i * tile));
            if (mapArray[i][j] === 1) {
              context.drawImage(glavniot, (j * tile), (i * tile));
            }
            if (mapArray[i][j] === 2) {
              context.drawImage(robot, (j * tile), (i * tile));
            }
            if (mapArray[i][j] === 4) {
              context.drawImage(fateno, (j * tile), (i * tile));
            }
          }
        }
      }, 20);
    };

    vm.up = function () {
      mapArray[posY][posX] = 0;
      if (posY > 0) {
        posY -= 1;
      }
      if (mapArray[posY][posX] === 1) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      vm.drawMap2();
    };

    vm.down = function () {
      mapArray[posY][posX] = 0;
      if (posY < 10) {
        posY += 1;
      }
      if (mapArray[posY][posX] === 1) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      vm.drawMap2();
    };

    vm.left = function () {
      mapArray[posY][posX] = 0;
      if (posX > 0) {
        posX -= 1;
      }
      if (mapArray[posY][posX] === 1) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      robot.src = '/images/dogeLeft.png';
      fateno.src = '/images/dogePacketLeft.png';
      vm.drawMap2();
    };

    vm.right = function () {
      mapArray[posY][posX] = 0;
      if (posX < 10) {
        posX += 1;
      }
      if (mapArray[posY][posX] === 1) {
        pick = 4;
      }
      if (posY === 10 && posX === 5) {
        pick = 2;
      }

      mapArray[posY][posX] = pick;
      robot.src = '/images/dogeRight.png';
      fateno.src = '/images/dogePacketRight.png';
      vm.drawMap2();
    };

    function init() {
      socket = $websocket('ws://10.80.49.2:8080/edit-javaee/angular');

      socket.onMessage(function (msg) {
        vm.listen(msg);
      });

      socket.onOpen(function () {
        console.log('Connection opened');
      });
    }
  }
}());
