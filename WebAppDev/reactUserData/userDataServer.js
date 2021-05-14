// Import the express library
const express = require('express');
const userData = require('./userData.js');

// Create a new express application
const app = express();

// Instruct the app to listen on port 3000
app.listen(3000, () => {
    console.log("Server is running at http://localhost:3000");
});

// Set a static route for all resources that don't have an explicit route
//   to use the static directory webpagedir
app.use(express.static(`${__dirname}/public`));

// Send all user data
app.get("/all", (request, response) => {
    response.json({
        status: "success",
        length: userData.length,
        values: userData,
    });
});

