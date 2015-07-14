(function () {
  'use strict';

  angular
    .module('home')
    .controller('StoreCtrl', StoreCtrl);

  function StoreCtrl($scope){
    $scope.products = prod;
  }

  var prod = [
    {
      name: 'Black',
      size: 3,
      description: 'Very nice!'
    },
    {
      name: 'Red',
      size: 1,
      description: 'Very nice!'
    },
    {
      name: 'Green',
      size: 2,
      description: 'Very nice!'
    }
  ];


  var expectFriendNames = function(expectedNames, key) {
    element.all(by.repeater(key + ' in friends').column(key + '.name')).then(function(arr) {
      arr.forEach(function(wd, i) {
        expect(wd.getText()).toMatch(expectedNames[i]);
      });
    });
  };
  
}());
