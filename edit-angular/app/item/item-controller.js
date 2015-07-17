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
    vm.error = 0;
    Items.getAllItems().then(function (data) {
      if (data.error) {
        vm.error = data.error;
      } else {
        vm.itemList = data.items;
      }
    });
    vm.cart = Cart.items;

    vm.onAddCart = function (id) {
      if (Cart.isInCart(id)) {
        Cart.removeFromCart(id);
      } else {
        Cart.addToCart(id);
      }
    };

    vm.sendId = function(id) {
      $http.post("http://10.80.49.6:8080/edit-javaee/items/getItem", id)
        .success(function(){
          alert("Success");
        })
        .error(function(){
          alert("Error");
        });
    };

    vm.isInCart = Cart.isInCart;
  }
}());
