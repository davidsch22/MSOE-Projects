// Class: SE2840 - Midterm Part 2 - Valid Message Recorder
// Name: David Schulz
// Class Section: 041

// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

class ValidMessager {
    constructor() {
        let table;
        let errorMessage;

        /**
         * onLoad
         * Invoked when the document is finished loading
         */
        const onLoad = () => {
            $("#add").click(addMessage);
            $("#removeinvalid").click(removeInvalid);

            table = $("#inputvalues > tbody");
            errorMessage = $("#errormessage");
        }

        /**
         * addMessage
         * Invoked when the Add Value button is pressed
         */
        const addMessage = ()  => {
            let message = $("#userinput").val();
            let length = message.length;

            if (length < 3 || length > 100) {
                errorMessage.text("ERROR: Message must be between 3 and 100 characters.");
                return;
            } else {
                errorMessage.text("");
            }

            table.append(
                '<tr>' +
                    '<td id="message"></td>' +
                    '<td>' + length + '</td>' +
                '</tr>'
            );
            $("#message").text(message);
            $("#message").removeAttr("id");
        }

        /**
         * removeInvalid
         * Invoked when the Clear Values button is pressed
         */
        const removeInvalid = ()  => {
            table.empty();
            errorMessage.text("");
        }

        $(document).ready(onLoad);
    }

    static create() {
        new ValidMessager(); // create an instance of this class
    }
}