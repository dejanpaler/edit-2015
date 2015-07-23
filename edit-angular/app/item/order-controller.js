(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name item.controller:OrderCtrl
   *
   * @description
   *
   */
  angular
    .module('item')
    .controller('OrderCtrl', OrderCtrl);

  function OrderCtrl($http, Cart) {
    var i,
        vm = this;

    vm.ctrlName = 'OrderCtrl';
    vm.cart = Cart.items;
    vm.removeFrom = function (id) {
      Cart.removeFromCart(id);
    };
    vm.checkOut = function () {
      for (i = 0; i < Cart.items.length; i++) {
        $http.post('http://10.80.49.2:8080/edit-javaee/items/get', {id: Cart.items[i].id});
      }
      Cart.removeAllItems();
      location.href = '/#/map';
    };
  }
}());
