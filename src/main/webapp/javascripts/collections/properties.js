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
                        alert('Unable to fetch properties!');
                    });
            },
        });
        return Properties;
    });

