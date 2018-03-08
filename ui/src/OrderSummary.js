import React, {Component} from 'react';
import * as HttpService from "./HttpService";


class OrderSummary extends Component {
    constructor() {
        super();
        this.state = {};
    }

    handleClick = () => {
        return HttpService.post(`/payment/${this.props.match.params.reservationId}`);
    };

    render() {
        return (
            //TODO FUCKING REDIRECT DOESN'T WORK ;(
            <div>
                <button type="button" onClick={this.handleClick}>Pay Fucking money</button>
            </div>
        )
    }
}

export default OrderSummary;