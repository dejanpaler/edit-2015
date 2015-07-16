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

  function Items($http, $q) {
    var ItemsBase = {};

    ItemsBase.getAllItems = function () {
      var defer = $q.defer();
      // 10.80.49.2:8080/go
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
        cache: true
      }).success(function (data) {
        defer.resolve({item: data});
      })
      .error(function () {
        defer.respolve({error: 'Couldn\'t connect to the server.'});
      });
      return defer.promise;
    };

    return ItemsBase;
  }
}());
