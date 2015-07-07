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
    ItemsBase.someValue = 'Items';

    ItemsBase.getAllItems = function () {
      var defer = $q.defer();
      $http.get('http://localhost:8080/edit-javaee/items', {
        cache: 'true'
      }).success(function (data) {
        defer.resolve(data);
      });
      return defer.promise;
    };

    ItemsBase.getItem = function (id) {
      var defer = $q.defer();
      $http.get('http://localhost:8080/edit-javaee/items/' + id, {
        cache: true
      }).success(function (data) {
        defer.resolve(data);
      });
      return defer.promise;
    };

    return ItemsBase;
  }
}());
