define(
    ["backbone",
        "jquery",
		"Category",
        "bootstrap",
    ], function (Backbone, $, Category) {
	   var Categories = Backbone.Collection.extend({
		   model: Category,
	   	   initialize: function() {
			   console.log("initializing categories");
			   this.fetchFromServer();
		   },
	   	   fetchFromServer: function() {
				$.ajax({
					url:'get_all_categories'
					}).done(function(data){
						this.reset(data);
					}.bind(this))
					.fail(function(){
						alert('Unable to fetch categories!');
				});
		   },
		});
	   return Categories;
    });
