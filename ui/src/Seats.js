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
            listOfChosenSeats: []
        };
    }


    printChosenSeats = (cinemaHall) => {
        return `Seat: ${cinemaHall.seatNumber}, Status: ${cinemaHall.free ? "free" : "reserved"} , price: $${cinemaHall.ticketPrice}`
    };


    printSeats = (cinemaHall) => {
        return `${cinemaHall.seatNumber}`
    };

    chooseSeats = (seats) => {
        const chosenSeatIdsList = seats.map(a => a.seatId)
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
            seatId: this.state.listOfChosenSeats.map(s => s.seatId)
        }
        return (

            <div className={"seats"}>
                <div className={"printedSeats"}>
                    <div className={"screen"}>Screen</div>
                    {this.state.cinemaHall ?
                        this.state.cinemaHall.map((rows, rowIndex) => {
                            //todo wydzielic do funkcji
                            const renderedRows = rows.map(seat => {
                                const seatClass = this.state.listOfChosenSeats.includes(seat) ? "chosenSeat" : "freeSeat";
                                return seat.free ?
                                    <li className={seatClass} key={rowIndex} onClick={(event => {
                                        if (!this.state.listOfChosenSeats.includes(seat)) {
                                            const newSeats = this.state.listOfChosenSeats.concat(seat)
                                            this.setState({listOfChosenSeats: newSeats})
                                        } else {
                                            const seats = this.state.listOfChosenSeats.filter(s => s !== seat)
                                            this.setState({listOfChosenSeats: seats})
                                        }

                                    })}> {this.printSeats(seat)}</li> :
                                    <li className={"reservedSeat"}> {this.printSeats(seat)} </li>

                                }
                            )

                            return (
                                <div className={"CinemaHallRows"}>
                                    {renderedRows}
                                    <p>{rowIndex + 1}</p>
                                </div>
                            )
                        }) : null}

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