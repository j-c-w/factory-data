/**
 * Created by Jackson on 11/14/2014.
 */
$(document).ready(function () {
    $("#addOr").click(function() {
        $.get("forms/filter", function(data){
            $("#filterDiv").append(data);
        });
    });
    $("#addAggregate").click(function() {
        $.get("forms/aggregate", function(data){
            $("#aggregateDiv").append(data);
        });
    });

    $("#addSort").click(function() {
        $.get("forms/sort", function(data){
            $("#sortDiv").append(data);
        });
    });


});