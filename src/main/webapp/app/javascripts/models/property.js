define([
  "backbone",
  "jquery",
  "bootstrap",
], function (Backbone, $) {
  var Property = Backbone.Model.extend({
    idAttribute: "id",
  });
  return Property;
});
