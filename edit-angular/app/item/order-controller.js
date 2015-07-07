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

  function OrderCtrl(Cart, Brick) {
    var vm = this;
    vm.ctrlName = 'OrderCtrl';

    vm.cart = Cart.items;
    vm.processOrder = function () {
      Brick.sendJson(Cart.items);
    };
  }
}());
