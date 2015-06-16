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
                'click .places-reset-submit': 'resetSubmit',
                'change #places-select-curr-categories': 'toggleApplyFilterOption',
                'change #places-select-curr-properties': 'toggleApplyFilterOption',
                'click .alert-resize-map': 'resizeMap'
            },
            initialize: function () {
				this.catView = MyGlobal.views.select_categories_view;
				this.propView = MyGlobal.views.select_properties_view;
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
                    });

				var circlesVectorSource = new ol.source.Vector();
				this.circlesVectorSource = circlesVectorSource;
				var circlesVectorLayer = new ol.layer.Vector({
					source: circlesVectorSource,
					style: circlesVectorStyle,
				});

				var pointsVectorStyle = new ol.style.Style({
                        image: new ol.style.Circle({
                            radius: 6,
                            fill: new ol.style.Fill({
                                color: 'rgba(0, 0, 255, 0.3)'
                            }),
							stroke: new ol.style.Stroke({
								color: 'blue',
								width: 1
							}),

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
                    target: 'places-map',
                    view: new ol.View({
                        center: ol.proj.transform([37, 31], 'EPSG:4326', 'EPSG:3857'),
                        zoom: 4,
                    })
                });

				this.map = map;

                var draw = new ol.interaction.Draw({
                    source: circlesVectorSource,
                    type: "Circle",
                    style: circlesVectorStyle
                });
				draw.on('drawstart', function() { // make sure only 1 circle
					var circlesCollection = circlesVectorSource.getFeatures();
					if (circlesCollection.length > 0) {
						circlesVectorSource.removeFeature(circlesCollection[0]);
					}
				});
                draw.on('drawend', function(e) {
                    this.toggleApplyFilterOption(e);
                }, this);
                map.addInteraction(draw);

				var selectedFeatureStyle = new ol.style.Style({
                        image: new ol.style.Circle({
                            radius: 6,
                            fill: new ol.style.Fill({
                                color: '#ffcc33'
                            }),
                            stroke: new ol.style.Stroke({
                                color: 'rgba(0, 0, 0, 0.8)',
                                width: 2
                            }),
                        })
                    });

				// hover over fetched places
				var pointerMove = new ol.interaction.Select({
					condition: ol.events.condition.pointerMove,
					style: selectedFeatureStyle,
					layers: [pointsVectorLayer],
				});
				pointerMove.on('select', function(e) {
					if (e.selected.length > 0 && e.selected[0].getGeometryName() == 'pointGeom' && circlesVectorSource.getFeatures().length > 0) {
						$('#places-map').css('cursor', 'hand');
						draw.setActive(false);
					} else {
						$('#places-map').css('cursor', 'default');
						draw.setActive(true);
					}
				});
				map.addInteraction(pointerMove);

				// click on fetched places
				var pointClick = new ol.interaction.Select({
					condition: ol.events.condition.click,
					style: selectedFeatureStyle,
					layers: [pointsVectorLayer],
				});
				pointClick.on('select', function(e) {
					if (e.selected.length > 0 && e.selected[0].getGeometryName() == 'pointGeom') {
					placeView = new ResponsePlaceView({
						model: e.selected[0].get('model')
					});
					placeView.render();

					} 
				});
				map.addInteraction(pointClick);
            },
            placesSubmit: function () {
                $('.alert-resize-map').click();
				var req_json = this.constructRequest();
				$.ajax({
                    method: "POST",
                    url: 'get_places_by_loc',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(req_json)
                }).done(function(data) {
                    if (data.length != 0) {
                        MyGlobal.collections.ResponsePlaces.reset(data);
                        this.overlayResponse();
                    } else {
                        $('#places-map').css('height', $('#places-map').height() - 60);
                        $('.alerts-row').html(
                            '<div class="alert alert-warning alert-dismissable" role="alert">' +
                            '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
                            '<span aria-hidden="true">&times;</span></button>' +
                            '<strong>No places were found!</strong> No places matching chosen filter criterias were found.</div>');
                    }
                }.bind(this))
                .fail(function() {
                    $('#places-map').css('height', $('#places-map').height() - 60);
                    $('.alerts-row').html(
                        '<div class="alert alert-danger alert-dismissable" role="alert">' +
                        '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
                        '<span aria-hidden="true">&times;</span></button>' +
                        '<strong>Unable to fetch places!</strong> See console log for more details.</div>');
                });

            },
            resizeMap: function () {
                $('#places-map').css('height', $('#places-map').height() + 60);
            },
            resetSubmit: function () {
                if (this.circlesVectorSource.getFeatures().length === 0) {
                    $('.places-submit').prop('disabled', true);
                }

                $('#places-select-curr-categories').val('');
                $('#places-select-curr-properties').val('');
                $('.places-reset-submit').prop('disabled', true);
            },
            toggleApplyFilterOption: function (e) {
                if ((this.circlesVectorSource.getFeatures().length != 0) || e.feature) {
                    $('.places-submit').prop('disabled', false);
                }

                if ($('#places-select-curr-categories').val() || $('#places-select-curr-properties').val()) {
                    $('.places-reset-submit').prop('disabled', false);
                }
            },
			constructRequest: function () {
                var location_circle = this.circlesVectorSource.getFeatures()[0];
                var location_coordinates = ol.proj.transform(location_circle.getGeometry().getCenter(), 'EPSG:3857', 'EPSG:4326');

                var cat_yago_ids = $('#places-select-curr-categories').val(); // array of yagoId
				var filtered_cats = MyGlobal.collections.categories.filter(function(c) {
					return _.contains(cat_yago_ids, c.id);
				});

                var prop_yago_ids = $('#places-select-curr-properties').val(); // array of yagoId
				var filtered_props = MyGlobal.collections.properties.filter(function(p) {
					return _.contains(prop_yago_ids, p.id + '');
				});
				
				var rad_km = location_circle.getGeometry().getRadius() / 1000;
				var rad_mile = rad_km * 0.621;
				return {
					"loc": {
                        "lat": location_coordinates[1],
                        "lon": location_coordinates[0],
						"radius": rad_mile,
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
				this.pointsVectorSource.clear();

				var pointsArray = MyGlobal.collections.ResponsePlaces.map(function(respPlace) {return respPlace.toOLFeature();});

				this.pointsVectorSource.addFeatures(pointsArray);
			},
        });
        return PlacesView;
    });
