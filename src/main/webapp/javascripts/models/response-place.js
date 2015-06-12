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
//				console.log(this);

//				console.log(lat);
//				console.log(ol.proj.transform([lat, lon], 'EPSG:4326', 'EPSG:3857'));
			
				var point = new ol.geom.Point(ol.proj.transform([lat, lon], 'EPSG:4326', 'EPSG:3857'));
//				console.log(point.getCoordinates());
				return new ol.Feature(point);
			},
		});
	   return ResponsePlace;
    });
