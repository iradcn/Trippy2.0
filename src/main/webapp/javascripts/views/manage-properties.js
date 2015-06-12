/**
 * Created by shiriladelsky on 6/12/15.
 */
define(
    ["backbone",
        "jquery",
        "text!templates/manage-properties.html",
        "bootstrap",
    ], function (Backbone, $, PropertiesTemplate) {
        var ManagePropertiesView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .add-property-submit': 'create',
                'click .rename-property-submit': 'update',
                'click .delete-property-submit': 'delete',
            },
            initialize: function () {
                this.catView = MyGlobal.views.select_categories_view;
                this.render();

            },
            render: function () {
                var template = _.template(PropertiesTemplate);
                this.$el.html(template());
                this.catView.render();
            },
            create: function() {
                $.ajax({
                    method: "GET",
                    url: 'create_property',
                    data: {
                        name: $('#new-property').val(),
                    }
                }).done(function(data) {
                    console.log(data);
                }.bind(this))
                    .fail(function(){
                        alert('Unable to Create New Property!');
                    });
            },
            update: function() {
                var prop_id = parseInt($('#properties').val()[0]); // should only enable one deletion at a time
                // TODO validate, or add support in multiple deletion

                console.log(MyGlobal.collections.properties.get(prop_id));

                $.ajax({
                    method: "GET",
                    url: 'modify_property',
                    data: {
                        id: prop_id,
                        name: $('#renamed-property').val(),
                    }
                }).done(function(data) {
                    console.log(data);
                }.bind(this))
                    .fail(function(){
                        alert('Unable to Rename Selected Property!');
                    });
            },
            delete: function() {
                var prop_id = parseInt($('#properties').val()[0]); // should only enable one deletion at a time
                // TODO validate, or add support in multiple deletion

                var prop = MyGlobal.collections.properties.get(prop_id);

                $.ajax({
                    method: "GET",
                    url: 'delete_property',
                    data: {
                        id: prop.id,
                        name: prop.get('name'),
                    }
                }).done(function(data) {
                    console.log(data);
                }.bind(this))
                    .fail(function(){
                        alert('Unable to Delete Selected Property!');
                    });
            }
        });

        return ManagePropertiesView;
    });

