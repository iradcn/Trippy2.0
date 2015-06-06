define(
    ["backbone",
        "jquery",
        "text!templates/areas.html",
        "bootstrap",
    ], function (Backbone, $, Areas) {
        var AreasView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .submit': 'onSubmit',
            },
            initialize: function () {
                this.render();
            },
            render: function () {
                var template = _.template(Areas);
                this.$el.html(template());
				this.getCategories();
            },
			getCategories: function () {
                $.ajax({
                    url:'get_all_categories'
                    }).done(function(data){
						MyGlobal.categories = data;
						this.appendCollectionNameToSelect(data, '#categories');
                    }.bind(this))
                    .fail(function(){
                        alert('Unable to fetch categories!');
                    });
			},
			getProperties: function () {
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
				/*
            onSubmit: function () {
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
				*/
        })

        return AreasView;
    });
