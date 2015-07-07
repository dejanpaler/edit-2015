(function () {
  'use strict';

  /**
   * @ngdoc service
   * @name item.factory:Brick
   *
   * @description
   *
   */
  angular
    .module('item')
    .factory('Brick', Brick);

  function Brick($websocket) {
    var socket, BrickBase;
    BrickBase = {};

    BrickBase.sendJson = function (data) {
      data = angular.toJson(data);
      console.info("Sending", data);
      socket.send(data);
    };

    init();

    return BrickBase;

    function init() {
      socket = $websocket('ws://localhost:8080/edit-javaee/commands');

      socket.onMessage = function (data) {
        console.log(data);
      }

      socket.onOpen = function (data) {
        console.log("Conection opened.", data);
      }
    }
  }
}());
