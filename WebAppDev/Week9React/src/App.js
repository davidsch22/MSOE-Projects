// Tell the code inspection tool that we're writing ES6 compliant code:
// jshint esversion: 6
// Tell the code inspection tool that we're using "developer" classes (console, alert, etc)
// jshint devel:true
// See https://jshint.com/docs/ for more JSHint directives
// jshint unused:false


import React from "react";

class App extends React.Component{
    constructor(props) {
        super(props);
        //initialize the state
        this.state={
            currentTime: new Date().toLocaleString("en-US"),
            color: "black",
        }
        const updateTime=()=>{
            this.setState({currentTime: new Date().toLocaleString("en-US")});
        }
        //needs two args.
        setInterval(updateTime,1000);
    }

    render(){
        const changeColor=()=>{
            this.setState({
                color: "blue",
            });
        }

        const changeColor2=()=>{
            this.setState({
                color: "green",
            });
        }
        const keyPressFunction=(event)=>{
            this.setState({text: event.target.value});
        }
        return(
            <div>
                <h1 className={this.state.color}> This is current time: {this.state.currentTime}</h1>
                <h1 className="blue">You entered the text: {this.state.text} </h1>
                <button className="btn btn-primary" onClick={changeColor}>ChangeToBlue</button>
                <button className="btn btn-success" onClick={changeColor2}>ChangeToGreen</button>
                <br/>
                <textarea onKeyPress={keyPressFunction}>Enter you text</textarea>
            </div>
        );
    }
}

export default App;