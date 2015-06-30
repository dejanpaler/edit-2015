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

  function OrderCtrl($stateParams, $webSocket) {
    var vm = this;
    vm.ctrlName = 'OrderCtrl';

    vm.itemId = $stateParams.itemId;

    
  }
}());
