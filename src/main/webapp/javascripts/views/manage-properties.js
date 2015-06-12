/**
 * Created by shiriladelsky on 6/12/15.
 */
define(
    ["backbone",
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
                'change #select-curr-properties': 'toggleDeleteOption',
                'keydown #renamed-property': 'toggleRenameOption',
            },
            initialize: function () {
                this.props = MyGlobal.collections.properties;
                this.render();
            },
            render: function () {
                var template = _.template(ManagePropertiesTemplate);
                this.$el.html(template());
            },
            toggleAddOption: function (e) {
                var key = e.keyCode || e.charCode;
                var addition = 1;
                if (key == 8 || key == 46) {
                    addition = -1;
                }
                if ($.trim($('#new-property').val()).length + addition == 0) {
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
                if ($.trim($('#renamed-property').val()).length + addition == 0) {
                    $('.rename-property-submit').prop('disabled', true);
                } else {
                    $('.rename-property-submit').prop('disabled', false);
                }
            },
            create: function() {
                $.ajax({
                    method: "GET",
                    url: 'create_property',
                    data: {
                        name: $('#new-property').val(),
                    }
                }).done(function() {
                    $('#new-property').val('');
                    this.props.fetchFromServer();
                }.bind(this))
                    .fail(function(){
                        alert('Unable to Create New Property!');
                    });
            },
            update: function() {
                var prop_id = parseInt($('#select-curr-properties').val()[0]); // should only enable one deletion at a time
                // TODO validate, or add support in multiple deletion

                $.ajax({
                    method: "GET",
                    url: 'modify_property',
                    data: {
                        id: prop_id,
                        name: $('#renamed-property').val(),
                    }
                }).done(function() {
                    $('#renamed-property').val('');
                    this.props.fetchFromServer();
                }.bind(this))
                    .fail(function(){
                        alert('Unable to Rename Selected Property!');
                    });
            },
            delete: function() {
                var prop_id = parseInt($('#select-curr-properties').val()[0]); // should only enable one deletion at a time
                // TODO validate, or add support in multiple deletion

                var prop = this.props.get(prop_id);

                $.ajax({
                    method: "GET",
                    url: 'delete_property',
                    data: {
                        id: prop.id,
                        name: prop.get('name'),
                    }
                }).done(function() {
                    this.props.fetchFromServer();
                }.bind(this))
                    .fail(function(){
                        alert('Unable to Delete Selected Property!');
                    });
            }
        });

        return ManagePropertiesView;
    });

