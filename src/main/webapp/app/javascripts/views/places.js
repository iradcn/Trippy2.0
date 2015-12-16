define([
  "backbone",
  "jquery",
  "ol",
  "text!templates/places.html",
  "text!templates/vote.html",
  "ResponsePlaceView",
  "bootstrap",
], function (Backbone, $, ol, PlacesTemplate, VoteTemplate, ResponsePlaceView) {
  var PlacesView = Backbone.View.extend({
    el: ".body-container",
    vote_el: ".dialog-container",
    events: {
      'click .places-submit': 'placesSubmit',
      'click .places-reset-submit': 'resetSubmit',
      'change #places-select-curr-categories': 'toggleApplyFilterOption',
      'change #places-select-curr-properties': 'toggleApplyFilterOption',
      'click #btnYes1': 'setYes1',
      'click #btnYes2': 'setYes2',
      'click #btnYes3': 'setYes3',
      'click #btnNo1': 'setNo1',
      'click #btnNo2': 'setNo2',
      'click #btnNo3': 'setNo3',
      'click #btnIDK1': 'setIDK1',
      'click #btnIDK2': 'setIDK2',
      'click #btnIDK3': 'setIDK3',
      'click #submitVote': 'submitVote'

 //     'click .alert-resize-map': 'resizeMap'
    },
    initialize: function () {
      this.catView = MyGlobal.views.select_categories_view;
      this.propView = MyGlobal.views.select_properties_view;
      this.voteAnswers = ['', '', ''];
    },
    render: function () {
      var places_template = _.template(PlacesTemplate);
      this.$el.html(places_template());

      var vote_template = _.template(VoteTemplate);
      $(this.vote_el).html(vote_template());

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
        target: 'places-map',
        view: new ol.View({
          center: ol.proj.transform([34.781813, 32.075978], 'EPSG:4326', 'EPSG:3857'),
          zoom: 14,
          minZoom: 14,
          maxZoom: 14
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
    renderVoteModal: function (data) {
      $.ajax({
        method: "GET",
        url: "/app/image/" + data.placeId + ".jpg",
      }).done(function() {
        $(this.vote_el).find('#placeImage').attr("src", "/app/image/" + data.placeId + ".jpg");
        $(this.vote_el).find('#placeImage').show();
      }.bind(this)).fail(function() {
        $(this.vote_el).find('#placeImage').hide();
      }.bind(this));
      $(this.vote_el).find('#placeName').text(data.name);
      $(this.vote_el).find('#row1PropLabel').text(data.property[0].name);
      $(this.vote_el).find('#row2PropLabel').text(data.property[1].name);
      $(this.vote_el).find('#row3PropLabel').text(data.property[2].name);
      $('.vote-modal').modal('show');
    },

    handlePlacesResponse: function (data) {
      if (data.places.length != 0) {
        MyGlobal.collections.ResponsePlaces.reset(data.places);
        this.overlayResponse();
      } else {
        $('.alerts-row').html(
            '<div class="alert alert-warning alert-dismissable" role="alert">' +
            '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
            '<span aria-hidden="true">&times;</span></button>' +
            '<strong>No places were found!</strong> No places matching chosen filter criterias were found.</div>');
      }
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
        if (data.places) {
          this.handlePlacesResponse(data);
        } else if (data.question) {
          this.inFlightQuestion = data.question;
          this.renderVoteModal(data.question);
        } else {
          $('.alerts-row').html(
            '<div class="alert alert-danger alert-dismissable" role="alert">' +
            '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
            '<span aria-hidden="true">&times;</span></button>' +
            '<strong>Something went terribly wrong!</strong> See console log for more details.</div>');
        }
      }.bind(this)).fail(function() {
        $('.alerts-row').html(
          '<div class="alert alert-danger alert-dismissable" role="alert">' +
          '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          '<strong>Unable to fetch places!</strong> See console log for more details.</div>');
      });
    },
    resizeMap: function () {
      //$('#places-map').css('height', $('#places-map').height() + 60);
    },
    resetSubmit: function () {
      if (this.circlesVectorSource.getFeatures().length === 0) {
        $('.places-submit').prop('disabled', true);
      }

      $('#places-select-curr-categories').val('');
      $('#places-select-curr-properties').val('');
      $('.places-reset-submit').prop('disabled', true);
	   this.pointsVectorSource.clear();
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
        return _.contains(cat_yago_ids, c.id.toString());
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
          "radius": rad_mile
        },
        "categories": filtered_cats.map(function (c) {
          return c.attributes; 
        }),
        "properties": filtered_props.map(function (p) {
          return p.attributes; 
        })
      };
    },
    overlayResponse: function () {
      this.pointsVectorSource.clear();

      var pointsArray = MyGlobal.collections.ResponsePlaces.map(function(respPlace) {return respPlace.toOLFeature();});

      this.pointsVectorSource.addFeatures(pointsArray);
    },
    fixButtons: function(qnum) {
      inner_qnum = qnum - 1;
      if (this.voteAnswers[inner_qnum] == 1) {
        $('#btnYes' + qnum).removeClass('btn-success');
        $('#btnYes' + qnum).addClass('btn-info');
        $('#btnNo' + qnum).addClass('btn-danger');
        $('#btnNo' + qnum).removeClass('btn-info');
        $('#btnIDK' + qnum).addClass('btn-warning');
        $('#btnIDK' + qnum).removeClass('btn-info');
      } else if (this.voteAnswers[inner_qnum] == -1) {
        $('#btnYes' + qnum).addClass('btn-success');
        $('#btnYes' + qnum).removeClass('btn-info');
        $('#btnNo' + qnum).removeClass('btn-danger');
        $('#btnNo' + qnum).addClass('btn-info');
        $('#btnIDK' + qnum).addClass('btn-warning');
        $('#btnIDK' + qnum).removeClass('btn-info');
      } else if (this.voteAnswers[inner_qnum] == 0) {
        $('#btnYes' + qnum).addClass('btn-success');
        $('#btnYes' + qnum).removeClass('btn-info');
        $('#btnNo' + qnum).addClass('btn-danger');
        $('#btnNo' + qnum).removeClass('btn-info');
        $('#btnIDK' + qnum).removeClass('btn-warning');
        $('#btnIDK' + qnum).addClass('btn-info');
      } else if (this.voteAnswers[inner_qnum] == '') {
        $('#btnYes' + qnum).addClass('btn-success');
        $('#btnYes' + qnum).removeClass('btn-info');
        $('#btnNo' + qnum).addClass('btn-danger');
        $('#btnNo' + qnum).removeClass('btn-info');
        $('#btnIDK' + qnum).addClass('btn-warning');
        $('#btnIDK' + qnum).removeClass('btn-info');
      } else {
        alert("someone fucked up here");
      }
    },
    setYes1: function() {
      this.voteAnswers[0] = 1;
      this.fixButtons(1);
    },
    setYes2: function() {
      this.voteAnswers[1] = 1;
      this.fixButtons(2);
    },
    setYes3: function() {
      this.voteAnswers[2] = 1;
      this.fixButtons(3);
    },
    setNo1: function() {
      this.voteAnswers[0] = -1;
      this.fixButtons(1);
    },
    setNo2: function() {
      this.voteAnswers[1] = -1;
      this.fixButtons(2);
    },
    setNo3: function() {
      this.voteAnswers[2] = -1;
      this.fixButtons(3);
    },
    setIDK1: function() {
      this.voteAnswers[0] = 0;
      this.fixButtons(1);
    },
    setIDK2: function() {
      this.voteAnswers[1] = 0;
      this.fixButtons(2);
    },
    setIDK3: function() {
      this.voteAnswers[2] = 0;
      this.fixButtons(3);
    },
    submitVote: function() {
      if (this.voteAnswers[0] === '' ||
          this.voteAnswers[1] === '' ||
          this.voteAnswers[2] === '') {
        alert("wtf man");
      } else {
        var res_json = this.inFlightQuestion;
        res_json["answers"] = this.voteAnswers;
        res_json["query"] = this.constructRequest();

        console.log(JSON.stringify(res_json));
        $.ajax({
          method: "POST",
          url: 'vote/answer',
          dataType: 'json',
          contentType: 'application/json',
          data: JSON.stringify(res_json)
        }).done(function(data) {
          this.handlePlacesResponse(data)
        }.bind(this)).fail(function(){
          console.log("answer sending failed!");
        });

        this.voteAnswers = ['', '', ''];
        $('.vote-modal').modal('hide');
      }
    },
  });
  return PlacesView;
});
