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
    .module('map')
    .factory('Brick', Brick);

  function Brick($websocket) {
    var socket, BrickBase, commands;
    BrickBase = {};

    BrickBase.sendJson = function (data) {
      data = angular.toJson(data);
      console.info('Sending', data);
      socket.send(data);
    };

    BrickBase.listen = function (msg) {
      try {
        commands = angular.fromJson(msg.data);
      } catch(err) {
        console.log("Error: %s", err);
      } finally {
        console.log(msg);
      }
      BrickBase.command = commands;
    };

    init();

    return BrickBase;

    function init() {
      socket = $websocket('ws://10.80.49.2:8080/edit-javaee/angular');

      socket.onMessage(function (msg) {
        BrickBase.listen(msg);
      });

      socket.onOpen(function () {
        console.log('Connection opened');
      });
    }
  }
}());
