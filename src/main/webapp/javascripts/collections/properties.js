/**
 * Created by shiriladelsky on 6/12/15.
 */
define(
    ["backbone",
        "jquery",
        "Property",
        "bootstrap",
    ], function (Backbone, $, Property) {
        var Properties = Backbone.Collection.extend({
            model: Property,
            initialize: function() {
                this.fetchFromServer();
            },
            fetchFromServer: function() {
                $.ajax({
                    url:'get_all_properties'
                }).done(function(data){
                    this.reset(data);
                }.bind(this))
                    .fail(function(){
                        $('.map').css('height', $('.map').height() - 60);
                        $('.alerts-row').html(
                            '<div class="alert alert-danger alert-dismissable" role="alert">' +
                            '<button type="button" class="close alert-resize-map" data-dismiss="alert" aria-label="Close">' +
                            '<span aria-hidden="true">&times;</span></button>' +
                            '<strong>Unable to fetch properties!</strong> See console log for more details.</div>');
                    });
            },
        });
        return Properties;
    });

