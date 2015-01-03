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
                // let the interval repeat, because the data has
                // not appeared in cache yet
            }
        })
    }, 1000);
});