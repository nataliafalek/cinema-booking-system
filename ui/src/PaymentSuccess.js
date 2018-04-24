import React, {Component} from 'react';

class PaymentSuccess extends Component {
  constructor() {
    super();
    this.state = {}
  }

  render() {
    return <div className={"paymentSuccess"}>
      <h1>Thank you for making a reservation at Nati Cinema!
      </h1>
      <h3>We have sent a ticket to your e-mail.</h3>
      <p>PS This cinema does not really exist. Thanks to your money, I have a yacht and I eat pineapples all
        day.</p>
      <img src={require('./assets/meOnYacht.jpg')} alt=""/>
    </div>;
  }
}


export default PaymentSuccess;