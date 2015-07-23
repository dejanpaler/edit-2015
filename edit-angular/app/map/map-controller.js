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

  function MapCtrl(Items, $timeout) {
    var i,
      j,
      z,
      vm = this,
      commands,
      pom,
      posY = 10,
      posX = 5,
      corX = 0,
      corY = 0,
      tile = 32,
      canvas = document.getElementById('canvas'),
      context = canvas.getContext('2d'),
      grass = new Image(),
      robot = new Image(),
      glavniot = new Image(),
      fateno = new Image(),
      pick = 2,
      itemList = [],
      mapArray = [
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
      ];
    vm.ctrlName = 'MapCtrl';

    grass.src = '/images/grass.png';
    robot.src = '/images/dogeRight.png';
    glavniot.src = '/images/product.png';
    fateno.src = '/images/dogePacketRight.png';

    mapArray[posY][posX] = 2;

    Items.soc.onMessage(function (msg) {
      vm.listen(msg);
    });

    Items.getAllItems().then(function (data) {
      itemList = data.items;
      for (i = 0; i < itemList.length; i++) {
        if (itemList[i].direction === 'up') {
          mapArray[8 - 3 * itemList[i].coorX - 1][posX + 2 * itemList[i].coorY] = 1;
        } else {
          mapArray[8 - 3 * itemList[i].coorX + 1][posX + 2 * itemList[i].coorY] = 1;
        }
      }
    });

    vm.listen = function (msg) {
      try {
        commands = angular.fromJson(msg.data);
        if (commands.command === 'location') {
          z = parseInt(commands.coordinate.split(',')[2], 10);
          if (parseInt(commands.coordinate.split(',')[0], 10) - corY === 1) {
            $timeout(function () {
              vm.up();
            }, 500);
            $timeout(function () {
              vm.up();
            }, 1000);
            $timeout(function () {
              vm.up();
            }, 1500);
            if (pom === 'start') {
              $timeout(function () {
                vm.up();
              }, 2000);
              $timeout(function () {
                vm.up();
              }, 2500);
              pom = 'end';
            }
            corY += 1;
          } else if (parseInt(commands.coordinate.split(',')[0], 10) - corY === -1) {
            $timeout(function () {
              vm.down();
            }, 500);
            $timeout(function () {
              vm.down();
            }, 1000);
            $timeout(function () {
              vm.down();
            }, 1500);
            if (pom === 'end') {
              pom = 'start';
            }
            corY -= 1;
          } else if (parseInt(commands.coordinate.split(',')[1], 10) - corX === -1) {
            $timeout(function () {
              vm.left();
            }, 500);
            $timeout(function () {
              vm.left();
            }, 1000);
            corX -= 1;
          } else if (parseInt(commands.coordinate.split(',')[1], 10) - corX === 1) {
            $timeout(function () {
              vm.right();
            }, 500);
            $timeout(function () {
              vm.right();
            }, 1000);
            corX += 1;
          }
        } else if (commands.command === 'start') {
          pom = 'start';
        } else if (commands.command === 'end') {
          if (pom !== 'dropped') {
            $timeout(function () {
              vm.down();
            }, 500);
            $timeout(function () {
              vm.down();
            }, 1000);
          }
        } else if (commands.command === 'pickedUp') {
          if (pom === 'end') {
            if (z === 1) {
              $timeout(function () {
                vm.down();
              }, 500);
              $timeout(function () {
                pick = 4;
                mapArray[posY][posX] = pick;
                vm.drawMap2();
              }, 3000);
              $timeout(function () {
                vm.up();
              }, 4500);
            } else {
              $timeout(function () {
                vm.up();
              }, 500);
              $timeout(function () {
                pick = 4;
                mapArray[posY][posX] = pick;
                vm.drawMap2();
              }, 3000);
              $timeout(function () {
                vm.down();
              }, 4500);
            }
          } else {
            pick = 4;
            mapArray[posY][posX] = pick;
            vm.drawMap2();
          }
        } else if (commands.command === 'dropped') {
          if (pom !== 'start') {
            if (z === 1) {
              $timeout(function () {
                vm.down();
              }, 500);
              $timeout(function () {
                pick = 2;
                mapArray[posY][posX] = pick;
                vm.drawMap2();
              }, 3000);
              $timeout(function () {
                vm.up();
              }, 4500);
              mapArray[posY+1][posX] = 1;
              vm.drawMap2();
            } else {
              $timeout(function () {
                vm.up();
              }, 500);
              $timeout(function () {
                pick = 2;
                mapArray[posY][posX] = pick;
                vm.drawMap2();
              }, 3000);
              $timeout(function () {
                vm.down();
                mapArray[posY-1][posX] = 1;
                vm.drawMap2();
              }, 4500);
            }
          } else {
            $timeout(function () {
              vm.down();
            }, 500);
            $timeout(function () {
              vm.down();
            }, 1000);
            $timeout(function () {
              pick = 2;
              mapArray[posY][posX] = pick;
              vm.drawMap2();
            }, 1500);
            pom = 'dropped';
          }
        }
      } catch (err) {
        console.log('Error: %s', err);
      }
    };

    vm.drawMap2 = function () {
      $timeout(function () {
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
      }, 30);
    };

    vm.up = function () {
      mapArray[posY][posX] = 0;
      if (posY > 0) {
        posY -= 1;
      }

      mapArray[posY][posX] = pick;
      vm.drawMap2();
    };

    vm.down = function () {
      mapArray[posY][posX] = 0;
      if (posY < 10) {
        posY += 1;
      }

      mapArray[posY][posX] = pick;
      vm.drawMap2();
    };

    vm.left = function () {
      mapArray[posY][posX] = 0;
      if (posX > 0) {
        posX -= 1;
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

      mapArray[posY][posX] = pick;
      robot.src = '/images/dogeRight.png';
      fateno.src = '/images/dogePacketRight.png';
      vm.drawMap2();
    };
  }
}());
