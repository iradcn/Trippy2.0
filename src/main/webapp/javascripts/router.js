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
	'SelectCategoriesView',
], function($, _, Backbone, YagoLoadView, PlacesView, MapCirclesView, MapDisplayView, AreasView, SelectCategoriesView){
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
			this.initNavBar();
			MyGlobal.views = {};
			MyGlobal.views.select_categories_view = new SelectCategoriesView();
			this.places_view = new PlacesView();
			this.areas_view = new AreasView();
            Backbone.history.start();
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
            this.places_view.render();
        },
        dbloadRoute: function(){
            this.currView = new YagoLoadView();
        },
        areasRoute: function(){
            this.areas_view.render();
        },
    });
    return AppRouter;
});
