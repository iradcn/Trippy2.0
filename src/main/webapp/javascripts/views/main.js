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
                'click .submit': 'onYagoUpdate',
				'click .refresh-yago-data': 'startUpdateModal'
            },
            initialize: function () {
                this.render();

            },
			startUpdateModal: function () {
				console.log("working!");
				$('.modal').modal();
			},
            onYagoUpdate: function () {
                this.startLoading();
                $.ajax({
                    url:'import'
                    }).success(function(data){
                        this.startLoading();
                        setTimeout(function(){this.fetchProgress();}.bind(this),10000);
                    }.bind(this))
                    .fail(function(){
                        alert('cannot update at this time. this can happen if there is a connection problem, or if there is already an ongoing update');
                    });
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
                        if (data.error==true){
                            alert('failure!');
                            this.doneLoading();
                            return;
                        }
                        else if(data.local_status_instance==false){
                            alert('success!');
                            this.doneLoading();
                            return;
                        }
                        this.updateProgressBar(data);

                    }
                    setTimeout(function(){this.fetchProgress();}.bind(this),3000);
                }.bind(this))
                    .fail(function(){
                        alert('failure');
                    }.bind(this));
            },
            startLoading: function(){
                $('.progress-bar').css('width','0%');
                $('.progress').show();
                $('.submit').attr('disabled',true);

            },
            doneLoading: function(){
                $('.progress').hide();
                $('.submit').attr('disabled',false);

            },
            updateProgressBar: function(data){
                if (data && data.local_total_read && data.local_status_instance && data.local_read)
                    $('.progress-bar').css('width',100*data.local_read/data.local_total_read +'%');
            }
        })

        return MainIndexView;
    });
