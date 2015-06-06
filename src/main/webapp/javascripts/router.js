/**
 * Created by I313712 on 13/01/2015.
 */
// Filename: router.js
define([
    'jquery',
    'underscore',
    'backbone',
    'MainIndexView',
    'MapCirclesView',
    'MapDisplayView'
], function($, _, Backbone, MainIndexView, MapCirclesView, MapDisplayView){
    var AppRouter = Backbone.Router.extend({
        routes: {
            "map_circles": "mapCirclesRoute",
            "map_display": "mapDisplayRoute",
			"places": "placesRoute",
			"dbload": "dbloadRoute",
			"*actions": "placesRoute",
        },
        initialize: function (options) {
            Backbone.history.start();
        },
        defaultRoute: function() {
            this.currView = new MainIndexView();
        },
        mapCirclesRoute: function() {
            this.currView = new MapCirclesView();
        },
        mapDisplayRoute: function() {
            this.currView = new MapDisplayView();
        },
        placesRoute: function(){
            this.currView = new PlacesView();
        },
        dbloadRoute: function(){
            this.currView = new MainIndexView();
        },
    });
    return AppRouter;
});
