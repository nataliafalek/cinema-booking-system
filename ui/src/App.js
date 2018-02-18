import React, { Component } from 'react';
import './App.css';

class App extends Component {

    constructor() {
        super();
        this.state = {
            whatsOn: [],
            chosenMovie: null,
        };
    }

    printMovie = (movie) => {
        return `Movie: ${movie.movieTitle}, Data: ${movie.dateOfProjection}, duration: ${movie.movieDurationInMinutes} minutes`
    };

    componentDidMount() {
        fetch('http://localhost:8080/whatsOn')
            .then(results => {
                return results.json();
            })
            .then(data => {
                console.log("Success", data);
                this.setState({whatsOn:data})
            })
    }

  render() {
    return (
      <div className="App">

          {this.state.whatsOn.map(a =>
              <li onClick={(event) => this.setState({chosenMovie: a})}> {this.printMovie(a)}</li>
          )}
            <div>
                {this.state.chosenMovie ? this.printMovie(this.state.chosenMovie) : null}
            </div>
      </div>
    );
  }
}

export default App;
