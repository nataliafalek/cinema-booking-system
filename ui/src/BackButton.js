import React, {Component} from 'react';

class BackButton extends Component {
  static contextTypes = {
    router: () => null,
  };

  render() {
    return (
      <button
        className="backButton"
        onClick={this.context.router.history.goBack}>
        Back
      </button>
    )
  }
}

export default BackButton;