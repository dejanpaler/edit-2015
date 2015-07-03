(function () {
  'use strict';

  /**
   * @ngdoc service
   * @name item.factory:Cart
   *
   * @description
   *
   */
  angular
    .module('item')
    .factory('Cart', Cart);

  function Cart(Items) {
    var CartBase = {};

    CartBase.Items = [];
    var ItemsMap = [];

    CartBase.IsInCart = function(id) {
      return ItemsMap.indexOf(id) > -1;
    }

    CartBase.AddToCart = function(id) {
      Items.GetItem(id).then(function(data) {
        if(data != null){
          CartBase.Items.push(data);
          ItemsMap.push(data.id);
        }
      })
    }

    CartBase.RemoveFromCart = function(id) {
      delete CartBase.Items[ItemsMap.indexOf(id)];
    }



    return CartBase;
  }
}());
