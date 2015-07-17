/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('AddItemCtrl', function () {
  var ctrl;

  beforeEach(module('item'));

  beforeEach(inject(function ($rootScope, $controller) {
    ctrl = $controller('AddItemCtrl');
  }));

  it('should have ctrlName as AddItemCtrl', function () {
    expect(ctrl.ctrlName).toEqual('AddItemCtrl');
  });
});
