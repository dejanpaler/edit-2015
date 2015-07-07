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

  function ItemCtrl($http, Cart, Items) {
    var vm = this;
    vm.ctrlName = 'ItemCtrl';

    vm.itemList = [];
    Items.getAllItems().then(function (data) {
      vm.itemList = data;
    });
    vm.Cart = Cart.items;

    vm.onAddCart = function (id) {
      if (Cart.isInCart(id)) {
        Cart.removeFromCart(id);
      } else {
        Cart.addToCart(id);
      }
    };
  }
}());
