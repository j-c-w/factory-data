/**
 * Created by Jackson on 11/16/2014.
 */

var pictureFileNameContainer;
var pictureFileName;

$(document).ready(function() {
    pictureFileNameContainer = $("#pictureFileName");
    pictureFileName = pictureFileNameContainer.text();
    pictureFileNameContainer.hide();
    var pictureUrl = "/cacheCheck/" + pictureFileName;
    alert(pictureFileName);
    var refreshPictureInterval = setInterval(function() {
        $.ajax({
            url: pictureUrl,
            success: function(data){
                if (data === 'Values still being computed') {
                    return;
                }
                // the graph exists, so load it into the div.
                loadGraph(pictureFileName);
                clearInterval(refreshPictureInterval);
            },
            error: function(data, status, error){
                $("#graphDisplayDiv").html("<p class='center'>Something went wrong. Please retry your query.</p>");
                clearInterval(refreshPictureInterval);
            }
        })
    }, 1000);
});

loadGraph = function (pictureName) {
    // We start the ajax that we need to load the file
    // from the server.
    var graphDiv = $("#graphDisplayDiv");
    $.ajax({
        url: "/loadPicture/" + pictureName,
        success: function (loadedData) {
            graphDiv.html(loadedData)
        },
        error: function(errror) {
            graphDiv.html("<p class='center'>oops! we messed up. Could you try that again?</p>")
        }
    })
};