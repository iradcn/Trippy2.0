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

				this.map = map;

                var circlesOverlay = new ol.FeatureOverlay({
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
                circlesOverlay.setMap(map);

                draw = new ol.interaction.Draw({
                    features: circlesOverlay.getFeatures(),
                    type: "Circle"
                });
				draw.on('drawstart', function() { // make sure only 1 circle
					circlesCollection = circlesOverlay.getFeatures();
					if (circlesCollection.getLength() > 0) {
						circlesOverlay.removeFeature(circlesCollection.item(0));
					}
				});
                map.addInteraction(draw);

                this.circlesOverlay = circlesOverlay;


                var pointsOverlay = new ol.FeatureOverlay({
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
				this.pointsOverlay = pointsOverlay;

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
						MyGlobal.collections.ResponsePlaces.reset(data);
						this.overlayResponse();
                    }.bind(this))
                    .fail(function(){
                        alert('Unable to fetch places!');
                    });

            },
			constructRequest: function () {
                var location_circle = this.circlesOverlay.getFeatures().getArray()[0];
                var location_coordinates = ol.proj.transform(location_circle.getGeometry().getCenter(), 'EPSG:3857', 'EPSG:4326');

                var cat_yago_ids = $('#categories').val(); // array of yagoId
				var filtered_cats = MyGlobal.collections.categories.filter(function(c) {
					return _.contains(cat_yago_ids, c.id);
				});

				return {
					"loc": {
                        "lat": location_coordinates[0],
                        "lon": location_coordinates[1],
						"radius": location_circle.getGeometry().getRadius() / 1000,
					},
					"categories": filtered_cats.map(function (c) {
						return c.attributes; 
					}),
					"properties": [],
				};
			},
			overlayResponse: function () {
				var pointsArray = MyGlobal.collections.ResponsePlaces.map(function(respPlace) {return respPlace.toOLFeature();});


				this.pointsOverlay.setFeatures(new ol.Collection(pointsArray));
				this.pointsOverlay.setMap(this.map);

			},
        })

        return PlacesView;
    });
