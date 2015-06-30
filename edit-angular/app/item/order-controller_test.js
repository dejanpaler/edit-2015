/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('OrderCtrl', function () {
  var ctrl;

  beforeEach(module('item'));

  beforeEach(inject(function ($rootScope, $controller) {
    ctrl = $controller('OrderCtrl');
  }));

  it('should have ctrlName as OrderCtrl', function () {
    expect(ctrl.ctrlName).toEqual('OrderCtrl');
  });
});
