define(
    ["backbone",
        "jquery",
		"ResponsePlace",
		"text!templates/response-place.html",
        "bootstrap",
    ], function (Backbone, $, ResponsePlace, ResponsePlaceTemplate) {
        var ResponsePlaceView = Backbone.View.extend({
            el: "#response-place",
            render: function () {
				var template = _.template(ResponsePlaceTemplate);
                this.$el.html(template());
				this.renderName();
				this.populateCategories();
				this.populateProperties();
				this.populateNewProperties();
            },
			renderName: function () {
				$('#place-name').html(this.model.attributes.name);
			},
			populateCategories: function () {
				var catsArr = this.model.attributes.categories;
				_.each(catsArr, function (cat) {
					var catModel = MyGlobal.collections.categories.get(cat.yagoId);
					$('#response-place-categories').append("<option>" + catModel.get('name') +  "</option>");
				});
			},
			populateProperties: function () {
				var propsArr = this.model.attributes.properties;
				_.each(propsArr, function (p) {
					$('#response-place-properties').append("<option>" + p.name +  "</option>");
				});
			},
			populateNewProperties: function () {
				var propsArr = this.model.attributes.properties;
				var diffPropsArr = MyGlobal.collections.properties.reject(function (p) {
					return _.contains(propsArr, p);	
				});
				console.log(diffPropsArr);
				_.each(diffPropsArr, function (p) {
					$('#response-place-new-property').append("<option value='" + p.id + "'>" + p.get('name') +  "</option>");
				});
			},
   
   
        });

        return ResponsePlaceView;
    });
