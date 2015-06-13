define(
    ["backbone",
        "jquery",
        "ol",
        "text!templates/areas.html",
        "bootstrap",
    ], function (Backbone, $, ol, AreasTemplate) {
        var AreasView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .areas-submit': 'areasSubmit',
                'click .areas-reset-submit': 'areasReset',
                'change #areas-select-curr-categories': 'toggleApplyFilterOption',
                'change #areas-select-curr-properties': 'toggleApplyFilterOption',
            },
            initialize: function () {
				this.catView = MyGlobal.views.select_categories_view;
				this.propView = MyGlobal.views.select_properties_view;
            },
            render: function () {
                var template = _.template(AreasTemplate);
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

                var map = new ol.Map({
                    layers: [raster, circlesVectorLayer],
                    target: 'map',
                    view: new ol.View({
                        center: ol.proj.transform([31, 37], 'EPSG:4326', 'EPSG:3857'),
                        zoom: 4,
                    })
                });

				this.map = map;

                var draw = new ol.interaction.Draw({
                    source: circlesVectorSource,
                    type: "Circle",
                    style: circlesVectorStyle
                });
				 draw.on('drawend', function(e) {
                    this.toggleApplyFilterOption(e);
                }, this);
                map.addInteraction(draw);

			 },
			 areasSubmit: function () {
				var req_json = this.constructRequest();
				$.ajax({
                    method: "POST",
                    url: 'get_places_aggregation',
                    dataType: 'json',
                    contentType: 'application/json',
                    data: JSON.stringify(req_json)
                }).done(function(data) {
                    MyGlobal.collections.ResponseAreas.reset(data);
                    this.overlayResponse();
                }.bind(this))
                .fail(function(){
                    alert('Unable to fetch place counts!');
                });
            },
            areasResetSubmit: function () {
                if (this.circlesVectorSource.getFeatures().length === 0) {
                    $('.areas-submit').prop('disabled', true);
                }

                $('#areas-select-curr-categories').val('');
                $('#areas-select-curr-properties').val('');
                $('.areas-reset-submit').prop('disabled', true);
            },
            toggleApplyFilterOption: function (e) {
                if ((this.circlesVectorSource.getFeatures().length != 0) || e.feature) {
                    $('.areas-submit').prop('disabled', false);
                }

                if ($('#areas-select-curr-categories').val() || $('#areas-select-curr-properties').val()) {
                    $('.areas-reset-submit').prop('disabled', false);
                }
            },
			constructRequest: function () {
				locs = _.map(this.circlesVectorSource.getFeatures(), function (f) {
					var coords = ol.proj.transform(f.getGeometry().getCenter(), 'EPSG:3857', 'EPSG:4326');
					var radius = f.getGeometry().getRadius() * 0.621 / 1000;
					return {
						'lat': coords[0],
						'lon': coords[1],
						'radius': radius,
					};
				});

                var cat_yago_id = $('#areas-select-curr-categories').val();
                var prop_id = $('#areas-select-curr-properties').val(); 

				return {
					"locs": locs,
					"category": cat_yago_id,
					"property": prop_id
				};
			},
			overlayResponse: function () {
				this.circlesVectorSource.clear();

				var circlesArray = MyGlobal.collections.ResponseAreas.map(function(respPlace) {return respPlace.toOLFeature();});

				this.circlesVectorSource.addFeatures(circlesArray);
			},
        });
        return AreasView;
	
    });
