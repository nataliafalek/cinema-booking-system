import React, {Component} from 'react';
import PersonalData from "./PersonalData";
import Schedule from "./Schedule";
import Movies from "./Movies";
import MovieDetails from "./MovieDetails";
import {HashRouter, NavLink, Route} from 'react-router-dom';
import ReservationSummary from "./ReservationSummary";
import PaymentSuccess from "./PaymentSuccess";
import SeatReservation from "./SeatReservation";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap-theme.min.css';

class App extends Component {
  constructor() {
    super();
    this.state = {};
  }

  render() {
    return <div className={"myApp"}>
      <main role="main">
        <HashRouter>
          <div>
            <ul className="header">
              <li><NavLink className={"nav"} id="brand" to="/"> <span
                className={"glyphicon glyphicon-star"}></span> NatiCinema</NavLink></li>
              <li><NavLink className={"nav"} to="/whatsOn"> repertuar</NavLink></li>
            </ul>
            <div className="content">
              <Route path="/movies" component={Movies}/>
              <Route exact path="/" component={Movies}/>
              <Route path="/movieDetails/:chosenMovieId" component={MovieDetails}/>
              <Route path="/whatsOn" component={Schedule}/>
              <Route path="/seats/:scheduledMovieId" component={SeatReservation}/>
              <Route path="/personalData" component={PersonalData}/>
              <Route path="/reservationSummary" component={ReservationSummary}/>
              <Route path="/paymentSuccess" component={PaymentSuccess}/>
            </div>
          </div>
        </HashRouter>
        <footer>
          <div className="footer-copyright text-center py-3">
            <a href="https://github.com/nataliafalek/cinema-booking-system">
              <span className={"glyphicon glyphicon-heart"}></span>
              Github
            </a>
          </div>
        </footer>
      </main>
    </div>
  }
}

export default App;
