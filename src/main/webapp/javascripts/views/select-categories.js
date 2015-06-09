define(
    ["backbone",
        "jquery",
		"Categories",
        "bootstrap",
    ], function (Backbone, $, Categories) {
        var SelectCategoriesView = Backbone.View.extend({
            el: "#categories",
            initialize: function () {
				this.collection = new Categories();
				this.collection.on("all", this.render, this);
            },
            render: function () {
				console.log("rendering cats");
                this.$el.html("");
				this.populateCategories();
            },
			populateCategories: function () {
				this.collection.each(function (cat) {
					$('#categories').append( "<option value='" + cat.id + "'>" + cat.get('name') +  "</option>");
				}, this);
			},
        });

        return SelectCategoriesView;
    });
