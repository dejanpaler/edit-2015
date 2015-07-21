/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('BrickCtrl', function () {
  var ctrl;

  beforeEach(module('item'));

  beforeEach(inject(function ($rootScope, $controller) {
    ctrl = $controller('BrickCtrl');
  }));

  it('should have ctrlName as BrickCtrl', function () {
    expect(ctrl.ctrlName).toEqual('BrickCtrl');
  });
});
