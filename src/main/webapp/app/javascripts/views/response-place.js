define([
    "backbone",
    "jquery",
    "ResponsePlace",
    "text!templates/response-place.html",
    "bootstrap"
], function (Backbone, $, ResponsePlace, ResponsePlaceTemplate) {
  var ResponsePlaceView = Backbone.View.extend({
    el: "#response-place",
    events: {
      'click .places-new-property-submit': 'newPropertySubmit',
      'click .places-remove-property-submit': 'deletePropertySubmit',
      'change #response-place-properties': 'toggleDeleteOption',
      'click .alert-resize-map': 'resizeMap'
    },
    render: function () {
      var template = _.template(ResponsePlaceTemplate);
      this.$el.html(template());
      this.renderName();
      this.populateCategories();
      this.populateProperties();
      this.renderImage();
      this.populateNewProperties();
      $('.response-place-modal').modal();
    },
    renderName: function () {
      $('#place-name').html(this.model.attributes.name);
    },
    populateCategories: function () {
      var catsArr = this.model.attributes.categories;
      _.each(catsArr, function (cat) {
        var catModel = MyGlobal.collections.categories.get(cat.id);
        $('#response-place-categories').append("<option>" + catModel.get('representationName') +  "</option>");
      });
    },
    populateProperties: function () {
      var propsArr = this.model.attributes.properties;
      _.each(propsArr, function (p) {
        var propModel = MyGlobal.collections.properties.get(p.id);
        $('#response-place-properties').append("<option>" + propModel.get('name') +  "</option>");
      });
    },
    renderImage: function () {
      var placeId = this.model.attributes.googleId;
      $.ajax({
        method: "GET",
        url: "/app/image/" + placeId + ".jpg"
      }).done(function() {
        $(this.el).find('#selectPlaceImage').attr("src", "/app/image/" + placeId + ".jpg");
        $(this.el).find('#selectPlaceImage').height("auto");
      }.bind(this)).fail(function() {
        $(this.el).find('#selectPlaceImage').height(0);
      }.bind(this));

    },
    populateNewProperties: function () {
      var propsArr = this.model.attributes.properties;
      var diffPropsArr = MyGlobal.collections.properties.reject(function (p) {
        return _.contains(_.map(propsArr, function(prop) {
          return prop.id;
        }), p.id);  
      });
      _.each(diffPropsArr, function (p) {
        $('#response-place-new-property').append("<option value='" + p.id + "'>" + p.get('name') +  "</option>");
      });
      if ($('#response-place-new-property:not(:has(option))').length != 0) {
        $('#response-place-new-property').prop('disabled', true);
        $('.places-new-property-submit').prop('disabled', true);
      }
    },
    newPropertySubmit: function () {
      $('.alert-resize-map').click();

      var propId = $('#response-place-new-property').val();
      var placeId = this.model.get('googleId');
      var placenId = this.model.get('nId');

      $.ajax({
        method: "GET",
        url: 'addVoteToProp',
        data: {'propId': propId,
               'placeId': placeId,
                'voteValue': 1,
                'placenId':placenId}
      }).done(function() {
        this.model.attributes.properties.push({'id': parseInt(propId)});
        this.render();
      }.bind(this)).fail(function(){
        $('.alerts-row').html(
          '<div class="alert alert-danger alert-dismissable" role="alert">' +
          '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          '<strong>Unable to add property to place!</strong> See console log for more details.</div>');
      });
    },
    resizeMap: function () {
    },
    deletePropertySubmit: function () {
      $('.alert-resize-map').click();

      var propId = $('#response-place-properties').val();
      var placeId = this.model.get('googleId');

      $.ajax({
        method: "GET",
        url: 'addVoteToProp',
        data: {'propId': propId,
          'placeId': placeId,
          'voteValue': -1}
      }).done(function() {
        this.model.attributes.properties = _.reject(this.model.attributes.properties, function (p) {
          return p.id == parseInt(propId);  
        }, this);
        this.render();
      }.bind(this)).fail(function(){
        $('.alerts-row').html(
          '<div class="alert alert-danger alert-dismissable" role="alert">' +
          '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
          '<span aria-hidden="true">&times;</span></button>' +
          '<strong>Unable to remove property to place!</strong> See console log for more details.</div>');
      });
    },
    toggleDeleteOption: function () {
      $('.places-remove-property-submit').prop('disabled', false);
    },
  });

  return ResponsePlaceView;
});
