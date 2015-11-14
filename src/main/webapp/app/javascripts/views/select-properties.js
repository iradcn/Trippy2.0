define([
  "backbone",
  "jquery",
  "Properties",
  "bootstrap",
], function (Backbone, $, Properties) {
  var SelectPropertiesView = Backbone.View.extend({
    el: ".select-curr-properties",
    initialize: function () {
      this.collection = new Properties();
      this.collection.on("all", this.render, this);
      MyGlobal.collections.properties = this.collection;
    },
    render: function () {
      $(".select-curr-properties").html("");
      this.collection.each(function(prop) {
        $(".select-curr-properties").append( "<option value='" + prop.id + "'>" + prop.get('name') +  "</option>");
      }, this);

      return this;
    },
  });

  return SelectPropertiesView;
});

