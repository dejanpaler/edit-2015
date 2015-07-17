(function () {
  'use strict';

  /**
   * @ngdoc directive
   * @name item.directive:products
   * @restrict EA
   * @element
   *
   * @description
   *
   * @example
     <example module="item">
       <file name="index.html">
        <products></products>
       </file>
     </example>
   *
   */
  angular
    .module('item')
    .directive('products', products);

  function products() {
    return {
      restrict: 'EA',
      scope: {},
      templateUrl: 'item/products-directive.tpl.html',
      replace: false,
      controller: function ItemCtrl($scope, $http, Cart, Items) {
        $scope.ctrlName = 'ItemCtrl';

        $scope.itemList = [];
        $scope.error = 0;
        Items.getAllItems().then(function (data) {
          if (data.error) {
            $scope.error = data.error;
          } else {
            $scope.itemList = data.items;
          }
        });
        $scope.cart = Cart.items;

        $scope.onAddCart = function (id) {
          if (Cart.isInCart(id)) {
            Cart.removeFromCart(id);
          } else {
            Cart.addToCart(id);
          }
        };

        $scope.sendId = function(id) {
          $http.post("http://10.80.49.6:8080/edit-javaee/items/getItem", {param:id} )
            .success(function(){
              alert("Success");
            })
            .error(function(){
              alert("Error");
            });
        };

        $scope.isInCart = Cart.isInCart;
      },
      link: function (scope, element, attrs) {
        /*jshint unused:false */
        /*eslint "no-unused-vars": [2, {"args": "none"}]*/
      }
    };
  }
}());
