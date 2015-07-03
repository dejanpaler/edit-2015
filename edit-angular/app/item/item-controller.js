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
    Items.GetAllItems().then(function(data){
      vm.itemList = data;
    });
    vm.Cart = Cart.Items;

    vm.OnAddCart = function(id) {
      if(Cart.IsInCart(id)){
        Cart.RemoveFromCart(id);
      } else {
        Cart.AddToCart(id);
      }
    }
  }
}());
