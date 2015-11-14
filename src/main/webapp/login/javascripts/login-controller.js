/**
 * Created by Amir on 14/11/2015.
 */
if (typeof(MyGlobal) === 'undefined') {
    MyGlobal = {};
    MyGlobal.Controllers = {};
}

(function ($) {

    var REQUIRED_PERMISSIONS = 'email,public_profile,user_tagged_places';

    var LoginController = {

        statusChangeCallback: function statusChangeCallback(response) {
            console.log('statusChangeCallback');
            console.log(response);
            if (response.status === 'connected') {
                this.authenticateServer(response);
            } else {
                $('.error-msg').text("Please Login into Facebook.");
            }
        },

        authenticateServer: function authenticateServer(response) {

            var basedUrl = "/login/facebook";
            var accessTokenParam = "accessToken=" + response.authResponse.accessToken;
            var signedRequestParam = "signedRequest=" + response.authResponse.signedRequest;
            var userIdParam = "userID=" + response.authResponse.userID;
            var requestUrl = basedUrl + '?' + accessTokenParam + '&' + signedRequestParam + '&' + userIdParam;

            $.ajax({
                url: requestUrl
            }).fail(function (err) {
                console.log(err)
            }).done(function (res) {
                console.log(res);
                window.location.pathname = "/app/index.html";
            });
        },

        init: function () {
            var that = this;
            $('.fb-button').click(function () {
                FB.login(function (response) {
                    that.statusChangeCallback(response)
                }, {scope: REQUIRED_PERMISSIONS});
            });
        }


    };

    MyGlobal.Controllers.Login = LoginController;

})($);