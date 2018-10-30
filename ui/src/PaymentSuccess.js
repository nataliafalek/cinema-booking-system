import React, {Component} from 'react';

class PaymentSuccess extends Component {
  constructor() {
    super();
    this.state = {}
  }

  render() {
    return <div className={"paymentSuccess"}>
      <h1>Dziękujemy za dokonanie rezerwacji w NatiCinema!
      </h1>
      <h3>Wysłaliśmy bilet na Twój adres e-mail.</h3>
    </div>
  }
}


export default PaymentSuccess;