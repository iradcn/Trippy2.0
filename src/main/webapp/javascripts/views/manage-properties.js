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
                'click .properties-submit': 'propertiesSubmit',
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
            propertiesSubmit: function () {
                console.log("Pressed Properties Submit!");
            },

        });

        return ManagePropertiesView;
    });

