import React from "react";
import Horses from "../img/horses.PNG";

class GetStarted extends React.Component {
    render() {
        return (
            <div>
                <h1>We Need Your Help</h1>
                <p>
                    Help us test our GAN by uploading any image and rating the resulting style change.
                </p>
                <img src={Horses} className="horse-image" alt="logo" />
                <button className="button" onClick={() => this.props.handler(1)}>Get Started</button>
            </div>
        );
    }
}

export default GetStarted;