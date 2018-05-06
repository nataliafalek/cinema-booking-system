import React, {Component} from 'react';
import PersonalData from "./PersonalData";
import Schedule from "./Schedule";
import Seats from "./Seats";
import Movies from "./Movies";
import MovieDetails from "./MovieDetails";
import {Route, HashRouter, NavLink} from 'react-router-dom';
import ReservationSummary from "./ReservationSummary";
import PaymentSuccess from "./PaymentSuccess";

class App extends Component {

  constructor() {
    super();
    this.state = {};

  }

  render() {
    return (
      <div className="App">
        <HashRouter>
          <div>
            <ul className="header">
              <NavLink className={"nav"} to="/movies"><img src={require('./assets/logo-palmy.jpg')} alt=""/>
              </NavLink>
              <li><NavLink className={"nav"} to="/movies">Movies</NavLink></li>
              <li><NavLink className={"nav"} to="/whatsOn">What's On</NavLink></li>
            </ul>
            <div className="content">
              <Route path="/movies" component={Movies}/>
              <Route exact path="/" component={Movies}/>
              <Route path="/movieDetails/:chosenMovieId" component={MovieDetails}/>
              <Route path="/whatsOn" component={Schedule}/>
              <Route path="/seats/:scheduledMovieId" component={Seats}/>
              <Route path="/personalData" component={PersonalData}/>
              <Route path="/reservationSummary" component={ReservationSummary}/>
              <Route path="/paymentSuccess" component={PaymentSuccess}/>
            </div>
          </div>
        </HashRouter>
      </div>
    );
  }
}

export default App;
