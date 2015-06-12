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
    'SelectPropertiesView',
    'ManagePropertiesView',
], function($, _, Backbone, YagoLoadView, PlacesView, MapCirclesView, MapDisplayView, AreasView, SelectCategoriesView,
            SelectPropertiesView, ManagePropertiesView){
    var AppRouter = Backbone.Router.extend({
        routes: {
            "map_circles": "mapCirclesRoute",
            "map_display": "mapDisplayRoute",
			"places": "placesRoute",
			"dbload": "dbloadRoute",
			"areas": "areasRoute",
            "manage_properties": "managePropertiesRoute",
			"*actions": "placesRoute",
        },
        initialize: function (options) {
			this.initNavBar();
            MyGlobal.collections = {};
			MyGlobal.views = {};
			MyGlobal.views.select_categories_view = new SelectCategoriesView();
            MyGlobal.views.select_properties_view = new SelectPropertiesView();
			this.places_view = new PlacesView();
			this.areas_view = new AreasView();
            this.manage_properties_view = new ManagePropertiesView();
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
        managePropertiesRoute: function(){
            this.manage_properties_view.render();
        }
    });
    return AppRouter;
});
