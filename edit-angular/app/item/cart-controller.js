(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name item.controller:CartCtrl
   *
   * @description
   *
   */
  angular
    .module('item')
    .controller('CartCtrl', CartCtrl);

  function CartCtrl(Cart) {
    var vm = this;
    vm.ctrlName = 'CartCtrl';
    vm.cart = Cart.items;
  }
}());
