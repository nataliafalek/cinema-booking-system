import React, {Component} from 'react';

class TransactionSummary extends Component {

    constructor() {
        super();
        this.state = {};
    }

    render() {
        return (
            <div>Summary: {this.props.match.params.reservationId}</div>
        );
    }

}

export default TransactionSummary;