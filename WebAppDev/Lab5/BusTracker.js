// Class: SE2840 - Lab 5 - AJAX Bus Tracker
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
        let table = null;
        let route = null;

        // The onLoad member function is called when the document loads and is used to perform initialization.
        this.onLoad = () => {
            let startPoint = [43.044240, -87.906446];// GPS lat/long location of MSOE athletic field
            map = createMap(startPoint);  // map this starting location (see code below) using MapQuest
            let loc = "MSOE Athletic Field";
			addMarker(map, startPoint, loc, "The place to be!");  // add a push-pin to the map

            table = $("#table");

            // initialize button event handlers
            $("#start").click(doAjaxRequest);
            $("#stop").click(stopTimer);
        };  // end onLoad function

        // NOTE: Remaining helper functions are all inner functions of the constructor; thus, they have
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
            route = $("#route").val();
            let params = "key=" + mcts_key + "&rt=" + route;
            $.ajax({
                type: "GET",
                url : "https://msoe-web-apps.appspot.com/BusInfo", // the url of the MSOE proxy server returning the Ajax response
                data : params, // key and route, for example "key=ABCDEF123456789&rt=31"
                dataType: "json",
                success: handleSuccess,
                error: handleError
            });
            table.html("");
            update++;

            // When started, the timer should cause doAjaxRequest to be called every 5 seconds
            if (timer == null) {
                // This indicates the first time going through the loop since the start button is clicked, so the map can be reset here
                map.remove();
                let startPoint = [43.044240, -87.906446];
                map = createMap(startPoint);
                let loc = "MSOE Athletic Field";
                addMarker(map, startPoint, loc, "The place to be!");
                update = 0;

                timer = setInterval(doAjaxRequest, 5000);
            }
        }; // end inner function doAjaxRequest

        // This function stops the timer and nulls the reference
        const stopTimer = () => {
            clearInterval(timer);
            timer = null;
        }; // end inner function stopTimer

        // This function is called if the Ajax request succeeds.
        // The response from the server is a JavaScript object!
        // Note that the Ajax request can succeed, but the response may indicate an error (e.g. if a bad route was specified)
        const handleSuccess = (response, textStatus, jqXHR) => {
            let label = $("#update");
            let errorMsg = null;
            if (response.status != null) {
                errorMsg = response.status;
            } else if (response["bustime-response"]["error"] != null) {
                errorMsg = response["bustime-response"]["error"][0]["msg"];
            }
            if (errorMsg != null) {
                label.get(0).setAttribute("style", "color:red");
                label.text("ERROR: " + errorMsg);
                stopTimer();
                return;
            }

            label.get(0).setAttribute("style", "color:black");
            label.text("Update: " + update);

            let innerhtml = "<thead><tr><th>Bus</th><th>Route " + route + "</th><th>latitude</th><th>longitude</th><th>speed(MPH)</th><th>dist(mi)</th></tr></thead>";
            innerhtml += "<tbody>";
            let buses = response["bustime-response"]["vehicle"];
            buses.forEach(bus => {
                let busID = bus["vid"];
                let dest = bus["des"];
                let lat = bus["lat"];
                let long = bus["lon"];
                let speed = bus["spd"];
                let distFt = bus["pdist"];
                let distMi = distFt / 5280;

                addMarker(map, [lat, long], busID, dest);

                innerhtml +=
                    "<tr>" +
                        "<td>" + busID + "</td>" +
                        "<td>" + dest + "</td>" +
                        "<td>" + lat + "</td>" +
                        "<td>" + long + "</td>" +
                        "<td>" + speed + "</td>" +
                        "<td>" + distMi + "</td>" +
                    "</tr>";
            });
            innerhtml += "</tbody>";
            table.html(innerhtml);
        }; // end inner function handleSuccess

        // This function is called if the Ajax request fails (e.g. network error, bad url, server timeout, etc)
        const handleError = (jqXHR, textStatus, errorThrown) => {
            console.log("Error processing Ajax request!");
            let message = $("#update");
            message.get(0).setAttribute("style", "color:red");
            message.text("ERROR: There was an error processing the Ajax request");
        }; // end inner function handerError
        
    }; // end constructor
	
} // end class BusTracker

// when document loads, do some initialization
$(document).ready(() => {
    const myTracker = new BusTracker();
    myTracker.onLoad();
});
