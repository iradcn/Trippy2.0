define([
  "backbone",
  "jquery",
  "Categories",
  "bootstrap",
], function (Backbone, $, Categories) {
  var SelectCategoriesView = Backbone.View.extend({
    el: ".select-curr-categories",
    initialize: function () {
      this.collection = new Categories();
      this.collection.on("all", this.render, this);
      MyGlobal.collections.categories = this.collection;
    },
    render: function () {

      $("#select-curr-categories").select2({
        placeholder: "Select categories",
      });

      this.collection.each(function (cat) {
        $('#select-curr-categories').append( "<option value='" + cat.id + "'>" + cat.get('representationName') +  "</option>");
        $("#select-curr-categories").select2("destroy");
        $("#select-curr-categories").select2({
          placeholder: "Select categories",
        });
      }, this);;
    },
  });

  return SelectCategoriesView;
});
