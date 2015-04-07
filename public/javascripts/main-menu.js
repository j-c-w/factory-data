/**
 * Created by Jackson on 4/6/2015.
 */


/*
 * This sets up the scrolling of the graphs on the main page.
 *
 * An explanation of the settings can be seen here:
 *      http://slippry.com/settings/
 */
$(document).ready(function(){
    $(".slippry-list").slippry({
        pause: 4000,
        start: "random"
    });
});