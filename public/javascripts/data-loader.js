/**
 * Created by Jackson on 11/16/2014.
 */
$(document).ready(function() {
    var sessionIdContainer = $("#sessionId");
    var sessionId = sessionIdContainer.text();
    sessionIdContainer.hide();
    var conformationUrl = "assets/images/gen/" + sessionId;
    var refreshDataInterval = setInterval(function() {
        $.ajax({
            url: conformationUrl,
            success: function(data){
                //the file exists, so load the data
                $.ajax({
                    url : "dataStart/" + sessionId,
                    success : function(result){
                        $("#dataDiv").html(result);
                        $("#dataLoading").hide();

                    }
                });
                clearInterval(refreshDataInterval);
            },
            error: function(data){
                //leave it, because the file does not exist
            }
        })
    }, 1000);
});