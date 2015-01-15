/**
 * Created by Jackson on 11/14/2014.
 */
var selectedXAxis;

$(document).ready(function () {
    setupDataTools();

    $(".submitButton").click(function() {
        document.queryForm.submit();
        return false;
    });

    $("#addFilter").click(function() {
        return addToForm("filter", function() {
            removeConnectors();
            setFilterListeners();
        })
    });
    $("#addAggregate").click(function() {
        return addToForm("aggregate", function() {
            $(".aggregateField:last").val(selectedXAxis);
            setDropdownListeners();
        })
    });
    $("#addSort").click(function() {
        return addToForm("sort");
    });
    $("#addGraph").click(function() {
        return addToForm("graph", oncomplete = function() {
            //set the value of the just-added value to the selected axis
            $(".xAxisDoublesOnly:last").val(selectedXAxis);
            $(".xAxisAll:last").val(selectedXAxis);
            setDropdownListeners();
            //after adding the form we need to hide what should not be shown -- i.e. the other part of the form
            hideRequiredXAxis($("#graphType").val());
        });
    });
    $("#addColumn").click(function() {
        return addToForm("column")
    });
    setDropdownListeners();
    setGraphAxisValues();
    setFilterListeners();
    updateListeners();
    hideRequiredXAxis($("#graphType").val());
    //se also set the selected xAxis to the default
    selectedXAxis = $(".aggregateField:last").val()
    if (selectedXAxis === undefined) {
    }

});

setFilterListeners = function () {
    var field = $(".filterComparator");
    field.change(function () {
        checkFilter($(this), $(this).parent().children("#filterValue"))
    });

    field.each(function (index, element) {
        checkFilter($(element), $(element).parent().children("#filterValue"))
    });
};

checkFilter = function (filter, valueBox) {
    if (filter.val() == "has data") {
        valueBox.hide()
    } else {
        valueBox.show()
    }
};

removeConnectors = function() {
    if ($("#filterDiv div:first #filterConnector").length != 0) {
        //in this case, the first filterDiv has a filterConnector attached
        //to it so we must remove it.
        $("#filterConnector:first").remove()
    }
};

setGraphAxisValues = function() {
    $("#graphType").change(function() {
        hideRequiredXAxis($(this).val())
    })
};

hideRequiredXAxis = function(value) {
    if (value === "Bar Chart") {
        $(".xAxisAll").show();
        $(".xAxisDoublesOnly").hide();
    } else if (value === "Line Graph" || value === "Scatter Plot") {
        $(".xAxisAll").hide();
        $(".xAxisDoublesOnly").show();
    } else {
        alert("Error: " + value + " is not a valid graph type")
    }

    if (value === "Scatter Plot") {
        // show all the regression dropdowns
        $(".regressionsSelect").show();
    } else {
        $(".regressionsSelect").hide();
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

    $(".aggregateField:last").val(value);
    selectedXAxis = value;
};

addToForm = function(type, oncomplete) {
    $("#" + type + "Div").append(tempLoadingBar());
    $.get("forms/" + type, function(data){
        $("#" + type + "Div #tempLoading:first").remove()
        $("#" + type + "Div").append(data);
        if (oncomplete != null) {
            oncomplete();
        }
        //this updates the delete buttons etc.
        updateListeners();

    });

    //this stops the links from being followed when they are clicked
    return false;
};

tempLoadingBar = function() {
    return '<p class="center" id="tempLoading"><img src="assets/images/loading.gif"></p>'
};

updateListeners = function() {
    $(".deleteButton").click(function() {
        $(this).parent().remove();
        //we need to call this in case we removed the second to last
        //filter.
        removeConnectors();
        return false
    });
};

setDropdownListeners = function() {
    $(".xAxisDoublesOnly").change(function() {
        updateXAxis(this.value)
    });
    $(".xAxisAll").change(function () {
        updateXAxis(this.value)
    });
    $(".aggregateField").off('change');
    $(".aggregateField:last").change(function() {
        updateXAxis(this.value)
    });
};

setupDataTools = function() {
    hideAllNoSlide = function() {
        //this is called the first time only
        //so I don't have to worry about the links
        filters.hide();
        aggregators.hide();
        sorting.hide();
        graphs.hide();
        columns.hide();
    };
    hideAll = function() {
        hideElem(filters, filterShow);
        hideElem(aggregators, aggregateShow);
        hideElem(sorting, sortShow);
        hideElem(graphs, graphShow);
        hideElem(columns, columnShow)
    };
    toggleElement = function(elem, link) {
        if (elem.is(":visible")) {
            elem.slideUp();
            hideAll();
        } else {
            hideAll();
            elem.slideDown();
            link.addClass("active")
        }
        return false
    };

    hideElem = function(elem, link) {
        elem.slideUp();
        link.removeClass("active")
    };
    //find all the bits of the form
    var filters = $("#filter");
    var aggregators = $("#aggregate");
    var sorting = $("#sort");
    var graphs = $("#graph");
    var columns = $("#column");
    //now we get all the buttons that we need
    var filterShow = $("#filterShow");
    var aggregateShow = $("#aggregateShow");
    var sortShow = $("#sortShow");
    var graphShow = $("#graphShow");
    var columnShow = $("#columnShow");
    //first, hide all the form bits
    hideAllNoSlide();
    //now set the click listeners for the links where we show the appropriate fields and
    //hide the others
    filterShow.click(function() {
        return toggleElement(filters, filterShow)
    });
    aggregateShow.click(function() {
        return toggleElement(aggregators, aggregateShow)
    });
    sortShow.click(function() {
        return toggleElement(sorting, sortShow)
    });
    graphShow.click(function() {
        return toggleElement(graphs, graphShow)
    });
    columnShow.click(function() {
        return toggleElement(columns, columnShow)
    });



}