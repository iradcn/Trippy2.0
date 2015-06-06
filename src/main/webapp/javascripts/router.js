/**
 * Created by I313712 on 13/01/2015.
 */
// Filename: router.js
define([
    'jquery',
    'underscore',
    'backbone',
    'YagoLoadView',
	'PlacesView',
    'MapCirclesView',
    'MapDisplayView',
	'AreasView',
], function($, _, Backbone, YagoLoadView, PlacesView, MapCirclesView, MapDisplayView, AreasView){
    var AppRouter = Backbone.Router.extend({
        routes: {
            "map_circles": "mapCirclesRoute",
            "map_display": "mapDisplayRoute",
			"places": "placesRoute",
			"dbload": "dbloadRoute",
			"areas": "areasRoute",
			"*actions": "placesRoute",
        },
        initialize: function (options) {
            Backbone.history.start();
			this.initNavBar();
        },
        defaultRoute: function() {
            this.currView = new MainIndexView();
        },
		initNavBar: function () {
			$('.myTab a').click(function () {
			  $(this).tab('show')
			})
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
            this.currView = new YagoLoadView();
        },
        areasRoute: function(){
            this.currView = new AreasView();
        },
    });
    return AppRouter;
});
