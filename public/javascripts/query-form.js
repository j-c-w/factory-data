/**
 * Created by Jackson on 11/14/2014.
 */
$(document).ready(function () {
    $("#addOr").click(function() {
        addToForm("filter")
    });
    $("#addAggregate").click(function() {
        addToForm("aggregate")
    });

    $("#addSort").click(function() {
        addToForm("sort");
    });
});

addToForm = function(type) {
    $.get("forms/" + type, function(data){
        $("#" + type + "Div").append(data);
    });
};

