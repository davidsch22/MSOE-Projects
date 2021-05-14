// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false


import React from "react";

class DOMComponent extends React.Component{
    render(){
        return(
            <h1>
                <p id="p1" className="text-primary">
                    Hello World
                    {this.props.message}

                </p>
            </h1>
        );
    }
}