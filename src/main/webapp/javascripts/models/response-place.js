define(
    ["backbone",
        "jquery",
		"ol",
        "bootstrap",
    ], function (Backbone, $, ol) {
	   var ResponsePlace = Backbone.Model.extend({
		   idAttribute: "yagoId",
			toOLFeature: function () {
		   		var lat = this.attributes.loc.lat;
	  			var lon = this.attributes.loc.lon;
				var point = new ol.geom.Point(ol.proj.transform([lon, lat], 'EPSG:4326', 'EPSG:3857'));

				var feat = new ol.Feature({
					pointGeom: point,
					model: this,
				});
				feat.setGeometryName('pointGeom');
				return feat;
			},
		});
	   return ResponsePlace;
    });
