/**
 * Created by I313712 on 13/01/2015.
 */
// Filename: router.js
define([
    'jquery',
    'underscore',
    'backbone',
    'MainIndexView',
    'MapDisplayView'
], function($, _, Backbone, MainIndexView, MapDisplayView){
    var AppRouter = Backbone.Router.extend({
        routes: {
            "map_display": "mapDisplayRoute",
            "*actions": "defaultRoute"
        },
        initialize: function (options) {
            Backbone.history.start();
        },
        defaultRoute: function() {
            this.currView = new MainIndexView();
        },
        mapDisplayRoute: function() {
            this.currView = new MapDisplayView();
        }
    });
    return AppRouter;
});