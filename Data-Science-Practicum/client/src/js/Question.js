import React from "react";
import "../css/question.css";

class Question extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name: props.name,
            question: props.question,
            score: -1
        };
        this.onChangeValue = this.onChangeValue.bind(this);
    }

    onChangeValue(event) {
        this.setState({
            score: event.target.value
        });
    }

    getScore() {
        return this.state.score;
    }

    render() {
        let name = this.state.name;

        return (
            <div className="Question" onChange={this.onChangeValue}>
                <p>{this.state.question}</p>
                <label htmlFor={name+"0"}>0<br/>
                    <input type="radio" id={name+"0"} name={name} value="0"/>
                </label>
                <label htmlFor={name+"1"}>1<br/>
                    <input type="radio" id={name+"1"} name={name} value="1"/>
                </label>
                <label htmlFor={name+"2"}>2<br/>
                    <input type="radio" id={name+"2"} name={name} value="2"/>
                </label>
                <label htmlFor={name+"3"}>3<br/>
                    <input type="radio" id={name+"3"} name={name} value="3"/>
                </label>
                <label htmlFor={name+"4"}>4<br/>
                    <input type="radio" id={name+"4"} name={name} value="4"/>
                </label>
                <label htmlFor={name+"5"}>5<br/>
                    <input type="radio" id={name+"5"} name={name} value="5"/>
                </label>
                <label htmlFor={name+"6"}>6<br/>
                    <input type="radio" id={name+"6"} name={name} value="6"/>
                </label>
                <label htmlFor={name+"7"}>7<br/>
                    <input type="radio" id={name+"7"} name={name} value="7"/>
                </label>
                <label htmlFor={name+"8"}>8<br/>
                    <input type="radio" id={name+"8"} name={name} value="8"/>
                </label>
                <label htmlFor={name+"9"}>9<br/>
                    <input type="radio" id={name+"9"} name={name} value="9"/>
                </label>
                <label htmlFor={name+"10"}>10<br/>
                    <input type="radio" id={name+"10"} name={name} value="10"/>
                </label>
            </div>
        );
    }
}

export default Question;