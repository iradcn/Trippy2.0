require.config({
   paths: {
       jquery: 'libs/jquery-1.11.2',
       underscore: 'libs/underscore',
       backbone: 'libs/backbone',
       bootstrap: 'libs/bootstrap.min',
       text: 'libs/text',
       async: 'libs/requirejs-plugins/src/async',
       ol: 'libs/ol',

       //views
       MainIndexView: 'views/main',
       MapCirclesView: 'views/map_circles',
       MapDisplayView: 'views/map_display',
	     PlacesView: 'views/places',

       //routers
       MainRouter: 'router'
    	   
   },

    shim:{
        'backbone':{
            deps:['underscore','jquery'],
            exports: 'Backbone'
        },
        'underscore':{
            exports: '_'
        },
        'bootstrap':{
            deps:['jquery']
        },
        'ol':{
            exports: 'ol'
        }

    }


});

require([
    'MainRouter'

], function(MainRouter){
    MyGlobal.routers = {};
    MyGlobal.routers.mainRouter = new MainRouter();
});
