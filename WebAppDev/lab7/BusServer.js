// Class: SE2840 - Lab 7 - MongoDB Bus Speed Checker
// Name: David Schulz
// Class Section: 041

const express = require('express');
const webpagedir = `${__dirname}/srv`;
const mongoose = require('mongoose');

const app = express();
const connection = mongoose.connect('mongodb://localhost/bustracker', {useNewUrlParser: true, useUnifiedTopology: true});

let busSchema = new mongoose.Schema({
    rt: {type: String},
    vid: {type: String},
    spd: {type: Number},
    tmstmp: {type: String},
    lat: {type: String},
    lon: {type: String}
});

let buses = mongoose.model('buses', busSchema);

app.use(express.static(webpagedir));

app.get('/', (req, res) => {
    res.redirect('BusSpeed.html');
});

app.get('/BusSpeed', (req, res) => {
    let speed = req.query.spd;
    if (isNaN(speed) || !Number.isInteger(parseFloat(speed)) || parseInt(speed) <= 0) {
        res.json({
            status: "error",
            message: "Speed is not an integer greater than zero",
        });
    } else {
        buses.find({spd: {$gte: speed}}, (err, foundBuses) => {
            if (err) {
                res.json({
                    status: "error",
                    message: "Query to MongoDB database failed",
                });
            } else {
                res.json({
                    status: "success",
                    length: foundBuses.length,
                    values: foundBuses,
                });
            }
        });
    }
});

app.listen(3000, () => {
    console.log("Listening at http://localhost:3000");
});