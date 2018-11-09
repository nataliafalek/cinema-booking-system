import React, {Component} from 'react';
import CinemaHall from "./CinemaHall";
import ChosenSeatsList from "./ChosenSeatsList";
import BackAndNextButton from "./SeatReservationButtons";

class SeatReservation extends Component {
  constructor(props) {
    super(props);
    this.state = {
      chosenSeats: []
    };
  }

  chosenSeatsChanged = (chosenSeats) => {
    this.setState({chosenSeats: chosenSeats})
  };

  render() {
    return (
      <div className={"container"}>
      <div className={"seats"}>
        <CinemaHall scheduledMovieId={this.props.match.params.scheduledMovieId}
                    chosenSeats={(chosenSeats) => this.chosenSeatsChanged(chosenSeats)}/>
        <ChosenSeatsList
          chosenSeatsChanged={(newChosenSeats) => this.chosenSeatsChanged(newChosenSeats)}
          chosenSeats={this.state.chosenSeats}
          scheduledMovieId={this.props.match.params.scheduledMovieId}/>
        <BackAndNextButton
          chosenSeats={this.state.chosenSeats}
          scheduledMovieId={this.props.match.params.scheduledMovieId}
        />
      </div>
      </div>
    );
  }
}


export default SeatReservation;

