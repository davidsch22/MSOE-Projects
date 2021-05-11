import React from "react";
import axios from "axios";

class Upload extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedFiles: undefined
        };
        this.selectFile = this.selectFile.bind(this);
        this.uploadImage = this.uploadImage.bind(this);
    }

    selectFile(event) {
        this.setState({
            selectedFiles: event.target.files
        });
    }

    uploadImage(event) {
        event.preventDefault();
        if (this.state.selectedFiles === undefined) {
            alert("Must select a file");
            return;
        }
        let currentFile = this.state.selectedFiles[0];

        let formData = new FormData();
        formData.append("file", currentFile);

        axios.post("/upload", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            }
        }).then((response) => {
            this.props.setImageName(response.data.filename);
            this.props.handler(2);
        });
    }

    render() {
        return (
            <div>
                <h1>Upload Image</h1>
                <form onSubmit={this.uploadImage}>
                    <input type="file" name="file" accept="image/*" onChange={this.selectFile} />
                    <input className="button" type="submit" value="Upload" />
                </form>
            </div>
        );
    }
}

export default Upload;