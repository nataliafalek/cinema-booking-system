import React, {Component} from 'react';
import BackButton from "./BackButton";
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';


class BackAndNextButton extends Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  chooseSeats = (seats) => {
    const chosenSeatIds = seats.map(seat => {
      return {seatId: seat.seat.seatId, ticketPriceId: seat.ticketPriceId}
    });
    return HttpService.postJson(`cinemaHall/seats/choose/${this.props.scheduledMovieId}`, chosenSeatIds)
      .then(data => {
        if (data.status === 200) {
          this.setState({redirect: true});
        }
      })
  };

  handleOnClick = () => {
    this.chooseSeats(this.props.chosenSeats);
  };

  render() {
    return (
      <div>
        {this.state.redirect ? <Redirect push to={'/personalData'}/> : null}
        <div className={"buttons"}>
          <BackButton/>
          <button className={"nextButton"} disabled={this.props.chosenSeats.length == 0}
                  onClick={this.handleOnClick}
                  type="button">Dalej
          </button>
        </div>
      </div>
    );
  }

}

export default BackAndNextButton;