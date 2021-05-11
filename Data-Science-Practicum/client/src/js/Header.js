import React from "react";
import '../css/header.css';
import logo from "../img/logo.svg";
import { NavLink } from "react-router-dom";

function Header() {
    return (
        <ul className="Header">
            <li><img src={logo} className="App-logo" alt="logo" /></li>
            <li><NavLink exact activeClassName="active" to="/">Home</NavLink></li>
            <li><NavLink activeClassName="active" to="/about">About</NavLink></li>
        </ul>
    );
}

export default Header;