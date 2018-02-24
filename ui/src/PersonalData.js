import React, {Component} from 'react';
import './css/PersonalData.css'

class PersonalData extends Component {
    constructor() {
        super();
        this.state = this.defaultState()
    }

    defaultState = () => {
        return {
            name: '',
            surname: '',
            phoneNumber: '',
            email: ''
        }
    };


    //TODO inline do metody render
    // (event) => {
    //     this.setState({name: event.target.value});
    // };
    changeName = (event) => {
        this.setState({name: event.target.value});
    };

    changeSurname = (event) => {
        this.setState({surname: event.target.value});
    };

    changePhoneNumber= (event) => {
        this.setState({phoneNumber: event.target.value});
    };

    changeEmail = (event) => {
        this.setState({email: event.target.value});
    };


    //TODO wydzielic do metody postJson analogicznie jak fetchJson
    addPerson = (event) => {
        fetch('http://localhost:8080/cinemaHall/addPerson', {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.state)
        })
            .then(json => {
                console.log("dostalam z backendu", json);
                this.setState(this.defaultState())
            });
        console.log(this.state);
        event.preventDefault()
    };

    render() {
        return (
            <div>
                <form onSubmit={this.addPerson}>
                    <label htmlFor="name">Name</label>
                    <input
                        type="text"
                        value={this.state.name}
                        onChange={this.changeName}
                        required
                    />
                    <label htmlFor="surname">Surname</label>
                    <input
                        type="text"
                        value={this.state.surname}
                        onChange={this.changeSurname}
                        required
                    />
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        value={this.state.email}
                        onChange={this.changeEmail}
                        required
                    />
                    <label htmlFor="name">Telephone</label>
                    <input
                        type="tel"
                        value={this.state.phoneNumber}
                        onChange={this.changePhoneNumber}
                        required
                    />
                    <button>Send</button>
                </form>
            </div>
        )
    }



}

export default PersonalData;