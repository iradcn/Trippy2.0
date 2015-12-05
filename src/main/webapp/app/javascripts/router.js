define([
    'jquery',
    'underscore',
    'backbone',
    'PlacesView',
    'SelectCategoriesView',
    'SelectPropertiesView',
    'ManagePropertiesView',
    'VoteView',
    'ResponsePlaces',
    'Vote'
], function($, _, Backbone, PlacesView, SelectCategoriesView,
            SelectPropertiesView, ManagePropertiesView, VoteView, ResponsePlaces, Vote) {
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
            this.vote_view = new VoteView();

            Backbone.history.start();
        },
        placesRoute: function() {
            $('#places-tab').tab('show');
            this.places_view.render();

            /*
            MyGlobal.models.vote = new Vote();
            MyGlobal.models.vote.on("sync", function()
            {
                var voteView = new VoteView();
                voteView.render();
            });
            MyGlobal.models.vote.fetch({data: { placeId : "AXSDEFEEEE"}});
            */
        },
        managePropertiesRoute: function(){
            $('#manage-properties-tab').tab('show');
            this.manage_properties_view.render();
        },
    });

    return AppRouter;
});
