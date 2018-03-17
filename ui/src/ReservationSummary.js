import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";


class ReservationSummary extends Component {

    constructor() {
        super();
        this.state = {
            personalData: [],
            ticketData: null,
            orderResponse: [],
        };
    }

    componentDidMount() {
        this.getSummary(this.props.match.params.reservationId)
    }

    getSummary = (reservationId) => {
        HttpService.fetchJson(`reservationSummary/${reservationId}`)
            .then(data => {
                console.log("Success - summary: ", data);
                this.setState({personalData: data.personalData, ticketData: data.ticketData})
            })
    };

    handleClick = () => {
        return HttpService.post(`/payment/${this.props.match.params.reservationId}`)
            .then(results => {
                return results.json();
            }).then(data => {
                window.location = data.redirectUri
            });

    };

    render() {
        return this.state.ticketData ? (
            <div>
                <div>
                    <h2>Summary</h2>
                    <div>Reservation id: {this.props.match.params.reservationId}</div>
                    <div>Name: {this.state.personalData.name}</div>
                    <div>Surname: {this.state.personalData.surname}</div>
                    <div>Email: {this.state.personalData.email}</div>
                    <div>Phone: {this.state.personalData.phoneNumber}</div>
                    <div>Movie: "{this.state.ticketData.movieTitle}"</div>
                    <div>Date of projection: {this.state.ticketData.projectionDate}</div>
                    <div>Seat Number: {this.state.ticketData.seatNumber.map(s => <li>{s} ticketPrice:
                        ${this.state.ticketData.ticketPrice}</li>)}</div>
                </div>

                {this.state.redirect ? <Redirect push to={`/payment/${this.props.match.params.reservationId}`}/> : null}

                <div>
                    <button type="button" onClick={this.handleClick}>Pay Fucking money</button>
                </div>
                <BackButton/>

            </div>
        ) : null;
    }


}

export default ReservationSummary;