import React, {Component} from 'react';

class BackButton extends Component {
    static contextTypes = {
        router: () => true,
    };

    render() {
        return (
            <button
                className="back-button"
                onClick={this.context.router.history.goBack}>
                Back
            </button>
        )
    }
}

export default BackButton;