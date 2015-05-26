/**
 * Created by Amir on 14/03/2015.
 */
define(
    ["backbone",
        "jquery",
        "text!templates/landing-page-template.html",
        "bootstrap",
    ], function (Backbone, $, LandingPageTemplate) {
        var MainIndexView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .submit': 'formSubmitted'
            },
            initialize: function () {
                this.render();

            },
            formSubmitted: function () {
                $.ajax({
                    url:'service'
                    }).done(function(data){

                    }.bind(this));
            },
            render: function () {
                var template = _.template(LandingPageTemplate);
                this.$el.html(template());
            }
        })
        return MainIndexView;
    });