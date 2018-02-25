import React, {Component} from 'react';
import PersonalData from "./PersonalData";
import Movie from "./Movie";
import Seats from "./Seats";
import TransactionSummary from "./TransactionSummary";
import {Route, HashRouter} from 'react-router-dom';

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
                    <div className="content">
                        <Route path="/movie" component={Movie}/>
                        <Route path="/seats/:scheduledMovieId" component={Seats}/>
                        <Route path="/personalData/:scheduledMovieId/:seatId" component={PersonalData}/>
                        <Route path="/transactionSummary/:reservationId" component={TransactionSummary}/>
                    </div>
                </HashRouter>
            </div>
        );
    }
}

export default App;
