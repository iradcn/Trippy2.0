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
	'ResponsePlaces',
    'SelectPropertiesView',
    'ManagePropertiesView',
], function($, _, Backbone, YagoLoadView, PlacesView, MapCirclesView, MapDisplayView, AreasView, SelectCategoriesView, ResponsePlaces,
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
            this.dbload_view = new YagoLoadView();

            Backbone.history.start();
        },
        placesRoute: function() {
            $('#places-tab').tab('show');
            this.places_view.render();
        },
        areasRoute: function() {
            $('#areas-tab').tab('show');
            this.areas_view.render();
        },
        managePropertiesRoute: function(){
            $('#manage-properties-tab').tab('show');
            //this.manage_properties_view;
        },
        dbloadRoute: function() {
            $('#dbload-tab').tab('show');
            this.dbload_view.render();
        },
    });
    return AppRouter;
});
