/**
 * Created by Amir on 14/03/2015.
 */
define(
    ["backbone",
        "jquery",
        "text!templates/yago-load-template.html",
        "bootstrap",
    ], function (Backbone, $, YagoLoadTemplate) {
        var YagoLoadView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .submit': 'onYagoUpdate',
            },
            initialize: function () {
                this.render();
            },
            render: function () {
                var template = _.template(YagoLoadTemplate);
                this.$el.html(template());
            },
            onYagoUpdate: function () {
                $('.alerts-row').html('');
                $.ajax({
                    url:'import'
                    }).success(function() {
                        this.startLoading();
                        setTimeout(function(){this.fetchProgress();}.bind(this),1000);
                    }.bind(this))
                    .fail(function(){
						$.ajax({
							url:'status'
						}).done(function(data){
							$('.alerts-row').html(
									'<div class="alert alert-warning alert-dismissable" role="alert">' +
									'<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
									'<span aria-hidden="true">&times;</span></button>' +
									'<strong>Update already in progress!</strong>'); 
                            $('.yago-btn-row').hide();
							$('.progress').show();
							setTimeout(function(){this.fetchProgress();}.bind(this),1000);
						}.bind(this))
							.fail(function(){
								$('.alerts-row').html(
									'<div class="alert alert-danger alert-dismissable" role="alert">' +
									'<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
									'<span aria-hidden="true">&times;</span></button>' +
									'<strong>Cannot update at this time!</strong> There is probably a connection problem.'); 
							}.bind(this));
                    }.bind(this));
            },
            fetchProgress: function(){
                $.ajax({
                    url:'status'
                }).done(function(data){

                    if (data){
                        if (data.error==true){
                            $('.alerts-row').html(
                                '<div class="alert alert-danger alert-dismissable" role="alert">' +
                                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                                '<span aria-hidden="true">&times;</span></button>' +
                                '<strong>Failed updating!</strong> See console log for more details.</div>');
                            console.log(data);
                            this.doneLoading();
                            return;
                        }
                        else if(data.local_status_instance == false){
                            $('.alerts-row').html(
                                '<div class="alert alert-success alert-dismissable" role="alert">' +
                                '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                                '<span aria-hidden="true">&times;</span></button>' +
                                '<strong>Finished updating!</strong> Updated data successfully.</div>');
                            this.doneLoading();
                            $('.yago-btn-row').hide();
                            return;
                        }
                        this.updateProgressBar(data);

                    }
                    setTimeout(function(){this.fetchProgress();}.bind(this),3000);
                }.bind(this))
                    .fail(function(){
                        $('.alerts-row').html(
                            '<div class="alert alert-danger alert-dismissable" role="alert">' +
                            '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                            '<span aria-hidden="true">&times;</span></button>' +
                            '<strong>Cannot update at this time!</strong> Either there is a connection problem, ' +
                            'or there is already an ongoing update</div>');
                        this.doneLoading();
                    }.bind(this));
            },
            startLoading: function(){
                $('.progress-bar').css('width','0%');
                $('.progress').show();
                $('.yago-text-row').show();
                $('.yago-btn-row').hide();
            },
            doneLoading: function(){
                $('.progress').hide();
                $('.yago-text-row').hide();
                $('.yago-btn-row').show();
            },
            updateProgressBar: function(data){
                if (data && data.local_total_read && data.local_status_instance && data.local_read) {
                    var percent = (100*data.local_read/data.local_total_read) - 0.02;
					var nice_percent = percent.toFixed(2);
                    $('.progress-bar').css('width', nice_percent + '%');
                    $('.progress-bar').html(nice_percent + '%');
				}
            }
        })

        return YagoLoadView;
    });
