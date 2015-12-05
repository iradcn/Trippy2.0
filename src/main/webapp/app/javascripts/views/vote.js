define([
  "backbone",
  "jquery",
  "text!templates/vote.html",
  "bootstrap",
  "jqueryui",
  "Vote"
], function (Backbone, $, VoteTemplate, Vote) {
  var VoteView = Backbone.View.extend({
    events: {
    },
    initialize: function () {
      $('.vote-yes').click(function(){
        console.log('im in');
      });
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
    }
  });

  return VoteView;
});
