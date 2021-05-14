// Class: SE2840 - Lab 4 - Coin Flip Charter
// Name: David Schulz
// Class Section: 041

// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false

class CoinFlipCharter {
    constructor() {
        // Constructor with private and public functions as needed
        let context; // the graphics context of the canvas; similar to a swing ContentPane
        let canvas; // the canvas element; similar to a java swing JFrame

        let chart; // the chartjs object
        let chartData; // data object to be supplied to the chart

        /**
         * onLoad
         * Invoked when the document as finished loading - initialize the Chart and set other events
         */
        const onLoad = () => {
            // Add additional variables if needed, but be sure to document them.

            // Get the DOM canvas element and context
            let jqCanvas = $("#chart1"); // note: jqCanvas is a jQuery object "wrapping" the underlying DOM canvas element
            canvas = jqCanvas.get(0); // get the DOM canvas element from the jQuery result
            context = canvas.getContext("2d"); // get the context from the canvas

            chart = null; // initialize the chart
            Chart.defaults.global.responsive = true; // make the chart responsive

            // Setup the button handler to call a function that updates the page
            $("#update").click(updateDisplay);

            // Call the below function that creates the "default" page that displays the full table and chart for the entire result set
            createDefaultDisplay();
        } // end onLoad()

        /**
         * createDefaultDisplay 
         * Create the "default" page - display all data with no filters
         */
        const createDefaultDisplay = () => {
            // Initialize the "chartData" object used by the Chartjs chart
            chartData = {
                labels: [],
                datasets: [{
                    label: 'Coin Flipper Execution Time',
                    data: []
                }]
            };
         
            // Convert the provided results object elements to the data format supported by chartjs
            // Iterate through the result set, insert the appropriate values into the "chartData" object
            // that will be used by the Chartjs object.
            let labels = [];
            let data = [];
            for (let i = 0; i < results.length; i++) {
                labels.push(i.toString());
                data.push(results[i].time);
            }
            chartData.labels = labels;
            chartData.datasets[0].data = data;
            
            // Display the chart. If an old chart exists, destroy it before creating a new one
            chart = new Chart(context, {
                type: 'bar',
                data: chartData
            });
            
            // Initialize the html of the table
            let table = $("#table1");
            table.append(
                '<thead>' +
                    '<tr>' +
                        '<th>Index</th>' +
                        '<th>ID</th>' +
                        '<th>Coins</th>' +
                        '<th>Flips</th>' +
                        '<th>Browser</th>' +
                        '<th>Time</th>' +
                    '</tr>' +
                '</thead>'
            );
            
            // Iterate through the result set, adding table rows and table data
            table.append('<tbody>');
            for (let i = 0; i < results.length; i++) {
                table.append(
                    '<tr id="' + i + '">' +
                        '<td>' + i + '</td>' +
                        '<td>' + results[i].id + '</td>' +
                        '<td>' + results[i].coins + '</td>' +
                        '<td>' + results[i].flips + '</td>' +
                        '<td>' + results[i].browser + '</td>' +
                        '<td>' + results[i].time + '</td>' +
                    '</tr>'
                );
            }
            table.append('</tbody>');
        } // end createDefaultDisplay()

        /**
         * updateDisplay 
         * Invoked when the Apply/Update button is pressed
         *    When it is called, it applies the specified filter expression to the result set, and
         *    redraws the table and chart with only the filtered results.
         */
        const updateDisplay = ()  => {
            // Determine which radio button is currently selected.
            // Retrieve the filter expression to determine what rows of the result set to show and hide.
            let filterBy = $("input[name=filters]:radio:checked").attr('id');
            filterBy = filterBy.substring(0, filterBy.length - 1); // Remove the 1 from the end of the string
            let value = $("#filter").val();

            // Iterate through the results data filtering based on filter value and type
            // Convert the provided results object elements to the data format supported by chartjs
            // Re-initialize the "data" object used by the Chartjs chart based on the filter value
            //    i.e. repopulate it with only the data that the filter does not remove
            let labels = [];
            let data = [];
            if (value !== "") {
                for (let i = 0; i < results.length; i++) {
                    if (filterBy === "id" || filterBy === "browser") {
                        if (results[i][filterBy].toLowerCase().includes(value.toLowerCase())) {
                            labels.push(i.toString());
                            data.push(results[i].time);
                        }
                    } else if (filterBy === "coins" || filterBy === "flips") {
                        if (results[i][filterBy].toString() === value) {
                            labels.push(i.toString());
                            data.push(results[i].time);
                        }
                    }
                }
            } else {
                for (let i = 0; i < results.length; i++) {
                    labels.push(i.toString());
                    data.push(results[i].time);
                }
            }
            chartData.labels = labels;
            chartData.datasets[0].data = data;
            
            // Display the chart. If an old chart exists, destroy it before creating a new one
            chart.data = chartData;
            chart.update();
            
            // Iterate through the results data filtering based on filter value and type
            // When done iterating, set the html of the table and re-display a filtered chart.
            // Data must not be removed from the result set - The 'filtered' data is hidden in the table
            // Thus, the indices of the subset will match those of the original data set.
            for (let i = 0; i < results.length; i++) {
                if (labels.includes(i.toString())) {
                    $("#"+i).show();
                } else {
                    $("#"+i).hide();
                }
            }
        } // end updateDisplay()
        
        // Set the event to call onLoad when the document is ready
        $(document).ready(onLoad);
    } // end constructor

    // Note: this method must be static because it is called to create the object instance.
    static create() {
        new CoinFlipCharter(); // create an instance of this class
    }

} // end CoinFlipCharter class