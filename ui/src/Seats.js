import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import './index.css';
import {Redirect} from 'react-router-dom';


class Seats extends Component {

    constructor() {
        super();
        this.state = {
            chosenSeat: null,
        };
    }


    printSeats = (cinemaHall) => {
        return `Seat: ${cinemaHall.seatNumber}, Status: ${cinemaHall.free ? "free" : "reserved"} , price: ${cinemaHall.ticketPrice}`
    };


    getSeat = (chosenSeat) => {
        HttpService.fetchJson(`cinemaHall/seats/choose/${this.props.match.params.scheduledMovieId}/${chosenSeat.seatId}`)
            .then(data => {
                console.log("ChosenSeat: ", data);
                this.setState({chosenSeat: data})
            })
    };

    getHall = (scheduledMovieId) => {
        HttpService.fetchJson(`cinemaHall/seats/${scheduledMovieId}`)
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
        this.getSeat(this.state.chosenSeat);
    };

    render() {
        return (

            <div>

                {this.state.cinemaHall ? this.state.cinemaHall.map
                (a =>
                    <li onClick={(event => {
                        if (a.free) {
                            // this.getSeat(a);
                            this.setState({chosenSeat: a})
                        }
                    })}> {this.printSeats(a)}
                    </li>
                ) : null}

                <div>
                    {this.state.chosenSeat ? this.printSeats(this.state.chosenSeat) : null}
                </div>
                {this.state.redirect ? <Redirect push
                                                 to={`/personalData/${this.props.match.params.scheduledMovieId}/${this.state.chosenSeat.seatId}`}/> : null}

                <button disabled={!((this.state.chosenSeat || {})).seatId} onClick={this.handleOnClick}
                        type="button">Next
                </button>
            </div>
        );
    }

}

export default Seats;