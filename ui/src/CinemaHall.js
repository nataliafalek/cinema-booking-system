import * as HttpService from "./HttpService";
import React, {Component} from 'react';
import ChosenSeatsList from "./ChosenSeatsList";

class CinemaHall extends Component {
  constructor(props) {
    super(props);
    this.state = {
      chosenSeats: [],
      cinemaHall: []
    };
  }

  getHall = (scheduledMovieId) => {
    HttpService.fetchJson(`cinemaHall/seats?scheduledMovieId=${scheduledMovieId}`)
      .then(data => {
        this.setState({cinemaHall: data})
      })
  };

  renderSeat = (seat, rowIndex, colIndex) => {
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
    const filteredList = Object.keys(hall).filter(key => key == rowNumber - 1);
    const row = filteredList ? hall[filteredList] : null;
    const rowList = row ? row.filter(element => element.seat.columnNumber == colNumber) : null;
    return rowList[0];
  };


  componentDidMount() {
    this.getHall(this.props.match.params.scheduledMovieId)
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
              return seat ? this.renderSeat(seat, rowIndexMax, colIndex) :
                <li key={`${rowIndexMax}${colIndex}`} className={"emptySeat"}>0</li>;
            });
            return (<div className={"CinemaHallRows"} key={`${rowIndexMax}`}>
              {row}
              <p>{rowIndexMax + 1}</p>
            </div>)
          })
        }

      </div>
      <ChosenSeatsList chosenSeats = {this.state.chosenSeats} scheduledMovieId = {this.props.match.params.scheduledMovieId} />
    </div>

  }

}

export default CinemaHall;
