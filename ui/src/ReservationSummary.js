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
      showUploadButton: false
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
    this.setState({ showUploadButton: true });
    this.refs.btn.style.cursor = 'not-allowed';
    return HttpService.post(`payment`)
        .then(results => {
          return results.json();
        }).then(data => {
          window.location = data.redirectUri
        });
  };

  render() {
    return this.state.ticketData ? (
      <div className={"container"}>
        <h3>#podsumowanie transakcji</h3>
        <div className={"summaryData"}>
          <li>
            <em className={"reservationData"}>Imię:</em>
            {this.state.personalData.name}</li>
          <li>
            <em className={"reservationData"}>Nazwisko:</em>
            {this.state.personalData.surname}</li>
          <li>
            <em className={"reservationData"}>Email:</em>
            {this.state.personalData.email}</li>
          <li>
            <em className={"reservationData"}>Numer telefonu:</em>
            {this.state.personalData.phoneNumber}</li>
          <li>
            <em className={"reservationData"}>Tytuł filmu:</em>
            "{this.state.ticketData.movieTitle}"
          </li>
          <li>
            <em className={"reservationData"}>Data:</em>
            {this.state.ticketData.projectionDate}</li>
          <li>
            <em className={"reservationData"}>Godzina:</em>
            {this.state.ticketData.projectionHour}</li>
          <li>
            <em className={"reservationData"}>Nr: &emsp; Rząd: &emsp;Typ:&emsp;&emsp;Cena:</em>
            {this.state.ticketData.seatAndPriceDetails.map((seatAndPrice, idx) => {
                return (
                  <li key={idx}
                      className={"reservationSeat"}>{seatAndPrice.seat.seatNumber} &emsp;&emsp; {seatAndPrice.seat.rowNumber}&emsp;&emsp;&emsp;&emsp;
                    {seatAndPrice.ticketPrice.ticketType}&emsp;&emsp;  {seatAndPrice.ticketPrice.ticketValue}</li>)
              }
            )}</li>
        </div>

        {this.state.redirect ? <Redirect push to={`/payment/${this.props.match.params.reservationId}`}/> : null}
        <div className={"container"}>
          <div className={"buttons"}>
            <BackButton/>
            <button className={"payButton"} ref="btn" disabled={this.state.showUploadButton} onClick={this.handleClick}>Zapłać</button>
          </div>
        </div>
      </div>
    ) : null;
  }
}

export default ReservationSummary;