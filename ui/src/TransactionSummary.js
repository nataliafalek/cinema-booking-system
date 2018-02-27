import React, {Component} from 'react';

class TransactionSummary extends Component {

    constructor() {
        super();
        this.state = {};
    }


    render() {
        return (
            <div>
                <div>Summary: {this.props.match.params.reservationId}</div>
                <a href={`http://localhost:8080/generatePdf/${this.props.match.params.reservationId}`} class="button">Download
                    ticket</a>
            </div>
        );
    }

}

export default TransactionSummary;