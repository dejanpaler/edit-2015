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

  function OrderCtrl($http, $stateParams) {
    var vm = this;
    vm.ctrlName = 'OrderCtrl';

    var itemId = $stateParams.itemId;

    vm.item = {};
    $http.get('http://localhost:8080/edit-javaee/items/' + itemId)
    .success(function(data){
      vm.item = data;
    })
    .error(function(){
      alert("Napaka");
    });
    
  }
}());
