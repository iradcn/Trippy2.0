/**
 * Created by shiriladelsky on 6/12/15.
 */
define(
    ["backbone",
        "jquery",
        "Properties",
        "bootstrap",
    ], function (Backbone, $, Properties) {
        var SelectPropertiesView = Backbone.View.extend({
            el: "#properties",
            initialize: function () {
                this.collection = new Properties();
                this.collection.on("all", this.render, this);
                MyGlobal.collections.properties = this.collection;
            },
            render: function () {
                this.$el.html("");
                this.populateProperties();
            },
            populateProperties: function () {
                this.collection.each(function (cat) {
                    $('#categories').append( "<option value='" + cat.id + "'>" + cat.get('name') +  "</option>");
                }, this);
            },
        });

        return SelectPropertiesView;
    });

