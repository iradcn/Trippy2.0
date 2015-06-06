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
    'MapDisplayView'
], function($, _, Backbone, YagoLoadView, PlacesView, MapDisplayView){
    var AppRouter = Backbone.Router.extend({
        routes: {
            "map_display": "mapDisplayRoute",
			"places": "placesRoute",
			"dbload": "dbloadRoute",
			"*actions": "placesRoute",
        },
        initialize: function (options) {
            Backbone.history.start();
			this.initNavBar();
        },
		initNavBar: function () {
			$('.myTab a').click(function () {
			  $(this).tab('show')
			})
		},
        mapDisplayRoute: function() {
            this.currView = new MapDisplayView();
        }
        placesRoute: function(){
            this.currView = new PlacesView();
        },
        dbloadRoute: function(){
            this.currView = new YagoLoadView();
        },
    });
    return AppRouter;
});
