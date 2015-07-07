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

  function OrderCtrl($stateParams, Items) {
    var itemId, vm = this;
    itemId = $stateParams.itemId;
    vm.ctrlName = 'OrderCtrl';

    Items.getItem(itemId).then(function (data) {
      vm.item = data;
    });
  }
}());
