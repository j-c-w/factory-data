/**
 * Created by Jackson on 11/14/2014.
 */
var selectedXAxis;

$(document).ready(function () {
    $("#addOr").click(function() {
        addToForm("filter")
    });
    $("#addAggregate").click(function() {
        addToForm("aggregate", function() {
            $(".aggregateField:last").val(selectedXAxis);
            setDropdownListeners();
        })
    });
    $("#addSort").click(function() {
        addToForm("sort");
    });
    $("#addGraph").click(function() {
        addToForm("graph", oncomplete = function() {
            //set the value of the just-added value to the selected axis
            $(".xAxisDoublesOnly:last").val(selectedXAxis);
            $(".xAxisAll:last").val(selectedXAxis);
            setDropdownListeners();
            //after adding the form we need to hide what should not be shown -- i.e. the other part of the form
            hideRequiredXAxis($("#graphType").val());
        });

    });
    setDropdownListeners();
    setGraphAxisValues();
    hideRequiredXAxis($("#graphType").val());
    //se also set the selected xAxis to the default
});

setGraphAxisValues = function() {
    $("#graphType").change(function() {
        hideRequiredXAxis(this.value)
    })
};

hideRequiredXAxis = function(value) {
    if (value === "Bar Chart") {
        $(".xAxisAll").show();
        $(".xAxisDoublesOnly").hide();
    } else if (value === "Line Graph") {
        $(".xAxisAll").hide();
        $(".xAxisDoublesOnly").show();
    } else {
        alert("Error: " + value + " is not a valid graph type")
    }
    setDropdownListeners();
};

updateXAxis = function(value) {
    var elements = $(".xAxisAll");

    for(var i = 0; i < elements.length; i ++){
        var element = elements.eq(i);
        element.val(value);
    }

    var elementsAgain = $(".xAxisDoublesOnly");
    for(var i = 0; i < elementsAgain.length; i ++){
        var elem = elementsAgain.eq(i);
        elem.val(value);
    }

    $(".aggregateField:last").val(value)
    selectedXAxis = value;
};

addToForm = function(type, oncomplete) {
    $.get("forms/" + type, function(data){
        $("#" + type + "Div").append(data);
        oncomplete()
    });
};

setDropdownListeners = function() {
    $(".xAxisDoublesOnly").change(function() {
        updateXAxis(this.value)
    });
    $(".xAxisAll").change(function () {
        updateXAxis(this.value)
    })
    $(".aggregateField").off('change');
    $(".aggregateField:last").change(function() {
        updateXAxis(this.value)
    });
}