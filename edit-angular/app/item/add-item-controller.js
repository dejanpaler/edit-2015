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
      $http.post("http://10.80.49.2:8080/edit-javaee/items/do", {"id": title, "command": "get"})
        .success(function(){
          alert("Success");
        })
        .error(function(){
          alert("Error");
        });
    };

    vm.removeItem = function (id) {
      $http.post("http://10.80.49.2:8080/edit-javaee/items/do", {"id": id, "command": "get"})
        .success(function(){
          alert("Success");
        })
        .error(function(){
          alert("Error");
        });
    };
  }
}());
