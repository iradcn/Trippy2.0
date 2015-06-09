define(
    ["backbone",
        "jquery",
        "text!templates/places.html",
        "bootstrap",
    ], function (Backbone, $, PlacesTemplate) {
        var PlacesView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .places-submit': 'placesSubmit',
            },
            initialize: function () {
				this.catView = MyGlobal.views.select_categories_view;
                this.render();
            },
            render: function () {
                var template = _.template(PlacesTemplate);
                this.$el.html(template());
				this.catView.render();
				this.initProperties();
            },
			initProperties: function () {
                $.ajax({
                    url:'get_all_properties'
                    }).done(function(data){
						MyGlobal.properties = data;
						this.appendCollectionNameToSelect(data, '#properties');
                    }.bind(this))
                    .fail(function(){
                        alert('Unable to fetch properties!');
                    });
			},
			appendCollectionNameToSelect: function (collection, select) {
				_.each(collection, function (elem) {
							$(select).append(
								"<option value='" + elem.yagoId + "'>" + elem.name +  "</option>"
							);
						
						});
			},
            placesSubmit: function () {
				var req_json = this.constructRequest();
				console.log(req_json);
				$.post({
                    url: 'get_all_properties',
					data: req_json,
                    }).done(function(data) {
						console.log(data);
                    }.bind(this))
                    .fail(function(){
                        alert('Unable to fetch places!');
                    });

            },
			constructRequest: function () {
				return {
					"loc": {
						"lat": $('#lat').val(),
						"lon": $('#lon').val(),
						"radius": $('#radius').val(),
					},
					"categories": $('#categories').val(),
					"properties": [],
				};
			}
        })

        return PlacesView;
    });