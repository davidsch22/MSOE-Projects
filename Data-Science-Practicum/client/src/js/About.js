import React from "react";
import '../css/about.css';
import Examples from "../img/examples.png";

class About extends React.Component {
    render() {
        return (
            <div className="About">
                <h2>
                    <em><u>Photo Altering GAN</u></em>
                </h2>
                <img src={Examples} className="Cycle-GAN" alt="GAN"/>

                <div>
                    <h3>
                        <em>About the Project</em>
                    </h3>
                    <p>
                        Hello! We are a group of 5 computer science students from MSOE working
                        on a Photo Altering generative adversarial networks (GAN) project with our project sponsors
                        Discovery World. The goal of the project is to create a minimum viable product (MVP) that alters
                        images that are taken of patrons and change the style of the image(e.g., monet style).
                        We are highly motivated in making a fun and engaging product for the customers of
                        the museum!
                    </p>
                </div>

                <h3 className="Team-Header">
                    <em>Team Members:</em>
                </h3>
                <div className="Wrapper">
                    <h3 className="Names">Hayden Klein</h3>
                    <div className="Grade-Level">Grade level: Junior Standing</div>
                    <div className="Major">Major: Computer Science</div>
                    <div className="Contact">email: kleinh@msoe.edu</div>
                </div>
                <div className="Wrapper">
                    <h3 className="Names">Leah Ersoy</h3>
                    <div className="Grade-Level">Grade level: Junior Standing</div>
                    <div className="Major">Major: Computer Science</div>
                    <div className="Contact">email: ersoyl@msoe.edu</div>
                </div>
                <div className="Wrapper">
                    <h3 className="Names">Nicholas Johnson</h3>
                    <div className="Grade-Level">Grade level: Junior Standing</div>
                    <div className="Major">Major: Computer Science</div>
                    <div className="Contact">email: johnsonn@msoe.edu</div>
                </div>
                <div className="Wrapper">
                    <h3 className="Names">David Schulz</h3>
                    <div className="Grade-Level">Grade level: Junior Standing</div>
                    <div className="Major">Major: Computer Science</div>
                    <div className="Contact">email: schulzd@msoe.edu</div>
                </div>
                <div className="Wrapper">
                    <h3 className="Names">Jesus Ramos</h3>
                    <div className="Grade-Level">Grade level: Junior Standing</div>
                    <div className="Major">Major: Computer Science</div>
                    <div className="Contact">email: ramosj@msoe.edu</div>
                </div>
            </div>
        );
    }
}

export default About;