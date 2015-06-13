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
	'AreasView',
	'SelectCategoriesView',
	'ResponsePlaces',
    'SelectPropertiesView',
    'ManagePropertiesView',
], function($, _, Backbone, YagoLoadView, PlacesView, AreasView, SelectCategoriesView, ResponsePlaces,
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
            MyGlobal.collections = {};
			MyGlobal.collections.ResponsePlaces = new ResponsePlaces();
			MyGlobal.views = {};
			MyGlobal.views.select_categories_view = new SelectCategoriesView();
            MyGlobal.views.select_properties_view = new SelectPropertiesView();
			this.places_view = new PlacesView();
			this.areas_view = new AreasView();
            this.manage_properties_view = new ManagePropertiesView();
            Backbone.history.start();
        },
        mapCirclesRoute: function() {
            this.currView = new MapCirclesView();
        },
        mapDisplayRoute: function() {
            this.currView = new MapDisplayView();
        },
        placesRoute: function(){
            $('#places-tab').tab('show');
            this.places_view.render();
        },
        dbloadRoute: function(){
            $('#dbload-tab').tab('show');
            this.currView = new YagoLoadView();
        },
        areasRoute: function(){
            $('#areas-tab').tab('show');
            this.areas_view.render();
        },
        managePropertiesRoute: function(){
            $('#manage-properties-tab').tab('show');
            this.manage_properties_view.render();
        }
    });
    return AppRouter;
});
