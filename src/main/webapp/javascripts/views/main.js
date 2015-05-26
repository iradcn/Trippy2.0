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
                'click .submit': 'onYagoUpdate'
            },
            initialize: function () {
                this.render();

            },
            onYagoUpdate: function () {
                $('.submit').attr('disabled','disabled');
                $.ajax({
                    url:'service'
                    }).done(function(data){
                        this.showProgressBar();
                        this.fetchProgress();
                    }.bind(this));
            },
            render: function () {
                var template = _.template(LandingPageTemplate);
                this.$el.html(template());
            },
            fetchProgress: function(){
                $.ajax({
                    url:'status'
                }).done(function(data){
                    if (data){
                        this.updateProgressBar(data);
                    }
                    if (!data || data.read != data.total)
                        setTimeout(function(){this.fetchProgress();},60000);
                }.bind(this));
            },
            showProgressBar: function(){
                $('.progress').show();

            },
            updateProgressBar: function(data){
                if (data && data.read && data.total)
                    $('.progress-bar').css('width',data.read/data.total +'%');
            }
        })

        return MainIndexView;
    });