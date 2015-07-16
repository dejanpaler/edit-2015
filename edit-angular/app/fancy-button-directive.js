(function () {
  'use strict';

  /**
   * @ngdoc directive
   * @name editAngular.directive:fancyButton
   * @restrict EA
   * @element
   *
   * @description
   *
   * @example
     <example module="editAngular">
       <file name="index.html">
        <fancy-button></fancy-button>
       </file>
     </example>
   *
   */
  angular
    .module('editAngular')
    .directive('fancyButton', fancyButton);

  function fancyButton() {
    return {
      restrict: 'EA',
      scope: {},
      templateUrl: '/fancy-button-directive.tpl.html',
      replace: false,
      controllerAs: 'fancyButton',
      controller: function () {
        var vm = this;
        vm.name = 'fancyButton';
      },
      link: function (scope, element, attrs) {
        /*jshint unused:false */
        /*eslint "no-unused-vars": [2, {"args": "none"}]*/
      }
    };
  }
}());
