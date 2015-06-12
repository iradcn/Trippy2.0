define(
    ["backbone",
        "jquery",
		"ResponsePlace",
		"text!templates/response-place.html",
        "bootstrap",
    ], function (Backbone, $, ResponsePlace, ResponsePlaceTemplate) {
        var ResponsePlaceView = Backbone.View.extend({
            el: "#response-place",
            events: {
                'click .places-new-property-submit': 'newPropertySubmit',
				'click .places-remove-property-submit': 'deletePropertySubmit',
            },
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
					var propModel = MyGlobal.collections.properties.get(p.id);
					$('#response-place-properties').append("<option value='" + propModel.id + "'>" + propModel.get('name') +  "</option>");
				});
			},
			populateNewProperties: function () {
				var propsArr = this.model.attributes.properties;
				var diffPropsArr = MyGlobal.collections.properties.reject(function (p) {
					return _.contains(_.map(propsArr, function(prop) {
						return prop.id;
					}), p.id);	
				});
				_.each(diffPropsArr, function (p) {
					$('#response-place-new-property').append("<option value='" + p.id + "'>" + p.get('name') +  "</option>");
				});
			},
		   newPropertySubmit: function () {
			   propId = $('#response-place-new-property').val();
			   placeId = this.model.id;
				$.ajax({
                    method: "GET",
                    url: 'AddPropToPlace',
                    data: {'propId': propId,
							'placeId': placeId}
                }).done(function(data) {
					this.model.attributes.properties.push({'id': parseInt(propId)});
					this.render();
                    }.bind(this))
                    .fail(function(){
                        alert('Unable to fetch places!');
                    });

            },
			deletePropertySubmit: function () {
				var propId = $('#response-place-properties').val();
				var placeId = this.model.id;

				$.ajax({
					method: "GET",
					url: 'DelPropFromPlace',
					data: {'propId': propId,
						'placeId': placeId}
				}).done(function(data) {
					console.log("success");
				}.bind(this)).fail(function(){
					alert('Unable to add property to place!');
				});
			}
   
        });

        return ResponsePlaceView;
    });
