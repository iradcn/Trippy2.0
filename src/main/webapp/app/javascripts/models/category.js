define([
  "backbone",
  "jquery",
  "bootstrap",
], function (Backbone, $){
  var Category = Backbone.Model.extend({
    idAttribute: "id",
  });
  return Category;
});
