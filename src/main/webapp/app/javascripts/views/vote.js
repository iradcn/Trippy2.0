define([
  "backbone",
  "jquery",
  "text!templates/vote.html",
  "bootstrap",
  "jqueryui",
  "Vote"
], function (Backbone, $, VoteTemplate, Vote) {
  var VoteView = Backbone.View.extend({
    el: ".dialog-container",
    events: {
      'click .vote-yes': 'respondYes',
      'click .vote-no': 'respondNo',
      'click #btnYes1': 'respondYes',
      'click .submit' : 'submit'
    },
    initialize: function () {
      $('.vote-yes').click(function(){
        console.log('im in');
      });
    },
    render: function () {
      var template = _.template(VoteTemplate);
      this.$el.html(template());

      // using dialogs instead of modals
/* 
      $('#dialog').dialog({
        modal: true,
        dialogClass: 'dlg-no-title',
        resizable: false,
        buttons: {
          "yes": {
            text: "Yes",
            class: 'btn btn-success',
            click: function() {
              this.respondYes();
              $(this).dialog("close");
            },
          },
          "no": {
            text: "No",
            class: 'btn btn-danger',
            click: function() {
              $(this).dialog("close");
            },
          },
          "dontknow": {
            text: "I have no idea!",
            class: 'btn',
            click: function() {
              $(this).dialog("close");
            },
          },

        }
      });
      */
      $('.modal').modal('show');
      
      //this.requestVote();

      $("#placeImage").attr("src", "data:image/jpeg;base64," + MyGlobal.models.vote.get("placeImage"));
    },
    requestVote: function() {
      $.ajax({
        method: "GET",
        url: 'vote/request',
      }).done(function(res) {
        console.log(res);
        this.vote = res;
        $('#vote-place').html(res.place);
        $('#vote-prop').html(res.property);
      }.bind(this)).fail(function(){
        console.log("Failed fetching a vote from server!");
      });
    },
    respond: function(ans) {
      $.ajax({
        method: "POST",
        url: 'vote/response',
        data: {
          placeId: this.vote.placeId,
          propId: this.vote.propertyId,
          answer: ans,
        }
      }).done(function() {
        console.log("answer sent!");
      }.bind(this)).fail(function(){
        console.log("answer sending failed!");
      });
    },
    respondYes: function(data) {
      repond("yes");
      $(this).dialog("close");
    },
    respondNo: function() {
      repond("no");
    },
    submit: function(data) {
      $('.modal').modal('hide');
    }
  });

  return VoteView;
});
