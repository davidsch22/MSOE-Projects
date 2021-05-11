import React from "react";

class End extends React.Component {
    render() {
        return (
            <div>
                <h1>Thank you for your participation!</h1>
                <button className="button" onClick={() => this.props.handler(1)}>Test Another</button>
            </div>
        );
    }
}

export default End;