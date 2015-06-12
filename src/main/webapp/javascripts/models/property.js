/**
 * Created by shiriladelsky on 6/12/15.
 */
define(
    ["backbone",
        "jquery",
        "bootstrap",
    ], function (Backbone, $) {
        var Property = Backbone.Model.extend({
            idAttribute: "id",
        });
        return Property;
    });
