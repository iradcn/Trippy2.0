/**
 * Created by Amir on 14/11/2015.
 */
    // Load the SDK asynchronously

(function(){

    var FB_API_VERSION = 'v2.2';
    var APP_ID = '1662478060658059';

    window.fbAsyncInit = function() {
        FB.init({
            appId      : APP_ID,
            cookie     : true,  // enable cookies to allow the server to access
            xfbml      : true,  // parse social plugins on this page
            version    : FB_API_VERSION // use version 2.2
        });
    };



}());


(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
