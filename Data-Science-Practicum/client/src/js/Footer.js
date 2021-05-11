import React from "react";
import '../css/footer.css';

function Footer() {
    return (
        <footer className="Footer">
            <div className="container">
                <div className="row">
                    <h6>Quick Links</h6>
                    <ul className="footer-links">
                        <li><a href="/">Home</a></li>
                        <li><a href="/about">About</a></li>
                    </ul>
                </div>
                <hr/>
            </div>
            <div className="container">
                <div className="row">
                    <p className="copyright-text">
                        Copyright &copy; 2021 All Rights Reserved
                    </p>
                </div>
            </div>
        </footer>
    );
}

export default Footer;