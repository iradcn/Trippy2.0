define(
    ["backbone",
        "jquery",
		"Category",
        "bootstrap",
    ], function (Backbone, $, Category) {
	   var Categories = Backbone.Collection.extend({
		   model: Category,
	   	   initialize: function() {
			   this.fetchFromServer();
		   },
	   	   fetchFromServer: function() {
				$.ajax({
					url:'get_all_categories'
				}).done(function(data){
					this.reset(data);
				}.bind(this))
					.fail(function(){
						$('.map').css('height', $('.map').height() - 60);
						$('.alerts-row').html(
							'<div class="alert alert-danger alert-dismissable" role="alert">' +
							'<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
							'<span aria-hidden="true">&times;</span></button>' +
							'<strong>Unable to fetch categories!</strong> See console log for more details.</div>');
					});
		   },
		});
	   return Categories;
    });
