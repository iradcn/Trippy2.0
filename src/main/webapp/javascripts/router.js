/**
 * Created by I313712 on 13/01/2015.
 */
// Filename: router.js
define([
    'jquery',
    'underscore',
    'backbone',
    'MainIndexView'
], function($, _, Backbone, MainIndexView){
    var AppRouter = Backbone.Router.extend({
        routes: {
            "*actions": "defaultRoute"
        },
        initialize: function (options){
            Backbone.history.start();
        },
        defaultRoute: function(){
            this.currView = new MainIndexView();
        }
    });
    return AppRouter;
});