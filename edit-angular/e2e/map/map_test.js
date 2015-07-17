/*global describe, beforeEach, it, browser, expect */
'use strict';

var MapPagePo = require('./map.po');

describe('Map page', function () {
  var mapPage;

  beforeEach(function () {
    mapPage = new MapPagePo();
    browser.get('/#/map');
  });

  it('should say MapCtrl', function () {
    expect(mapPage.heading.getText()).toEqual('map');
    expect(mapPage.text.getText()).toEqual('MapCtrl');
  });
});
