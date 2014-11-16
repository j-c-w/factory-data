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
    $("#addGraph").click(function() {
        addToForm("graph")
    });
    setDropdownListeners();
});

updateXAxis = function(value) {
    var elements = $(".xAxis");
    for(var i=0; i<elements.length; i++){
        var element = elements.eq(i);
        element.val(value);
    }

    $(".aggregateField:last").val(value)
};

addToForm = function(type) {
    $.get("forms/" + type, function(data){
        $("#" + type + "Div").append(data);
        setDropdownListeners();
    });
};

setDropdownListeners = function() {
    $(".xAxis").change(function() {
        updateXAxis(this.value)
    });
}