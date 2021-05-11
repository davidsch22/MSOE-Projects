import React from "react";
import { Switch, Route, withRouter } from "react-router";
import { BrowserRouter } from "react-router-dom";
import '../css/app.css';
import Header from "./Header";
import TestFlow from "./TestFlow";
import About from "./About";
import Footer from "./Footer";

function App() {
    return (
        <BrowserRouter>
            <div>
                <Header/>
                <div className="site-body">
                    <Switch>
                        <Route exact path="/" component={withRouter(TestFlow)}/>
                        <Route exact path="/about" component={withRouter(About)}/>
                    </Switch>
                </div>
                <Footer/>
            </div>
        </BrowserRouter>
    );
}

export default App;