/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('produkts', function () {
  var scope
    , element;

  beforeEach(module('home', 'home/produkts-directive.tpl.html'));

  beforeEach(inject(function ($compile, $rootScope) {
    scope = $rootScope.$new();
    element = $compile(angular.element('<produkts></produkts>'))(scope);
  }));

  it('should have correct text', function () {
    scope.$apply();
    expect(element.isolateScope().produkts.name).toEqual('produkts');
  });
});
