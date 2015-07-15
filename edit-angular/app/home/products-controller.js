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
      description: 'Very nice!',  // yoo
      price: 110.50,
      images: [
        "https://pbs.twimg.com/media/BhgzCoICcAAvk6Y.jpg",
        "images/green_doge_01"
      ]
    },
    {
      name: 'Red',
      size: 1,
      description: 'Very nice!'
    },
    {
      name: 'Green',
      size: 2,
      description: 'Such green. Much doge. WOW!',
      price: 1.75,
      images: [
        "../images/black_ball_01.jpg",
        "https://i.imgur.com/nAeeF8W.jpg",
        "../images/black_ball_01.jpg"
      ]
    },
    {
      name: 'Black',
      size: 5,
      description: 'Its round and black! Very nice. How much?'
    },
    {
      name: 'Red',
      size: 2,
      description: 'Very nice!'
    },
    {
      name: 'Green',
      size: 4,
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
