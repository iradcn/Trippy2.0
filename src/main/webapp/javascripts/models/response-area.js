/**
 * Created by shiriladelsky on 6/13/15.
 */
define(
    ["backbone",
        "jquery",
        "ol",
        "bootstrap",
    ], function (Backbone, $, ol) {
        var ResponseArea = Backbone.Model.extend({
            idAttribute: "yagoId",
            toOLFeature: function () {

                console.log(this.attributes);

                var lat = this.attributes.loc.lat;
                var lon = this.attributes.loc.lon;
                var radius = this.attributes.loc.radius * 1000 / 0.621;
                var places_count = this.attributes.countPlaces;
                var circle = new ol.geom.Circle(ol.proj.transform([lat, lon], 'EPSG:4326', 'EPSG:3857'), radius);

                var feat = new ol.Feature({
                    geometry: circle,
                    model: this,
                });

                feat.setStyle(new ol.style.Style({
                    fill: new ol.style.Fill({
                        color: 'rgba(255, 255, 255, 0.2)'
                    }),
                    stroke: new ol.style.Stroke({
                        color: '#ffcc33',
                        width: 2
                    }),
                    text: new ol.style.Text({
                        textAlign: 'center',
                        textBaseline: 'aplhabetic',
                        font: 'normal 30px Arial',
                        text: places_count,
                        fill: new ol.style.Fill({color: 'green'}),
                        stroke: new ol.style.Stroke({color: 'black', width: 2}),
                        offsetX: 0,
                        offsetY: 16,
                        rotation: 0.0
                    })
                }));

                return feat;
            },
        });
        return ResponseArea;
    });

