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
    var CartBase, i;
    CartBase = {};

    CartBase.items = [];

    CartBase.isInCart = isInCart;
    CartBase.addToCart = addToCart;
    CartBase.removeFromCart = removeFromCart;
    CartBase.removeAllItems = removeAllItems;

    return CartBase;

    function idIndex(id) {
      for (i = 0; i < CartBase.items.length; i++) {
        if (CartBase.items[i].id === id) {
          return i;
        }
      }
      return -1;
    }

    function isInCart(id) {
      return idIndex(id) > -1;
    }

    function addToCart(id) {
      Items.getItem(id).then(function (data) {
        if (data !== null) {
          CartBase.items.push(data.item);
        }
      });
    }

    function removeFromCart(id) {
      if (isInCart(id)) {
        CartBase.items.splice(idIndex(id), 1);
      }
    }

    function removeAllItems() {
      CartBase.items.splice(CartBase.items, CartBase.items.length);
    }
  }
}());
