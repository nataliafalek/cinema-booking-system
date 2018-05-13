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
    };
  }

  componentDidMount() {
    this.getSummary()
  }

  getSummary = () => {
    HttpService.fetchJson("reservationSummary")
      .then(data => {
        this.setState({personalData: data.personalData, ticketData: data.ticketData})
      })
  };

  handleClick = () => {
    return HttpService.post(`payment`)
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
            <em className={"reservationData"}>Name:</em>
            {this.state.personalData.name}</li>
          <li>
            <em className={"reservationData"}>Surname:</em>
            {this.state.personalData.surname}</li>
          <li>
            <em className={"reservationData"}>Email:</em>
            {this.state.personalData.email}</li>
          <li>
            <em className={"reservationData"}>Phone:</em>
            {this.state.personalData.phoneNumber}</li>
          <li>
            <em className={"reservationData"}>Movie:</em>
            "{this.state.ticketData.movieTitle}"
          </li>
          <li>
            <em className={"reservationData"}>Date of projection:</em>
            {this.state.ticketData.projectionDate}</li>
          <li>
            <em className={"reservationData"}>Hour of projection:</em>
            {this.state.ticketData.projectionHour}</li>
          <li>
            <em className={"reservationData"}>Seat Nr: &emsp; Row: &emsp;&emsp;Type:&emsp;&emsp;Price:</em>
            {this.state.ticketData.seatAndPriceDetails.map((seatAndPrice, idx) => {
                return (
                  <li key={idx} className={"reservationSeat"}>&emsp;{seatAndPrice.seat.seatNumber} &emsp;&emsp;&emsp;&emsp;&emsp; {seatAndPrice.seat.rowNumber}&emsp;&emsp;&emsp;&emsp;
                    {seatAndPrice.ticketPrice.ticketType}&emsp;&emsp;&emsp;  {seatAndPrice.ticketPrice.ticketValue}</li>)
              }
            )}</li>
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