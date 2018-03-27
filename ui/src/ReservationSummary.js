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
            <div className={"summary"}>
                <div className={"summaryData"}>
                    <h2>Summary</h2>
                    <li>
                        <text className={"reservationData"}>Reservation id: </text>
                        {this.props.match.params.reservationId}</li>
                    <li>
                        <text className={"reservationData"}>Name: </text>
                        {this.state.personalData.name}</li>
                    <li>
                        <text className={"reservationData"}>Surname: </text>
                        {this.state.personalData.surname}</li>
                    <li>
                        <text className={"reservationData"}>Email: </text>
                        {this.state.personalData.email}</li>
                    <li>
                        <text className={"reservationData"}>Phone: </text>
                        {this.state.personalData.phoneNumber}</li>
                    <li>
                        <text className={"reservationData"}>Movie: </text>
                        "{this.state.ticketData.movieTitle}"
                    </li>
                    <li>
                        <text className={"reservationData"}>Date of projection: </text>
                        {this.state.ticketData.projectionDate}</li>
                    <li>
                        <text className={"reservationData"}>Hour of projection: </text>
                        {this.state.ticketData.projectionHour}</li>
                    <li>
                        <text className={"reservationData"}>Seat Number: &emsp;&emsp; Price: </text>
                        {this.state.ticketData.seatNumber.map
                        ((s,idx) => <li
                            className={"reservationSeat"}>&emsp;{s} &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;
                            ${this.state.ticketData.ticketPrice[idx]}</li>)}</li>
                </div>

                {this.state.redirect ? <Redirect push to={`/payment/${this.props.match.params.reservationId}`}/> : null}
                <div className={"buttons"}>

                    <BackButton/>
                    <button className={"payButton"} type="button" onClick={this.handleClick}>Pay</button>
                </div>

            </div>
        ) : null;
    }


}

export default ReservationSummary;