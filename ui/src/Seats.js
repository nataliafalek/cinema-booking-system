import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import './index.css';
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";

class Seats extends Component {

  constructor(props) {
    super(props);
    this.state = {
      chosenSeats: [],
      cinemaHall: [],
      ticketPrices: [],
      seatsWithChosenPrices: []
    };
  }

  printChosenSeats = (cinemaHall) => {
    return `Seat number: ${cinemaHall.seat.seatNumber}, row: ${cinemaHall.seat.rowNumber}`
  };

  chooseSeats = (seats) => {
    const chosenSeatIds = seats.map(seat => {
      return {seatId: seat.seat.seatId, ticketPriceId: seat.ticketPriceId}
    });
    return HttpService.postJson(`cinemaHall/seats/choose/${this.props.match.params.scheduledMovieId}`, chosenSeatIds)
      .then(data => {
        if (data.status === 200) {
          this.setState({redirect: true});
        }
      })
  };

  getHall = (scheduledMovieId) => {
    HttpService.fetchJson(`cinemaHall/seats?scheduledMovieId=${scheduledMovieId}`)
      .then(data => {
        this.setState({cinemaHall: data})
      })
  };

  renderSeat = (oneSeat, rowIndex, colIndex) => {
    const seat = oneSeat[0];
    const seatClass = this.state.chosenSeats.includes(seat) ? "chosenSeat" : "freeSeat";
    if (seat) {
      return seat.free ?
        <li className={seatClass} key={`${rowIndex}${colIndex}`} onClick={(event => {
          if (!this.state.chosenSeats.includes(seat)) {
            const newSeats = this.state.chosenSeats.concat(seat);
            this.setState({chosenSeats: newSeats})
          } else {
            const seats = this.state.chosenSeats.filter(s => s !== seat);
            this.setState({chosenSeats: seats})
          }

        })}>{seat.seat.seatNumber}</li> :
        <li className={"reservedSeat"}> {seat.seat.seatNumber} </li>
    }

  };

  findSeat = (hall, rowNumber, colNumber) => {
    //TODO ZAMIENIC NA ===
    const filteredList = Object.keys(hall).filter(key => key == rowNumber - 1);
    const row = filteredList ? hall[filteredList] : null;
    return row ? row.filter(element => element.seat.columnNumber == colNumber) : null;
  };


  componentDidMount() {
    this.getHall(this.props.match.params.scheduledMovieId)
    HttpService.fetchJson('ticketPriceList')
      .then(data => {
        this.setState({ticketPrices: data})
      })
  }

  handleOnClick = () => {
    this.chooseSeats(this.state.chosenSeats);
  };

  getTicketPrice = (priceId) => {
    const ticketPrice = this.state.ticketPrices.filter(ticketPrice =>
      // TODO Zamienic na ==
      ticketPrice.ticketPriceId == priceId);
    return ticketPrice[0].ticketValue
  }

  selectChange = (event, seat) => {
    const selectedPrice = event.target.value;
    const newChosenSeats = this.state.chosenSeats.map(chosenSeat => {
      {
        if (chosenSeat.seat.seatId === seat.seat.seatId) {
          chosenSeat.ticketPriceId = selectedPrice;
          return chosenSeat;
        }
        return chosenSeat;
      }
    });
    this.setState({seatsWithChosenPrices: newChosenSeats});
  }

  render() {
    const maxColumns = Math.max.apply(Math, this.state.cinemaHall.map(function (row) {
      return row.length;
    }));
    const maxRows = this.state.cinemaHall.length;
    const maxCinemaHallSeats = Array(maxRows).fill().map(() => Array.from(new Array(maxColumns), (value, index) => index + 1));

    return <div className={"seats"}>
      <div className={"printedSeats"}>
        <div className={"screen"}>Screen</div>
        {
          maxCinemaHallSeats.map((rowNumber, rowIndexMax) => {
            const row = rowNumber.map((columnNumber, colIndex) => {
              const seat = this.findSeat(this.state.cinemaHall, rowIndexMax + 1, columnNumber)
              return this.renderSeat(seat, rowIndexMax, colIndex) ? this.renderSeat(seat, rowIndexMax, colIndex) :
                <li key={`${rowIndexMax}${colIndex}`} className={"emptySeat"}>0</li>;
            });
            return (<div className={"CinemaHallRows"} key={`${rowIndexMax}`}>
              {row}
              <p>{rowIndexMax + 1}</p>
            </div>)
          })
        }

      </div>
      {this.state.chosenSeats ? this.state.chosenSeats.map((seat, idx) =>
        <div className={"chosenSeats"} key={idx}>
          {this.printChosenSeats(seat)}, Type:
          <select className={"ticketType"} key={idx} onChange={event => this.selectChange(event, seat)}>
            {this.state.ticketPrices.map((price, ticketIdx) => {
              return <option key={ticketIdx} value={price.ticketPriceId}>
                {price.ticketType}
              </option>
            })}
          </select>
          , price: ${this.getTicketPrice(seat.ticketPriceId)}
        </div>) : []}

      {this.state.redirect ? <Redirect push to={'/personalData'}/> : null}
      <div className={"buttons"}>
        <BackButton/>
        <button className={"nextButton"} disabled={this.state.chosenSeats.length === 0}
                onClick={this.handleOnClick}
                type="button">Next
        </button>
      </div>
    </div>;
  }

}

export default Seats;