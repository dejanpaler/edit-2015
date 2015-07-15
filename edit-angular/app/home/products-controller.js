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
      name: 'Black Doge',
      size: 3,
      description: 'Very black. Much rare. Wow!',  // Dogescription
      price: 300,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
      ]
    },
    {
      name: 'Red Item Name',
      size: 1,
      description: 'Lorem ipsum here!',
      price: 100,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
      ]
    },
    {
      name: 'Green Doge',
      size: 2,
      description: 'Such green. Much doge. WOW!',
      price: 200,
      images: [
        "../images/green_doge_01.jpg"
      ]
    },
    {
      name: 'Black Item Name',
      size: 5,
      description: 'Lorem ipsum here!',
      price: 300,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
      ]
     },
    {
      name: 'Red Item Name',
      size: 2,
      description: 'Very nice!'
      price: 300,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
      ]
    },
    {
      name: 'Green',
      size: 4,
      description: 'Very nice!'
      price: 300,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
      ]

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
