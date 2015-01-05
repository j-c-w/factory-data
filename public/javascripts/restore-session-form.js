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
 * Created by Jackson on 12/12/2014.
 */
var sessionID;

$(document).ready(function () {
    var form = $("#recoverSession");
    var sessionIDStore = $("#sessionIdStore");
    $("#submitSessionRestore").click(function() {
        var spinner = $("#serverCheckSpinner");
        var messageBox = $("#queryRestoreMessageBox");
        messageBox.text("");
        spinner.show();
        $.ajax({
            url: "assets/images/gen/" + sessionID,
            success: function() {
                spinner.hide();
                //successful - submit the form
                form.submit();
                messageBox.text("Restore Query...")
            },
            error: function() {
                spinner.hide();
                messageBox.slideDown();
                messageBox.text("oops! It looks like something went wrong!")
            }
        });
        return false;
    });

    sessionID = sessionIDStore.text();
    sessionIDStore.remove();
    $("#sessionIDEntryBox").val(sessionID);
});