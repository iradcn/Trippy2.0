define(
    ["backbone",
        "jquery",
        "ol",
        "text!templates/places.html",
		"ResponsePlaceView",
        "bootstrap",
    ], function (Backbone, $, ol, PlacesTemplate, ResponsePlaceView) {
        var PlacesView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .places-submit': 'placesSubmit',
            },
            initialize: function () {
				this.catView = MyGlobal.views.select_categories_view;
				this.propView = MyGlobal.views.select_properties_view;
                this.render();
            },
            render: function () {
                var template = _.template(PlacesTemplate);
                this.$el.html(template());
                this.initMap();
				this.catView.render();
				this.propView.render();
            },
            initMap: function () {
				// The actual map layer
                var raster = new ol.layer.Tile({
                    source: new ol.source.MapQuest({layer: 'osm'})
                });

				// Drawing circles layer
				var circlesVectorStyle = new ol.style.Style({
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
                    });
				var circlesVectorSource = new ol.source.Vector();
				this.circlesVectorSource = circlesVectorSource;
				var circlesVectorLayer = new ol.layer.Vector({
					source: circlesVectorSource,
					style: circlesVectorStyle,
				});



				pointsVectorStyle = new ol.style.Style({
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
                    });

				var pointsVectorSource = new ol.source.Vector();
				this.pointsVectorSource = pointsVectorSource;
				var pointsVectorLayer = new ol.layer.Vector({
					source: pointsVectorSource,
					style: pointsVectorStyle,
				});



                var map = new ol.Map({
                    layers: [raster, circlesVectorLayer, pointsVectorLayer],
                    target: 'map',
                    view: new ol.View({
                        center: ol.proj.transform([31, 37], 'EPSG:4326', 'EPSG:3857'),
                        zoom: 4,
                    })
                });

				this.map = map;

                draw = new ol.interaction.Draw({
                    source: circlesVectorSource,
                    type: "Circle"
                });
				draw.on('drawstart', function() { // make sure only 1 circle
					circlesCollection = circlesVectorSource.getFeatures();
					if (circlesCollection.length > 0) {
						circlesVectorSource.removeFeature(circlesCollection[0]);
					}  
				});
                map.addInteraction(draw);


				map.on('click', function(evt) {
				  var feature = map.forEachFeatureAtPixel(evt.pixel,
					  function(feature, layer) {
						  if (layer) { 
							return feature;
						  }
					  });
				  if (feature) {
					this.map.removeInteraction(draw);

					var geometry = feature.getGeometry();
					var coord = geometry.getCoordinates();
					placeView = new ResponsePlaceView({
						model: feature.get('model')
					});
					placeView.render();

					this.map.addInteraction(draw);
				  } 
				}, this);   
				


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
                var location_circle = this.circlesVectorSource.getFeatures()[0];
                var location_coordinates = ol.proj.transform(location_circle.getGeometry().getCenter(), 'EPSG:3857', 'EPSG:4326');

                var cat_yago_ids = $('#categories').val(); // array of yagoId
				var filtered_cats = MyGlobal.collections.categories.filter(function(c) {
					return _.contains(cat_yago_ids, c.id);
				});

                var prop_yago_ids = $('#select-curr-properties').val(); // array of yagoId
				var filtered_props = MyGlobal.collections.properties.filter(function(p) {
					return _.contains(prop_yago_ids, p.id + '');
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
					"properties": filtered_props.map(function (p) {
						return p.attributes; 
					}),
				};
			},
			overlayResponse: function () {
				this.circlesVectorSource.clear();

				var pointsArray = MyGlobal.collections.ResponsePlaces.map(function(respPlace) {return respPlace.toOLFeature();});

				this.pointsVectorSource.addFeatures(pointsArray);
			},
        });
        return PlacesView;
    });
