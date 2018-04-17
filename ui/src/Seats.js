import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import './index.css';
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";
import queryString from 'query-string';

class Seats extends Component {

  constructor(props) {
    super(props);
    this.state = {
      chosenSeats: [],
      cinemaHall: [],
      ticketPrices: [],
      chosenPrice: []
    };
  }


  printChosenSeats = (cinemaHall) => {
    return `Seat number: ${cinemaHall.seat.seatNumber}, row: ${cinemaHall.seat.rowNumber}`

  };


  chooseSeats = (seats) => {
    const chosenSeatIds = seats.map(seat => {
      return {seatId: seat.seat.seatId, ticketPriceId: seat.ticketPriceId}
    })
    console.log("chosenSeatIds",chosenSeatIds)
    return HttpService.postJson(`cinemaHall/seats/choose/${this.props.match.params.scheduledMovieId}`, chosenSeatIds)
      .then(data => {
        if(data.status===200) {
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

  renderSeat = (oneSeat, rowIndex) => {
    const seat = oneSeat[0];
    const seatClass = this.state.chosenSeats.includes(seat) ? "chosenSeat" : "freeSeat";
    if (seat) {
      return seat.free ?
        <li className={seatClass} key={rowIndex} onClick={(event => {
          if (!this.state.chosenSeats.includes(seat)) {
            const newSeats = this.state.chosenSeats.concat(seat)
            this.setState({chosenSeats: newSeats})
          } else {
            const seats = this.state.chosenSeats.filter(s => s !== seat)
            this.setState({chosenSeats: seats})
          }

        })}>{seat.seat.seatNumber}</li> :
        <li className={"reservedSeat"}> {seat.seat.seatNumber} </li>
    }


  }

  findSeat = (hall, rowNumber, colNumber) => {
    const filteredList = Object.keys(hall).filter(key => key == rowNumber - 1);
    const row = filteredList ? hall[filteredList] : null;
    const col = row ? row.filter(element => element.seat.columnNumber == colNumber) : null
    return col;
  }


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
      ticketPrice.ticketPriceId == priceId)
    return ticketPrice[0].ticketValue
  }

  selectChange = (event, seat) => {
    const selectedPrice = event.target.value;
    const newChosenSeats = this.state.chosenSeats.map(s => {
      {
        if (s.seat.seatId === seat.seat.seatId) {
          s.ticketPriceId = selectedPrice;
          return s;
        }
        return s;
      }
    });
    this.setState({chosenPrice: newChosenSeats});
  }

  render() {
    //TODO skasowac i zmienic przy wysylaniu do backendu
    const params = {
      scheduledMovieId: this.props.match.params.scheduledMovieId,
      seatId: this.state.chosenSeats.map(seat => seat.seat.seatId)
    }

    const maxColumns = Math.max.apply(Math, this.state.cinemaHall.map(function (a) {
      return a.length;
    }));
    const maxRows = this.state.cinemaHall.length;
    const maxCinemaHallSeats = Array(maxRows).fill().map(() => Array.from(new Array(maxColumns), (val, index) => index + 1));

    return <div className={"seats"}>
      <div className={"printedSeats"}>
        <div className={"screen"}>Screen</div>

        {
          maxCinemaHallSeats.map((rowNumber, rowIndexMax) => {
            const row = rowNumber.map(columnNumber => {
              const seat = this.findSeat(this.state.cinemaHall, rowIndexMax + 1, columnNumber)
              return this.renderSeat(seat, rowIndexMax) ? this.renderSeat(seat, rowIndexMax) :
                <li className={"emptySeat"}>0</li>;

            });
            return (
              <div className={"CinemaHallRows"}>
                {row}
                <p>{rowIndexMax + 1}</p>
              </div>
            )
          })

        }

      </div>
      {this.state.chosenSeats ? this.state.chosenSeats.map((seat, idx) =>
        <div className={"chosenSeats"} key={idx}>
          {this.printChosenSeats(seat)}
          <select key={idx} onChange={event => this.selectChange(event, seat)}>
            {this.state.ticketPrices.map(price => {
              return <option value={price.ticketPriceId}>
                {price.ticketType}
              </option>
            })
            }
          </select>
          <p>price: ${this.getTicketPrice(seat.ticketPriceId)}</p>
        </div>) : []}


      {this.state.redirect ? <Redirect push
                                       to={`/personalData?${queryString.stringify(params)}`}/> : null}

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