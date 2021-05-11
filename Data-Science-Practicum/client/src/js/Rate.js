import React from "react";
import Question from "./Question";
import axios from "axios";

class Rate extends React.Component {
    constructor(props) {
        super(props);
        this.structureQ = React.createRef();
        this.styleQ = React.createRef();
        this.offensiveQ = React.createRef();
        this.fileName = undefined;
        this.submitRating = this.submitRating.bind(this);
    }

    submitRating(event) {
        event.preventDefault();
        let structure = this.structureQ.current.getScore();
        let style = this.styleQ.current.getScore();
        let offensive = this.offensiveQ.current.getScore();
        if (structure === -1 || style === -1 || offensive === -1) {
            alert("All 3 questions must be answered");
            return;
        }
        const rating = {
            datetime: new Date(Date.now()).toString(),
            image: this.fileName,
            structure: structure,
            style: style,
            offensive: offensive
        };
        axios.post('/rate', rating).then(() => this.props.handler(3));
    }

    render() {
        let imageName = this.props.getImageName();
        this.fileName = imageName;

        return (
            <div className="container">
                <h1>Rate the Result</h1>
                <div className="row">
                    <div className="col-4">
                        <div className="row">
                            <h4>Original</h4>
                            <img src={"/images?name="+imageName+"&type=in"} alt="original" />
                        </div>
                        <div className="row">
                            <h4>Result</h4>
                            <img src={"/images?name="+imageName+"&type=out"} alt="result" />
                        </div>
                    </div>
                    <form className="col-8" style={{textAlign: "left"}} onSubmit={this.submitRating}>
                        <Question ref={this.structureQ} name="structure" question="How well does the output picture retain the structure of the original picture?" />
                        <Question ref={this.styleQ} name="style" question="How well does the output picture replicate the artist's style?" />
                        <Question ref={this.offensiveQ} name="offensive" question="How racially insensitive or disturbing is the output picture?" />
                        <input className="button" type="submit" value="Submit" />
                    </form>
                </div>
            </div>
        );
    }
}

export default Rate;