import React, {Component} from 'react';
import PersonalData from "./PersonalData";
import Movie from "./Movie";
import Seats from "./Seats";
import Home from "./Home";
import TransactionSummary from "./TransactionSummary";
import {Route, HashRouter, NavLink} from 'react-router-dom';
import ReservationSummary from "./ReservationSummary";
import PaymentSuccess from "./PaymentSuccess";

//TODO stworzyc dwie nowe klasy jedna to czesc odnosnie movie, jedna to czesc seat

class App extends Component {

    constructor() {
        super();
        this.state = {};
    }

    //TODO stworzyc sesje dla użytkownika
    render() {
        return (
            <div className="App">
                <HashRouter>
                    <div>
                        <ul className="header">
                            <li><NavLink to="/home">Home</NavLink></li>
                            <li><NavLink to="/whatsOn">What's On</NavLink></li>
                        </ul>
                        <div className="content">
                            <Route path="/home" component={Home}/>
                            <Route path="/whatsOn" component={Movie}/>
                            <Route path="/seats/:scheduledMovieId" component={Seats}/>
                            <Route path="/personalData/:scheduledMovieId/:seatId" component={PersonalData}/>
                            <Route path="/transactionSummary/:reservationId" component={TransactionSummary}/>
                            <Route path="/reservationSummary/:reservationId" component={ReservationSummary}/>
                            <Route path="/paymentSuccess" component={PaymentSuccess}/>
                        </div>
                    </div>
                </HashRouter>
            </div>
        );
    }
}

export default App;
