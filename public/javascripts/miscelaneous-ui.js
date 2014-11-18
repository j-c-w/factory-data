/**
 * Created by Jackson on 11/18/2014.
 */
$(document).ready(function() {
    var whatLink = $(".whatIsThis");
    var message = $("#floatingMessageBox");
    message.hide();
    hideMessage = function() {
        message.slideUp(callback = function() {
            whatLink.html("What is this?");
        });
    };
    whatLink.click(function() {
        if (message.is(":visible")) {
            hideMessage()
        } else {
            message.slideDown(callback = function() {

            });
            whatLink.html("OK");
        }
        return false
    });
    message.mouseleave(hideMessage)
});