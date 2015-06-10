define(
    ["backbone",
        "jquery",
        "ol",
        "text!templates/places.html",
        "bootstrap",
    ], function (Backbone, $, ol, PlacesTemplate) {
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
                this.initMap();
				this.catView.render();
				this.initProperties();
            },
            initMap: function () {
                var raster = new ol.layer.Tile({
                    source: new ol.source.MapQuest({layer: 'osm'})
                });

                var map = new ol.Map({
                    layers: [raster],
                    target: 'map',
                    view: new ol.View({
                        center: ol.proj.transform([31, 37], 'EPSG:4326', 'EPSG:3857'),
                        zoom: 4,
                    })
                });

                var featureOverlay = new ol.FeatureOverlay({
                    style: new ol.style.Style({
                        fill: new ol.style.Fill({
                            color: 'rgba(255, 255, 255, 0.2)'
                        }),
                        stroke: new ol.style.Stroke({
                            color: '#ffcc33',
                            width: 2
                        }),
                        image: new ol.style.Circle({
                            radius: 7,
                            fill: new ol.style.Fill({
                                color: '#ffcc33'
                            })
                        })
                    })
                });
                featureOverlay.setMap(map);

                draw = new ol.interaction.Draw({
                    features: featureOverlay.getFeatures(),
                    type: "Circle"
                });
                map.addInteraction(draw);

                MyGlobal.circle_locations = featureOverlay;
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
				$.ajax({
                    method: "POST",
                    url: 'get_places_by_loc',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(req_json)
                }).done(function(data) {
						console.log(data);
                    }.bind(this))
                    .fail(function(){
                        alert('Unable to fetch places!');
                    });

            },
			constructRequest: function () {
                var location_circle = MyGlobal.circle_locations.getFeatures().getArray()[0];
                var location_coordinates = ol.proj.transform(location_circle.getGeometry().getCenter(), 'EPSG:3857', 'EPSG:4326');

                var cat_yago_id = $('#categories').val();
				return {
					"loc": {
                        "lat": location_coordinates[0],
                        "lon": location_coordinates[1],
						"radius": location_circle.getGeometry().getRadius(),
					},
					"categories": [
                        MyGlobal.collections.categories.get(cat_yago_id).attributes
                    ],
					"properties": [],
				};
			}
        })

        return PlacesView;
    });
