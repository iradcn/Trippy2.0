define([
  "backbone",
  "jquery",
  "text!templates/manage-properties.html",
  "bootstrap",
], function (Backbone, $, ManagePropertiesTemplate) {
  var ManagePropertiesView = Backbone.View.extend({
    el: ".body-container",
    events: {
      'click .add-property-submit': 'create',
      'click .rename-property-submit': 'update',
      'click .delete-property-submit': 'delete',
      'keydown #new-property': 'toggleAddOption',
      'change #manage-select-curr-properties': 'toggleDeleteOption',
      'keydown #renamed-property': 'toggleRenameOption',
    },
    initialize: function () {
      this.props = MyGlobal.collections.properties;
    },
    render: function () {
      var template = _.template(ManagePropertiesTemplate);
      this.$el.html(template());
      MyGlobal.views.select_properties_view.render();
    },
    create: function() {
      $.ajax({
        method: "GET",
        url: 'create_property',
        data: {
            name: $('#new-property').val(),
        }
      }).done(function() {
        $('.alerts-row').html(
          '<div class="alert alert-success alert-dismissable" role="alert">' +
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          'Property was added successfully!</div>');
        $('#new-property').val('');
        $('.add-property-submit').prop('disabled', true);
        $('.rename-property-submit').prop('disabled', true);
        $('#renamed-property').prop('disabled', true);
        $('.delete-property-submit').prop('disabled', true);
        this.props.fetchFromServer();
      }.bind(this)).fail(function(){
        $('.alerts-row').html(
          '<div class="alert alert-danger alert-dismissable" role="alert">' +
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          'Unable to add new property!</div>');
      });
    },
    update: function() {
      var prop_id = parseInt($('#manage-select-curr-properties').val()); // should only enable one deletion at a time
      $.ajax({
        method: "GET",
        url: 'modify_property',
        data: {
          id: prop_id,
          name: $('#renamed-property').val(),
        }
      }).done(function() {
        $('.alerts-row').html(
          '<div class="alert alert-success alert-dismissable" role="alert">' +
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          'Property was renamed successfully!</div>');
        $('#renamed-property').val('');
        $('.add-property-submit').prop('disabled', true);
        $('.rename-property-submit').prop('disabled', true);
        $('#renamed-property').prop('disabled', true);
        $('.delete-property-submit').prop('disabled', true);
        this.props.fetchFromServer();
      }.bind(this)).fail(function(){
        $('.alerts-row').html(
          '<div class="alert alert-danger alert-dismissable" role="alert">' +
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          'Unable to rename selected property!</div>');
      });
    },
    delete: function() {
      var prop_id = parseInt($('#manage-select-curr-properties').val()); // should only enable one deletion at a time
      var prop = this.props.get(prop_id);

      $.ajax({
        method: "GET",
        url: 'delete_property',
        data: {
          id: prop.id,
          name: prop.get('name'),
        }
      }).done(function() {
        $('.alerts-row').html(
          '<div class="alert alert-success alert-dismissable" role="alert">' +
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          'Property was deleted successfully!</div>');
        $('.add-property-submit').prop('disabled', true);
        $('.rename-property-submit').prop('disabled', true);
        $('#renamed-property').prop('disabled', true);
        $('.delete-property-submit').prop('disabled', true);
        this.props.fetchFromServer();
      }.bind(this)).fail(function(){
        $('.alerts-row').html(
          '<div class="alert alert-danger alert-dismissable" role="alert">' +
          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          'Unable to delete selected property!</div>');
      });
    },
    toggleAddOption: function (e) {
      var key = e.keyCode || e.charCode;
      var addition = 1;
      if (key == 8 || key == 46) {
        addition = -1;
      }
      if ($.trim($('#new-property').val()).length + addition <= 0) {
        $('.add-property-submit').prop('disabled', true);
      } else {
        $('.add-property-submit').prop('disabled', false);
      }
    },
    toggleDeleteOption: function () {
      $('#renamed-property').prop('disabled', false);
      $('.delete-property-submit').prop('disabled', false);
    },
    toggleRenameOption: function (e) {
      var key = e.keyCode || e.charCode;
      var addition = 1;
      if (key == 8 || key == 46) {
        addition = -1;
      }
      if ($.trim($('#renamed-property').val()).length + addition <= 0) {
        $('.rename-property-submit').prop('disabled', true);
      } else {
        $('.rename-property-submit').prop('disabled', false);
      }
    },
  });

  return ManagePropertiesView;
});
