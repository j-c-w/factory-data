/**
 * Created by Jackson on 11/16/2014.
 */
$(document).ready(function() {
    var sessionIdContainer = $("#sessionId");
    var sessionId = sessionIdContainer.text();
    sessionIdContainer.hide();
    var conformationUrl = "cacheCheck/" + sessionId;
    var refreshDataInterval = setInterval(function() {
        $.ajax({
            url: conformationUrl,
            success: function(data){
                if (data === 'Values still being computed') {
                    return;
                }
                // the cache says it has the data loaded
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
                // If there was an error, then that means that data is not going to be computed.
                // We should ask the user to run this query again.
                $("#dataDiv").html("<p class='center'>Something went wrong. Please retry your query.</p>")
                $("#dataLoading").hide();
                clearInterval(refreshDataInterval);
            }
        })
    }, 2000);
});