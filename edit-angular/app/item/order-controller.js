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
    var vm = this;
    vm.ctrlName = 'OrderCtrl';

    var itemId = $stateParams.itemId;

    Items.GetItem(itemId).then(function(data){
      vm.item = data;
    });
    
  }
}());
