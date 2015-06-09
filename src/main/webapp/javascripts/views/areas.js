define(
    ["backbone",
        "jquery",
		"ol",
        "text!templates/areas.html",
        "bootstrap",
    ], function (Backbone, $, ol, Areas) {
        var AreasView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .areas-submit': 'areasSubmit',
            },
            initialize: function () {
				this.catView = MyGlobal.views.select_categories_view;
                this.render();
            },
            render: function () {
                var template = _.template(Areas);
                this.$el.html(template());
				this.initMap();
				this.initProperties();
				this.catView.render();
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
            areasSubmit: function() {
                var locationsArr = MyGlobal.circle_locations.getFeatures().getArray();
                for (i = 0; i < MyGlobal.circle_locations.getFeatures().getLength(); i++) {
                    console.log(ol.proj.transform(locationsArr[i].getGeometry().getCenter(), 'EPSG:3857', 'EPSG:4326'));
                    console.log(locationsArr[i].getGeometry().getRadius());
                }
            },
			
        })

        return AreasView;
    });
