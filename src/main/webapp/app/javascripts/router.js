define([
    'jquery',
    'underscore',
    'backbone',
    'PlacesView',
    'SelectCategoriesView',
    'SelectPropertiesView',
    'ManagePropertiesView',
    'ResponsePlaces',
], function($, _, Backbone, PlacesView, SelectCategoriesView,
            SelectPropertiesView, ManagePropertiesView, ResponsePlaces) {
    var AppRouter = Backbone.Router.extend({
        routes: {
            "places": "placesRoute",
            "manage_properties": "managePropertiesRoute",
            "*actions": "placesRoute",
        },
        initialize: function (options) {
            MyGlobal.collections = {};
            MyGlobal.collections.ResponsePlaces = new ResponsePlaces();

            MyGlobal.models = {};

            MyGlobal.views = {};
            MyGlobal.views.select_categories_view = new SelectCategoriesView();
            MyGlobal.views.select_properties_view = new SelectPropertiesView();

            this.places_view = new PlacesView();
            this.manage_properties_view = new ManagePropertiesView();

            Backbone.history.start();
        },
        placesRoute: function() {
            $('#places-tab').tab('show');
            this.places_view.render();
        },
        managePropertiesRoute: function(){
            $('#manage-properties-tab').tab('show');
            this.manage_properties_view.render();
        },
    });

    return AppRouter;
});
