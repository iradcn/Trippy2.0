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
    'SelectPropertiesView',
    'ManagePropertiesView',
    'ResponsePlaces',
    'ResponseAreas',
], function($, _, Backbone, YagoLoadView, PlacesView, AreasView, SelectCategoriesView,
            SelectPropertiesView, ManagePropertiesView, ResponsePlaces, ResponseAreas) {
    var AppRouter = Backbone.Router.extend({
        routes: {
			"places": "placesRoute",
			"dbload": "dbloadRoute",
			"areas": "areasRoute",
            "manage_properties": "managePropertiesRoute",
			"*actions": "placesRoute",
        },
        initialize: function (options) {
            MyGlobal.collections = {};
			MyGlobal.collections.ResponsePlaces = new ResponsePlaces();
            MyGlobal.collections.ResponseAreas = new ResponseAreas();

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
            this.manage_properties_view.render();
        },
        dbloadRoute: function() {
            $('#dbload-tab').tab('show');
            this.dbload_view.render();
        },
    });
    return AppRouter;
});
