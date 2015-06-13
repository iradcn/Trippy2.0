/**
 * Created by shiriladelsky on 6/13/15.
 */
define(
    ["backbone",
        "jquery",
        "ResponseArea",
        "bootstrap",
    ], function (Backbone, $, ResponseArea) {
        var ResponseAreas = Backbone.Collection.extend({
            model: ResponseArea,
        });
        return ResponseAreas;
    });
