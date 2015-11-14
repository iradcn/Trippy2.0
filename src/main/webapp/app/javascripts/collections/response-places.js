define([
  "backbone",
  "jquery",
  "ResponsePlace",
  "bootstrap",
], function (Backbone, $, ResponsePlace) {
  var ResponsePlaces = Backbone.Collection.extend({
    model: ResponsePlace,
  });
  return ResponsePlaces;
});
