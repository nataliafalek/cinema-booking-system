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
      chosenSeat: [],
      listOfChosenSeats: [],
      cinemaHall: []
    };
  }


  printChosenSeats = (cinemaHall) => {
    return `Seat: ${cinemaHall.seatNumber}, Status: ${cinemaHall.free ? "free" : "reserved"} , price: $${cinemaHall.ticketPrice}`
  };


  // printSeats = (cinemaHall) => {
  //   return `${cinemaHall.seat.seatNumber}`
  // };

  chooseSeats = (seats) => {
    const chosenSeatIdsList = seats.map(a => a.seat.seatId)
    const chosenSeats = {
      seatId: chosenSeatIdsList
    };

    HttpService.postJson(`cinemaHall/seats/choose/${this.props.match.params.scheduledMovieId}`, chosenSeatIdsList)
      .then(data => {
        console.log("ChosenSeat: ", data);
        this.setState({chosenSeat: data})
      })
  };

  getHall = (scheduledMovieId) => {
    HttpService.fetchJson(`cinemaHall/seats?scheduledMovieId=${scheduledMovieId}`)
      .then(data => {
        console.log("Cinema hall seats: ", data);
        this.setState({cinemaHall: data})
      })
  };

  renderSeat = (oneSeat, rowIndex) => {
    const seat = oneSeat[0];
    const seatClass = this.state.listOfChosenSeats.includes(seat) ? "chosenSeat" : "freeSeat";
    // const emptySeat = seat.columnNumber === seat.seatNumber ? "notEmptySeat" : "emptySeat"
    if (seat) {
      return seat.free ?
        <li className={seatClass} key={rowIndex} onClick={(event => {
          if (!this.state.listOfChosenSeats.includes(seat)) {
            const newSeats = this.state.listOfChosenSeats.concat(seat)
            this.setState({listOfChosenSeats: newSeats})
          } else {
            const seats = this.state.listOfChosenSeats.filter(s => s !== seat)
            this.setState({listOfChosenSeats: seats})
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
  }

  handleOnClick = () => {
    this.setState({redirect: true});
    this.chooseSeats(this.state.listOfChosenSeats);
  };

  render() {
    const params = {
      scheduledMovieId: this.props.match.params.scheduledMovieId,
      seatId: this.state.listOfChosenSeats.map(s => s.seat.seatId)
    }
      console.log("seatId",params)

    const maxColumns = Math.max.apply(Math, this.state.cinemaHall.map(function (a) {
      return a.length;
    }));
    const maxRows = this.state.cinemaHall.length;
    const maxCinemaHallSeats = Array(maxRows).fill().map(() => Array.from(new Array(maxColumns), (val, index) => index + 1));
    console.log("the longest row", maxColumns, maxRows)
    console.log("max cinema hall seats", maxCinemaHallSeats)

    return (

      <div className={"seats"}>
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

        {this.state.redirect ? <Redirect push
                                         to={`/personalData?${queryString.stringify(params)}`}/> : null}

        <div className={"buttons"}>
          <BackButton/>
          <button className={"nextButton"} disabled={this.state.listOfChosenSeats.length === 0}
                  onClick={this.handleOnClick}
                  type="button">Next
          </button>
        </div>
      </div>
    );
  }

}

export default Seats;