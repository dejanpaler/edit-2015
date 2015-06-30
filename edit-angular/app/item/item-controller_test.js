/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('ItemCtrl', function () {
  var ctrl;

  beforeEach(module('item'));

  beforeEach(inject(function ($rootScope, $controller) {
    ctrl = $controller('ItemCtrl');
  }));

  it('should have ctrlName as ItemCtrl', function () {
    expect(ctrl.ctrlName).toEqual('ItemCtrl');
  });
});
