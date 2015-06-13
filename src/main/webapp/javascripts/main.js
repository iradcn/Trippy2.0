require.config({
   paths: {
       jquery: 'libs/jquery-1.11.2',
       underscore: 'libs/underscore',
       backbone: 'libs/backbone',
       bootstrap: 'libs/bootstrap.min',
       text: 'libs/text',
       async: 'libs/requirejs-plugins/src/async',
       ol: 'libs/ol',

	   // Models
	   Category: 'models/category',
       Property: 'models/property',
       ResponsePlace: 'models/response-place',
       ResponseArea: 'models/response-area',

	   // Collections
	   Categories: 'collections/categories',
       Properties: 'collections/properties',
       ResponsePlaces: 'collections/response-places',
       ResponseAreas: 'collections/response-areas',

       //views
       YagoLoadView: 'views/yagoload',
	   PlacesView: 'views/places',
	   AreasView: 'views/areas',
       SelectCategoriesView: 'views/select-categories',
       SelectPropertiesView: 'views/select-properties',
       ManagePropertiesView: 'views/manage-properties',
	   ResponsePlaceView: 'views/response-place',

       //routers
       MainRouter: 'router',
   },

    shim: {
        'backbone': {
            deps:['underscore','jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'bootstrap': {
            deps:['jquery']
        },
        'ol':{
            exports: 'ol'
        }

    }


});

require([
    'MainRouter'

], function(MainRouter) {
    MyGlobal.routers = {};
    MyGlobal.routers.mainRouter = new MainRouter();
});

