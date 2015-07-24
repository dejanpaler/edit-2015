(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name item.controller:AddItemCtrl
   *
   * @description
   *
   */
  angular
    .module('item')
    .controller('AddItemCtrl', AddItemCtrl);

  function AddItemCtrl($http, Items) {
    var vm = this;
    vm.ctrlName = 'AddItemCtrl';

    vm.itemList = [];
    vm.error = 0;
    Items.getAllItems().then(function (data) {
      if (data.error) {
        vm.error = data.error;
      } else {
        vm.itemList = data.items;
      }
    });

    vm.addItem = function (title) {
      $http.post('http://localhost:8080/edit-javaee/items/put', {title: title});
      location.href = '/#/map';
    };

    vm.editItem = function (id, title) {
      $http.post('http://localhost:8080/edit-javaee/items/edit', {id: id, title: title});
      location.reload();
    };
  }
}());
