import React from "react";
import '../css/body.css';
import GetStarted from "./GetStarted";
import Upload from "./Upload";
import Rate from "./Rate";
import End from "./End";

class TestFlow extends React.Component {
    constructor(props) {
        super(props);
        this.state = {view: 0};
        this.imageName = undefined;
        this.handler = this.handler.bind(this);
        this.setImageName = this.setImageName.bind(this);
        this.getImageName = this.getImageName.bind(this);
    }

    setImageName(imageName) {
        this.imageName = imageName;
    }

    getImageName() {
        return this.imageName;
    }

    handler(view) {
        this.setState({
            view: view
        });
    }

    render() {
        return (
            <div>
                {this.state.view === 0 ? <GetStarted handler={this.handler} /> : ''}
                {this.state.view === 1 ? <Upload handler={this.handler} setImageName={this.setImageName} /> : ''}
                {this.state.view === 2 ? <Rate handler={this.handler} getImageName={this.getImageName} /> : ''}
                {this.state.view === 3 ? <End handler={this.handler} /> : ''}
            </div>
        );
    }
}

export default TestFlow;