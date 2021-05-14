// Class: SE2840 - Lab 8 - React Bus Tracker
// Name: David Schulz
// Class Section: 041

// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

const mcts_key = "hmBsmnXxhyyiYN6ACQBMmBFWW"; // Your MCTS key here
const mapquest_key = "MP1A85j0nJYbQ2GYXUlqzFLiZXnwUINQ"; // Your MapQuest key here

class BusTracker {
	constructor() {
        // Note: Any "private" variables you create via "let x=..." will be visible to all functions below
        let timer = null;       // an interval timer
        let update = 0;         // update counter
        let map = null;         // reference to the MapQuest map object
        let route = "";
        let buses = [];

        // The onLoad member function is called when the document loads and is used to perform initialization.
        this.onLoad = () => {
            let startPoint = [43.044240, -87.906446];// GPS lat/long location of MSOE athletic field
            map = createMap(startPoint);  // map this starting location (see code below) using MapQuest

            let loc = "MSOE Athletic Field";
			addMarker(map, startPoint, loc, "The place to be!");  // add a push-pin to the map

            // initialize button event handlers
            $("#start").click(doAjaxRequest);
            $("#stop").click(stopTimer);
        };  // end onLoad function

        //NOTE: Remaining helper functions are all inner functions of the constructor; thus, they have
        // access to all variables declared within the constructor.

        // Create a MapQuest map centered on the specified position. If the map already exists, update the center point of the map per the specified position
        // param position - a GPS array of [lat,long] containing the coordinates to center the map around.
        // Note: You must set a finite size for the #map element using CSS; otherwise it won't appear!
        const createMap = (position) => {
            L.mapquest.key = mapquest_key;
            // 'map' refers to a <div> element with the ID map
            const map = L.mapquest.map('map', {
                center: position,
                layers: L.mapquest.tileLayer('map'),
                zoom: 14
            });
            map.addControl(L.mapquest.control()); // use alternate map control
            return map;
        }; // end inner function displayMap

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
            }).addTo(map);
        }; // end inner function addMarker

        // This function executes an Ajax request to the server
        const doAjaxRequest = () => {
            if(timer === null) {
                clearDisplay();
                route = $("#route").val();
                if (route.length <= 0) {
                    displayErrorMessage("Please enter a route");
                    stopTimer();
                    return;
                }
                update = 0;
            }
            let params = "key=" + mcts_key + "&rt=" + route;
            $.ajax({
                type: "GET",        // Issue a GET request
                url : "https://msoe-web-apps.appspot.com/BusInfo", // the url of the MSOE proxy server returning the Ajax response
                data : params, // key and route, for example "key=ABCDEF123456789&rt=31"
                crossDomain: true,  // Access data from another domain
                async: true,        // Make the request in the background
                dataType: "json",   // Format results in JSON
                success: handleSuccess,
                error: handleError,
            });
            // Start an interval timer only if one has not already been started (to avoid duplicates)
            // When started, it should cause doAjaxRequest to be called every 5 seconds
            if(timer === null) {
                timer = setInterval(doAjaxRequest, 5000);
            }
        }; // end inner function doAjaxRequest

        // This function stops the timer and nulls the reference
        const stopTimer = () => {
            if(timer !== null) {
                clearInterval(timer);
                timer = null;
            }
        }; // end inner function stopTimer

        // This function is called if the Ajax request succeeds.
        // The response from the server is a JavaScript object!
        // Note that the Ajax request can succeed, but the response may indicate an error (e.g. if a bad route was specified)
        const handleSuccess = (response, textStatus, jqXHR) => {
            // Check to ensure that the response does not indicate an error such as a bad route number, missing key, or invalid key!
            if(response["bustime-response"] !== undefined && response["bustime-response"].error !== undefined) {
                displayErrorMessage(`Error response for route: ${route}: ${response["bustime-response"].error[0].msg}`);
                stopTimer();
                return;
            }
            if(response.status !== undefined) {
                displayErrorMessage(`Error: ${response.status}`);
                stopTimer();
                return;
            }

            buses = [];

            // Iterate through the response to create the table and markers for the map
            if(response["bustime-response"] !== undefined) {
                if(response["bustime-response"].vehicle !== undefined) {
                    response["bustime-response"].vehicle.forEach((element) => {
                        // Get the bus number, destination, latitude, longitude, speed, and distance of each bus
                        const bus = element.vid;
                        const lat = element.lat;
                        const lon = element.lon;
                        buses.push({
                            bus: element.vid,
                            des: element.des,
                            lat: element.lat,
                            lon: element.lon,
                            spd: element.spd,
                            dist: element.pdist / 5280
                        });

                        // Add a marker to the map representing the updated position of each bus, along with information about the bus
                        addMarker(map, [lat, lon], bus, route);
                    });
                }
            }
            ReactDOM.render(<BusTrackerTable route={route} items={buses} />, document.getElementById("table"));

            // Update the display
            update++;
            displayUpdate(update);
        }; // end inner function handleSuccess

        // This function is called if the Ajax request fails (e.g. network error, bad url, server timeout, etc)
        const handleError = (jqXHR, textStatus, errorThrown) => {
            console.log("Error processing Ajax request!");
            stopTimer();
            displayErrorMessage("Request to retrieve data failed: " + textStatus);
        }; // end inner function handerError

        const clearDisplay = () => {
            $("#update").css("color", "black").html("").hide();
        }

        const displayErrorMessage = (message) => {
            $("#update").css("color", "red").html(message).show();
        }

        const displayUpdate = (count) => {
            $("#update").css("color", "black").html("Update: " + count).show();
        }
        
    } // end constructor

} // end class BusTracker

// when document loads, do some initialization
$(document).ready(() => {
    const myTracker = new BusTracker();
    myTracker.onLoad();
});
