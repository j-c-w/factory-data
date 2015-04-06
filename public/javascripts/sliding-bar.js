/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by Jackson on 1/4/2015.
 */

$(document).ready(function() {
    var restoreDiv = $("#sessionRestoreDiv");
    var restoreDivButton = $("#sessionRestoreButton");

    var premadeResults = $("#defaultSessionsBox");
    var premadeResultsButton = $("#defaultSessionsButton");

    restoreDiv.hide();

    restoreDivButton.click(function() {
        if (restoreDiv.is(":visible")) {
            restoreDiv.slideUp();
            var text = minusToPlus(restoreDivButton.text());
            restoreDivButton.text(text)
        } else {
            restoreDiv.slideDown();
            var text = plusToMinus(restoreDivButton.text());
            restoreDivButton.text(text)
        }

        return false;
    });

    premadeResultsButton.click(function () {
        if (premadeResults.is(":visible")) {
            premadeResults.slideUp();
            var text = minusToPlus(premadeResultsButton.text());
            premadeResultsButton.text(text)
        } else {
            premadeResults.slideDown();
            var text = plusToMinus(premadeResultsButton.text());
            premadeResultsButton.text(text)
        }

        return false;
    });

    var plusToMinus = function (currentText) {
        currentText = currentText.substring(0, currentText.length - 1);
        currentText = currentText + " -";
        return currentText;
    };

    var minusToPlus = function (currentText) {
        currentText = currentText.substring(0, currentText.length - 1);
        currentText = currentText + " +";
        return currentText;
    };

});