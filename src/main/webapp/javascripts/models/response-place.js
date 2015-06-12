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
				var point = new ol.geom.Point(ol.proj.transform([lat, lon], 'EPSG:4326', 'EPSG:3857'));
				return new ol.Feature({
					geometry: point,
					model: this,
				});
			},
		});
	   return ResponsePlace;
    });
