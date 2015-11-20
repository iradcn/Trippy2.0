define([
  "backbone",
  "jquery",
  "bootstrap",
], function (Backbone, $){
  var Vote = Backbone.Model.extend({
    urlRoot: '/app/vote/request',
  });
  return Vote;
});
