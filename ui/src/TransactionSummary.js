import React, {Component} from 'react';
import * as HttpService from "./HttpService";

class TransactionSummary extends Component {

    constructor() {
        super();
        this.state = {};
    }

    // getTicket = (reservationId) => {
    //     HttpService.fetchJson(`/generatePdf/${reservationId}`)
    //         .then(data => {
    //             console.log("ChosenSeat: ", data);
    //            // this.setState({chosenSeat: data})
    //         })
    // }

    render() {
        return (
            <div>
            <div>Summary: {this.props.match.params.reservationId}</div>
                <a href={`http://localhost:8080/generatePdf/${this.props.match.params.reservationId}`} class="button">Download ticket</a>
                {/*<button onClick={this.getTicket(this.props.match.params.reservationId)}>Generate pdf</button>*/}
            </div>
        );
    }

}

export default TransactionSummary;