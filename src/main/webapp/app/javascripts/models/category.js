define([
  "backbone",
  "jquery",
  "bootstrap",
], function (Backbone, $){
  var Category = Backbone.Model.extend({
    idAttribute: "yagoId",
  });
  return Category;
});
