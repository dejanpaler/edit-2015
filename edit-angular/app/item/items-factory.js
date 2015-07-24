(function () {
  'use strict';

  /**
     * @ngdoc service
     * @name item.factory:Items
     *
     * @description
     *
     */
  angular
    .module('item')
    .factory('Items', Items);

  function Items($http, $q, $websocket) {
    var ItemsBase = {},
        socket;

    ItemsBase.getAllItems = function () {
      var defer = $q.defer();
      $http.get('http://localhost:8080/edit-javaee/items', {
        cache: 'true'
      }).success(function (data) {
        defer.resolve({items: data});
      })
      .error(function () {
        defer.resolve({error: 'Couldn\'t connect to the server.'});
      });
      return defer.promise;
    };

    ItemsBase.getItem = function (id) {
      var defer = $q.defer();
      $http.get('http://localhost:8080/edit-javaee/items/' + id, {
        cache: 'true'
      }).success(function (data) {
        defer.resolve({item: data});
      })
      .error(function () {
        defer.respolve({error: 'Couldn\'t connect to the server.'});
      });
      return defer.promise;
    };

    init();

    function init() {
      socket = $websocket('ws://localhost:8080/edit-javaee/angular');

      socket.onMessage(function (msg) {
        console.log(msg);
      });

      socket.onOpen(function () {
        console.log('Connection opened');
      });

      ItemsBase.soc = socket;
    }

    return ItemsBase;
  }
}());
