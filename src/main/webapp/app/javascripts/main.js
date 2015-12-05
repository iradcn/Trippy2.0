require.config({
  paths: {
    jquery: '/common/javascripts/libs/jquery-1.11.2',
    jqueryui: '/common/javascripts/libs/jquery-ui.min',
    underscore: 'libs/underscore',
    backbone: 'libs/backbone',
    bootstrap: '/common/javascripts/libs/bootstrap.min',
    text: 'libs/text',
    async: 'libs/requirejs-plugins/src/async',
    ol: 'libs/ol',

    // Models
    Category: 'models/category',
    Property: 'models/property',
    ResponsePlace: 'models/response-place',

    // Collections
    Categories: 'collections/categories',
    Properties: 'collections/properties',
    ResponsePlaces: 'collections/response-places',

    // Views
    PlacesView: 'views/places',
    SelectCategoriesView: 'views/select-categories',
    SelectPropertiesView: 'views/select-properties',
    ManagePropertiesView: 'views/manage-properties',
    ResponsePlaceView: 'views/response-place',

    // Routers
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

