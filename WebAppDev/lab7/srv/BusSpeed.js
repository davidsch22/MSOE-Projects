// Class: SE2840 - Lab 7 - MongoDB Bus Speed Checker
// Name: David Schulz
// Class Section: 041

// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

const mapquest_key = "MP1A85j0nJYbQ2GYXUlqzFLiZXnwUINQ";  // Replace with your MapQuest Key

class BusSpeedChecker {
    constructor() {
        // Note: Any "private" variables you create via "let x=..." will be visible to all functions below
        let map = null;         // reference to the MapQuest map object
        let layerGroup = null;  // reference to the layer group for removing markers

        // The onLoad member function is called when the document loads and is used to perform initialization.
        this.onLoad = () => {
            createMap();  // Initialize the MapQuest map
            $("#displayButton").click(doDisplay); // initialize button event handler
            $("#resultsText").hide();
        };  // end onLoad function

        // NOTE: Remaining helper functions are all inner functions of the constructor; thus, they have
        // access to all variables declared within the constructor.

        // Create a MapQuest
        // Note: You must set a finite size for the #map element using CSS; otherwise it won't appear!
        const createMap = () => {
            L.mapquest.key = mapquest_key;
            // 'map' refers to a <div> element with the ID map
            map = L.mapquest.map('map', {
                center: [39.8283, -98.5795],  // center of the US
                layers: L.mapquest.tileLayer('map'),
                zoom: 4 // Zoom level to display all of US
            });
            map.addControl(L.mapquest.control()); // use alternate map control
            layerGroup = L.layerGroup().addTo(map);
        }; // end inner function createMap

        // This function adds a "push-pin" marker to the existing map
        // param map - map object (returned from createMap method
        // param position - the [lat, long] position of the marker on the map
        // param description - text that appears next to the marker
        // param popup - the text that appears when a user hovers over the marker
        const addMarker = (map, position, description, popup) => {
            L.mapquest.textMarker(position, {
                text: description,
                title: popup,
                position: 'right',
                type: 'marker',
                icon: {
                    primaryColor: '#FF0000',
                    secondaryColor: '#00FF00',
                    size: 'sm'
                }
            }).addTo(layerGroup);
        }; // end inner function addMarker

        // This function executes the click action on the display button
        const doDisplay = () => {
            // Filter based on the selected radio button
            const filterValue = $("#speedInput").val();
            doAjaxRequest("BusSpeed", filterValue);
        };// end inner function doDisplay

        // This function executes an Ajax request to the server
        const doAjaxRequest = (request, filterValue) => {
            let requestData = `spd=${filterValue}`;
            $.ajax({
                type: "GET",        // Issue a GET request
                url : `http://localhost:3000/${request}`, // Resource to request
                data : requestData, // Data for the request
                async: true,        // Make the request in the background
                dataType: "json",   // Format results in JSON
                success: handleSuccess,
                error: handleError,
            });
        }; // end inner function doAjaxRequest

        // This function is called if the Ajax request succeeds.
        // The response from the server is a JavaScript object!
        // Note that the Ajax request can succeed, but the response may indicate an error
        const handleSuccess = (response, textStatus, jqXHR) => {
            clearDisplay();
            clearTable();
            clearMap();

            // Check to ensure that the response does not indicate an error!
            if(response.status === undefined) {
                displayErrorMessage("Bad response from server");
                return;
            }
            if(response.status === "error") {
                displayErrorMessage(`Error response: ${response.message}`);
                return;
            }
            if(response.length === 0) {
                displayErrorMessage("No results");
                return;
            }

            const filterValue = $("#speedInput").val();
            $("#resultsText").html(`Speeding buses >= ${filterValue} mph: ${response.values.length}`).show();

            let innerhtml = `<thead><tr><th>Bus</th><th>Latitude</th><th>Longitude</th><th>Speed(MPH)</th></tr></thead>`;
            innerhtml += "<tbody>";

            // Iterate through the response to create the table and markers for the map
            response.values.forEach((element) => {
                // Get the name, age, hometown and location of each person
                const route = element.rt;
                const busID = element.vid;
                const lat = element.lat;
                const lon = element.lon;
                const position = [lat, lon];
                const speed = element.spd;

                innerhtml += `<tr><td>${busID}</td><td>${lat}</td><td>${lon}</td><td>${speed}</td></tr>`;

                // Add a marker to the map representing the updated position of each bus, along with information about the bus
                addMarker(map, position, `${busID}:${speed}`, route);
            });

            innerhtml += "</tbody>";
            $("#table").html(innerhtml);
        }; // end inner function handleSuccess

        // This function is called if the Ajax request fails (e.g. network error, bad url, server timeout, etc)
        const handleError = (jqXHR, textStatus, errorThrown) => {
            console.log("Error processing Ajax request!");
            clearTable();
            clearMap();
            displayErrorMessage("Request to retrieve data failed: " + textStatus);
        }; // end inner function handerError

        // Clear the map of all old markers
        const clearMap = () => {
            layerGroup.clearLayers();  // clear old markers
        };

        // Clear the table of all data
        const clearTable = () => {
            $("#table").empty();
        };

        // Clear the message display areas
        const clearDisplay = () => {
            $("#messages").css("color", "black").html("").hide();
            $("#resultsText").html("").hide();
        };

        // Display the given error messgae in the error display area
        const displayErrorMessage = (message) => {
            $("#messages").css("color", "red").html(message).show();
        };
    }; // end constructor

} // end class PersonTracker

// when document loads, do some initialization
$(document).ready(() => {
    const busSpeedChecker = new BusSpeedChecker();
    busSpeedChecker.onLoad();
});
