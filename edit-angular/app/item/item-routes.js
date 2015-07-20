(function () {
  'use strict';

  angular
    .module('item')
    .config(config);

  function config($stateProvider) {
    $stateProvider
      .state('items', {
        url: '/items',
        templateUrl: 'item/items.tpl.html',
        controller: 'ItemCtrl',
        controllerAs: 'vm'
      })
      .state('order', {
        url: '/order/:itemId',
        templateUrl: 'item/order.tpl.html',
        controller: 'OrderCtrl',
        controllerAs: 'vm'
      })
      .state('checkout', {
        url: '/checkout',
        templateUrl: 'item/checkout.tpl.html',
        controller: 'OrderCtrl',
        controllerAs: 'vm'
      })
      .state('add-item', {
        url: '/add-item',
        templateUrl: 'item/add-item.tpl.html',
        controller: 'AddItemCtrl',
        controllerAs: 'vm'
      });
  }
}());
