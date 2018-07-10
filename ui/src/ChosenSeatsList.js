import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import './index.css';
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";

class ChosenSeatsList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      ticketPrices: [],
      chosenSeat: []
    };
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

  componentWillReceiveProps(nextProps){
    if(nextProps.chosenSeats!==this.props.chosenSeats){
      //Perform some operation
      this.setState({chosenSeat: this.props.chosenSeats });
    }
  }

  componentDidMount() {
    HttpService.fetchJson('ticketPriceList')
      .then(data => {
        this.setState({ticketPrices: data})
      })
  }

  handleOnClick = () => {
    this.chooseSeats(this.props.chosenSeats);
  };

  getTicketPrice = (priceId) => {
    const ticketPrice = this.state.ticketPrices.filter(ticketPrice =>
      // TODO Zamienic na ==
      ticketPrice.ticketPriceId == priceId);
    return ticketPrice[0].ticketValue
  }

  selectChange = (event, seat) => {
    const selectedPrice = event.target.value;
    const newChosenSeats =  this.props.chosenSeats.map(chosenSeat => {
      {
        if (chosenSeat.seat.seatId === seat.seat.seatId) {
          return {
            ...chosenSeat,
            ticketPriceId: selectedPrice
          }
        }
        return chosenSeat;
      }

    });
       this.setState({chosenSeats: newChosenSeats});
       console.log("chosenSeats", this.state.chosenSeats)
  }

  render() {
  return <div>

    {this.props.chosenSeats ? this.props.chosenSeats.map((seat, idx) =>
        <div className={"chosenSeats"} key={idx}>
          Seat number: {seat.seat.seatNumber}, row: {seat.seat.rowNumber}, Type:
          <select className={"ticketType"} key={idx} onChange={event => this.selectChange(event, seat)}>
            {this.state.ticketPrices.map((price, ticketIdx) => {
              return <option key={ticketIdx} value={price.ticketPriceId}>
                {price.ticketType}
              </option>
            })}
          </select>
          , price: ${this.getTicketPrice(seat.ticketPriceId)}
        </div>) : [] }

      {this.state.redirect ? <Redirect push to={'/personalData'}/> : null}
      <div className={"buttons"}>
        <BackButton/>
        <button className={"nextButton"} disabled={this.props.chosenSeats.length === 0}
                onClick={this.handleOnClick}
                type="button">Next
        </button>
      </div>
    </div>;
  }

}

export default ChosenSeatsList;


