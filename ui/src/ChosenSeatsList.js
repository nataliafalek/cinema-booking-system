import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import './index.css';

class ChosenSeatsList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      ticketPrices: [],
      chosenPriceList: []
    };
  }

  componentDidMount() {
    HttpService.fetchJson('ticketPriceList')
      .then(data => {
        this.setState({ticketPrices: data})
      });
    HttpService.fetchJson('session')
      .then(data => {
        if (data.chosenPriceList && data.status !== 500) {
          this.setState({chosenPriceList: data.chosenPriceList})
        }
      });
  }

  getTicketPrice = (priceId) => {
    const ticketPrice = this.state.ticketPrices.filter(ticketPrice => ticketPrice.ticketPriceId === priceId);
    return ticketPrice.length !== 0 ? ticketPrice[0].ticketValue : [];
  };

  calculateNewChosenSeats = (event, seat, selectedPrice) => {
    const newChosenSeats = this.props.chosenSeats.map(chosenSeat => {
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
    return newChosenSeats
  };

  render() {
    return <div>
      {this.props.chosenSeats ? this.props.chosenSeats.map((seat, idx) =>
        <div className={"chosenSeats"} key={idx}>
          Numer siedzenia: {seat.seat.seatNumber}, rząd: {seat.seat.rowNumber}, Typ:
          <select className={"ticketType"} key={idx} onChange={event => {
            const newChosenSeats = this.calculateNewChosenSeats(event, seat, event.target.value)
            this.props.chosenSeatsChanged(newChosenSeats)
          }}> {this.state.ticketPrices.map((price, ticketIdx) => {
            return <option key={ticketIdx} value={price.ticketPriceId}>
              {price.ticketType}
            </option>
          })}
          </select>
          , cena: {this.getTicketPrice(seat.ticketPriceId)}zł
        </div>) : []}
    </div>;
  }
}

export default ChosenSeatsList;


