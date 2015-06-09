/**
 * Created by Shiri on 06/06/2015.
 */
define(
    ["backbone",
        "jquery",
        "ol",
        "text!templates/map-display-template.html",
        "bootstrap"
    ], function (Backbone, $, ol, MapDisplayTemplate) {
        var MapDisplayView = Backbone.View.extend({
            el: ".body-container",
            events: {
                'click .test': 'printFeatures',
                'click .find': 'findLocation'
            },
            initialize: function () {
                this.render();

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
                    type: "Point"
                });
                map.addInteraction(draw);

                MyGlobal.point_locations = featureOverlay;
            },

            render: function() {
                var template = _.template(MapDisplayTemplate);
                this.$el.html(template());
            },

            printFeatures: function() {
                var locationsArr = MyGlobal.point_locations.getFeatures().getArray();
                for (i = 0; i < MyGlobal.point_locations.getFeatures().getLength(); i++) {
                    console.log(ol.proj.transform(locationsArr[i].getGeometry().getCoordinates(), 'EPSG:3857', 'EPSG:4326'));
                }
            },

            findLocation: function() {
                var location = new ol.Feature({
                    geometry: new ol.geom.Point(ol.proj.transform([parseInt($('#lat').val()), parseInt($('#lon').val())], 'EPSG:4326', 'EPSG:3857')),
                });
                MyGlobal.point_locations.addFeature(location);

                this.printFeatures();
            }
        })

        return MapDisplayView;
    });