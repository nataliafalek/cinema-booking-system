import React, {Component} from 'react';
import './App.css';

class App extends Component {

    constructor() {
        super();
        this.state = {
            whatsOn: [],
            chosenMovie: null,
            cinemaHall: null,
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
                this.setState({whatsOn: data})
            })
    }

    getHall = (chosenMovie) => {
        fetch(`http://localhost:8080/cinemaHall/seats/${chosenMovie.scheduledMovieId}`)
            .then(results => {
                return results.json();
            })
            .then(data => {
                console.log("Cinema hall seats: ", data);
                this.setState({cinemaHall: data})
            })
    };

    render() {
        return (
            <div className="App">

                {this.state.whatsOn.map(a =>
                    <li onClick={(event) => {
                        this.getHall(a)
                        this.setState({chosenMovie: a})
                    }}> {this.printMovie(a)}</li>
                )}
                <br></br>
                <div>
                    {this.state.chosenMovie ? this.printMovie(this.state.chosenMovie) : null}
                </div>
                <br></br>
                <table>
                    <tr>
                        <th>SeatId</th>
                        <th>Status</th>
                        <th>Ticket Price</th>
                    </tr>
                    {this.state.cinemaHall ? this.state.cinemaHall.seats.map
                    (a =>
                        <tr>
                            <td> {a.seatId} </td>
                            <td> {a.free ? "free" : "reserved"} </td>
                            <td> {a.ticketPrice} </td>
                        </tr>
                    ) : null}
                </table>
            </div>
        );
    }
}

export default App;
