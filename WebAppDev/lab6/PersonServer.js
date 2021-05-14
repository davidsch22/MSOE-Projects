// Class: SE2840 - Lab 6 - Person Tracker Node.js Server
// Name: David Schulz
// Class Section: 041

const personData = require('./PersonData.js');
const express = require('express');
const webpagedir = `${__dirname}/srv`;

const app = express();

app.use(express.static(webpagedir));

app.get('/', (req, res) => {
    res.redirect('PersonTracker.html');
});

app.get('/all', (req, res) => {
    res.json({
        status: "success",
        length: personData.length,
        values: personData,
    });
});
app.get('/firstname', (req, res) => {
    let filterValue = req.query.filtervalue;
    if (filterValue.length === 0) {
        res.json({
            status: "error",
            message: "Filter value cannot be empty",
        });
    } else {
        let filteredData = personData.filter(person => person.firstname.toLowerCase().includes(filterValue.toLowerCase()));

        res.json({
            status: "success",
            length: filteredData.length,
            values: filteredData,
        });
    }
});
app.get('/lastname', (req, res) => {
    let filterValue = req.query.filtervalue;
    if (filterValue.length === 0) {
        res.json({
            status: "error",
            message: "Filter value cannot be empty",
        });
    } else {
        let filteredData = personData.filter(person => person.lastname.toLowerCase().includes(filterValue.toLowerCase()));

        res.json({
            status: "success",
            length: filteredData.length,
            values: filteredData,
        });
    }
});
app.get('/age', (req, res) => {
    let filterValue = req.query.filtervalue;
    if (isNaN(filterValue) || !Number.isInteger(parseFloat(filterValue)) || parseInt(filterValue) <= 0) {
        res.json({
            status: "error",
            message: "Filter value is not an integer greater than zero",
        });
    } else {
        let filteredData = personData.filter(person => person.age === parseInt(filterValue));

        res.json({
            status: "success",
            length: filteredData.length,
            values: filteredData,
        });
    }
});
app.get('/hometown', (req, res) => {
    let filterValue = req.query.filtervalue;
    if (filterValue.length === 0) {
        res.json({
            status: "error",
            message: "Filter value cannot be empty",
        });
    } else {
        let filteredData = personData.filter(person => person.hometown.toLowerCase().includes(filterValue.toLowerCase()));

        res.json({
            status: "success",
            length: filteredData.length,
            values: filteredData,
        });
    }
});

app.listen(3000, () => {
    console.log("Listening at http://localhost:3000");
});