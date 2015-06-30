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

  function ItemCtrl() {
    var vm = this;
    vm.ctrlName = 'ItemCtrl';

    vm.itemList = [
      {title: 'Item 1', location: {x: 10, y: 20}},
      {title: 'Item 2', location: {x: 10, y: 20}},
      {title: 'Item 3', location: {x: 10, y: 20}},
      {title: 'Item 4', location: {x: 10, y: 20}},
      {title: 'Item 5', location: {x: 10, y: 20}},
      {title: 'Item 6', location: {x: 10, y: 20}}
    ];

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
