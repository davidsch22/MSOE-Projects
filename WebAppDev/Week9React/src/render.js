// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false


import ReactDOM from "react-dom";
import React from "react";
import App from "./App";

$(document).ready(()=>{

    ReactDOM.render(<App/>,document.getElementById("div1"))

})