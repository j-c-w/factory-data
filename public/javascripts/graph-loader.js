/**
 * Created by Jackson on 11/16/2014.
 */

var pictureFileNameContainer;
var pictureFileName;

$(document).ready(function() {
    pictureFileNameContainer = $("#pictureFileName");
    pictureFileName = pictureFileNameContainer.text();
    pictureFileNameContainer.hide();
    var pictureUrl = "assets/images/gen/" + pictureFileName
    var refreshPictureInterval = setInterval(function() {
        $.ajax({
            url: pictureUrl,
            success: function(data){
                //the file exists, so load it
                $("#graphDisplay").attr("src", pictureUrl);
                $("#loadingGraphDisplay").hide();
                clearInterval(refreshPictureInterval);
            },
            error: function(data){
                //leave it, because the file does not exist
                alert(url)
            }
        })
    }, 1000);
});