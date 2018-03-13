import React, {Component} from 'react';
import './index.css'
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';


class PersonalData extends Component {
    constructor() {
        super();
        this.state = {
            name: '',
            surname: '',
            phoneNumber: '',
            email: '',
            chosenMovie: '',
            chosenSeatId: '',
            reservationId: '',
        }
    }


    addPerson = (event) => {
        const person = {
            name: this.state.name,
            surname: this.state.surname,
            phoneNumber: this.state.phoneNumber,
            email: this.state.email,
            chosenMovie: this.props.match.params.scheduledMovieId,
            chosenSeatId: this.props.match.params.seatId,
        };
        HttpService.postJson('cinemaHall/addPerson', person)
            .then(results => {
                return results.text();
            }).then(reservationId => {
            this.setState({reservationId: reservationId});
            console.log("reservation id", this.state.reservationId)
            this.handleOnClick()
        });
        console.log(this.state);
        event.preventDefault()
    };

    handleOnClick = () => {
        this.setState({redirect: true});
    };

    render() {
        return (
            <div>
                <form onSubmit={this.addPerson}>
                    <label htmlFor="name">Name</label>
                    <input
                        type="text"
                        value={this.state.name}
                        onChange={(event) => {
                            this.setState({name: event.target.value});
                        }}
                        required
                    />
                    <label htmlFor="surname">Surname</label>
                    <input
                        type="text"
                        value={this.state.surname}
                        onChange={(event) => {
                            this.setState({surname: event.target.value});
                        }}
                        required
                    />
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        value={this.state.email}
                        onChange={(event) => {
                            this.setState({email: event.target.value});
                        }}
                        required
                    />
                    <label htmlFor="name">Telephone</label>
                    <input
                        type="tel"
                        value={this.state.phoneNumber}
                        onChange={(event) => {
                            this.setState({phoneNumber: event.target.value});
                        }}
                        required
                    />
                    {this.state.redirect ? <Redirect push
                                                     to={`/reservationSummary/${this.state.reservationId}`}/> : null}
                    <button type="submit">Show summary</button>
                </form>
            </div>
        )
    }


}

export default PersonalData;