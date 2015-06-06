/**
 * Created by I313712 on 13/01/2015.
 */
// Filename: router.js
define([
    'jquery',
    'underscore',
    'backbone',
    'YagoLoadView',
	'PlacesView'
], function($, _, Backbone, YagoLoadView, PlacesView){
    var AppRouter = Backbone.Router.extend({
        routes: {
			"places": "placesRoute",
			"dbload": "dbloadRoute",
			"*actions": "placesRoute",
        },
        initialize: function (options){
            Backbone.history.start();
        },
        placesRoute: function(){
            this.currView = new PlacesView();
        },
        dbloadRoute: function(){
            this.currView = new YagoLoadView();
        },
    });
    return AppRouter;
});
