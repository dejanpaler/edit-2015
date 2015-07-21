(function () {
  'use strict';

  /**
   * @ngdoc object
   * @name item.controller:BrickCtrl
   *
   * @description
   *
   */
  angular
    .module('map')
    .controller('BrickCtrl', BrickCtrl);

  function BrickCtrl(Brick) {
    var vm = this;
    vm.ctrlName = 'BrickCtrl';

    vm.location = function () {
      console.log(Brick.command);
    }
  }
}());
