define([
  "backbone",
  "jquery",
  "Properties",
  "bootstrap",
  "select",
], function (Backbone, $, Properties) {
  var SelectPropertiesView = Backbone.View.extend({
    el: "#select-curr-properties",
    initialize: function () {
      this.collection = new Properties();
      this.collection.on("all", this.render, this);
      MyGlobal.collections.properties = this.collection;
    },
    render: function () {

      $("#select-curr-properties").select2({
        placeholder: "Select properties",
      });
      this.collection.each(function(prop) {
        $("#select-curr-properties").append( "<option value='" + prop.id + "'>" + prop.get('name') +  "</option>");
        $("#select-curr-properties").select2("destroy");
        $("#select-curr-properties").select2({
          placeholder: "Select properties",
        });
      }, this);

      return this;
    },
  });

  return SelectPropertiesView;
});

