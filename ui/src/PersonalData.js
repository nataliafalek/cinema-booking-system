import React, {Component} from 'react';
import './index.css'
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";
import queryString from 'query-string';


class PersonalData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            surname: '',
            phoneNumber: '',
            email: '',
            chosenMovie: '',
            reservationId: '',
        }
    }


    addPerson = (event) => {
        const parsedQueryParams = queryString.parse(this.props.location.search)
        const person = {
            name: this.state.name,
            surname: this.state.surname,
            phoneNumber: this.state.phoneNumber,
            email: this.state.email,
            chosenMovie: parsedQueryParams.scheduledMovieId,
            chosenSeatId: Array.isArray(parsedQueryParams.seatId) ? parsedQueryParams.seatId : [parsedQueryParams.seatId],
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
                    <button type="submit">summary</button>
                    <BackButton/>
                </form>
            </div>
        )
    }


}

export default PersonalData;