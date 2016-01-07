require.config({
  paths: {
    jquery: '/common/javascripts/libs/jquery-1.11.2',
    jqueryui: '/common/javascripts/libs/jquery-ui.min',
    underscore: 'libs/underscore',
    backbone: 'libs/backbone',
    bootstrap: '/common/javascripts/libs/bootstrap.min',
    multiselect: '/common/javascripts/libs/bootstrap-multiselect',
    select: '/common/javascripts/libs/select2',
    text: 'libs/text',
    async: 'libs/requirejs-plugins/src/async',
    ol: 'libs/ol',

    // Models
    Category: 'models/category',
    Property: 'models/property',
    ResponsePlace: 'models/response-place',
    SingleResponsePlace: 'models/single-response-place',

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
    'jquery': {
      exports: ['jQuery','$']
    },
    'underscore': {
      exports: '_'
    },
    'bootstrap': {
      deps:['jquery']
    },
    'ol':{
      exports: 'ol'
    },
    'select': {
      deps:['jquery']
    }

  }
});

require([
  'MainRouter'
], function(MainRouter) {
  MyGlobal.routers = {};
  MyGlobal.routers.mainRouter = new MainRouter();
});

