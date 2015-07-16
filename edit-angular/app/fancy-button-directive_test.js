/*global describe, beforeEach, it, expect, inject, module*/
'use strict';

describe('fancyButton', function () {
  var scope
    , element;

  beforeEach(module('editAngular', '/fancy-button-directive.tpl.html'));

  beforeEach(inject(function ($compile, $rootScope) {
    scope = $rootScope.$new();
    element = $compile(angular.element('<fancy-button></fancy-button>'))(scope);
  }));

  it('should have correct text', function () {
    scope.$apply();
    expect(element.isolateScope().fancyButton.name).toEqual('fancyButton');
  });
});
