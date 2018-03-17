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


    printSeats = (cinemaHall) => {
        return `Seat: ${cinemaHall.seatNumber}, Status: ${cinemaHall.free ? "free" : "reserved"} , price: ${cinemaHall.ticketPrice}`
    };


    chooseSeats = (seats) => {
        const chosenSeatIdsList = seats.map(a => a.seatId)
        HttpService.fetchJson(`cinemaHall/seats/choose?${queryString.stringify({
            scheduledMovieId: this.props.match.params.scheduledMovieId,
            seatId: chosenSeatIdsList
        })}`)
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

            <div>

                {this.state.cinemaHall ? this.state.cinemaHall.map
                (a =>
                    <li onClick={(event => {
                        if (a.free && !this.state.listOfChosenSeats.includes(a)) {
                            const newSeats = this.state.listOfChosenSeats.concat(a)
                            this.setState({listOfChosenSeats: newSeats})

                        }

                    })}> {this.printSeats(a)}

                    </li>
                ) : null}

                <div>
                    {this.state.listOfChosenSeats ? this.state.listOfChosenSeats.map(a =>
                        <div>{this.printSeats(a)}</div>) : []}
                </div>
                {this.state.redirect ? <Redirect push
                                                 to={`/personalData?${queryString.stringify(params)}`}/> : null}
                <BackButton/>
                <button disabled={this.state.listOfChosenSeats.length === 0} onClick={this.handleOnClick}
                        type="button">Next
                </button>
            </div>
        );
    }

}

export default Seats;