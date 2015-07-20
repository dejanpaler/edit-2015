/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('MapCtrl', function () {
  var ctrl;

  beforeEach(module('map'));

  beforeEach(inject(function ($rootScope, $controller) {
    ctrl = $controller('MapCtrl');
  }));

  it('should have ctrlName as MapCtrl', function () {
    expect(ctrl.ctrlName).toEqual('MapCtrl');
  });
});
