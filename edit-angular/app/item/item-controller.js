(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name item.controller:ItemCtrl
   *
   * @description
   *
   */
  angular
    .module('item')
    .controller('ItemCtrl', ItemCtrl);

  function ItemCtrl($http) {
    var vm = this;
    vm.ctrlName = 'ItemCtrl';

    vm.itemList = [];

    $http.get('http://localhost:8080/edit-javaee/items/list')
    .success(function(data) {
      vm.itemList = data;
    })
    .error(function(data) {
      alert("you suck");
      console.log(data);
    })


    vm.basket = [];

    vm.BasketChange = function(id) {
      var idIndex = vm.basket.indexOf(id);
      if(idIndex > -1)
        vm.basket.splice(idIndex, 1);
      else
        vm.basket.push(id);
    }
  }
}());
