import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";
import {Table} from 'react-bootstrap';


class ReservationSummary extends Component {
  constructor() {
    super();
    this.state = {
      personalData: [],
      ticketData: null,
      disableButton: false
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
    this.setState({disableButton: true});
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
            <em className={"reservationData"}>Imię: </em>
            {this.state.personalData.name}</li>
          <li>
            <em className={"reservationData"}>Nazwisko: </em>
            {this.state.personalData.surname}</li>
          <li>
            <em className={"reservationData"}>Email: </em>
            {this.state.personalData.email}</li>
          <li>
            <em className={"reservationData"}>Numer telefonu: </em>
            {this.state.personalData.phoneNumber}</li>
          <li>
            <em className={"reservationData"}>Tytuł filmu: </em>
            "{this.state.ticketData.movieTitle}"
          </li>
          <li>
            <em className={"reservationData"}>Data: </em>
            {this.state.ticketData.projectionDate}</li>
          <li>
            <em className={"reservationData"}>Godzina: </em>
            {this.state.ticketData.projectionHour}</li>
          <em className={"reservationData"}>
            <Table className={"summaryTable"} responsive>
              <thead>
              <tr>
                <th>Nr:</th>
                <th>Rząd:</th>
                <th>Typ:</th>
                <th>Cena:</th>
              </tr>
              </thead>
              <tbody>
              {this.state.ticketData.seatAndPriceDetails.map((seatAndPrice, idx) => {
                  return (
                    <tr key={idx} className={"reservationSeat"}>
                      <td>{seatAndPrice.seat.seatNumber}</td>
                      <td> {seatAndPrice.seat.rowNumber} </td>
                      <td>{seatAndPrice.ticketPrice.ticketType}</td>
                      <td>{seatAndPrice.ticketPrice.ticketValue} zł</td>
                    </tr>)
                }
              )}
              </tbody>
            </Table>
          </em>

        </div>
        {this.state.redirect ? <Redirect push to={`/payment/${this.props.match.params.reservationId}`}/> : null}
        <div className={"container"}>
          <div className={"buttons"}>
            <BackButton/>
            <button className={"payButton"} disabled={this.state.disableButton} onClick={this.handleClick}>Zapłać
            </button>
          </div>
        </div>
      </div>
    ) : null;
  }
}

export default ReservationSummary;