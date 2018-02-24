import React, {Component} from 'react';
import './App.css';
import PersonalData from "./PersonalData";

//TODO stworzyc dwie nowe klasy jedna to czesc odnosnie movie, jedna to czesc seat

class App extends Component {

    constructor() {
        super();
        this.state = {
            whatsOn: [],
            chosenMovie: null,
            cinemaHall: null,
            chosenSeat: null
        };
    }

    printMovie = (movie) => {
        return `Movie: ${movie.movieTitle}, Data: ${movie.dateOfProjection}, duration: ${movie.movieDurationInMinutes} minutes`
    };

    printSeats = (cinemaHall) => {
        return `Seat: ${cinemaHall.seatNumber}, Free: ${cinemaHall.free ? "free" : "reserved"} , price: ${cinemaHall.ticketPrice}`
    }

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
        //TODO wydzielic metode httpService
        // htppService.fetchJson(`cinemaHall/seats/${chosenMovie.scheduledMovieId}`)
        //     .then(data => {
        //         console.log("Cinema hall seats: ", data);
        //         this.setState({cinemaHall: data})
        //     })
        fetch(`http://localhost:8080/cinemaHall/seats/${chosenMovie.scheduledMovieId}`)
            .then(results => {
                return results.json();
            })
            .then(data => {
                console.log("Cinema hall seats: ", data);
                this.setState({cinemaHall: data})
            })
    };

    getSeat = (chosenSeat) => {
        fetch(`http://localhost:8080/cinemaHall/seats/choose/${chosenSeat.seatId}`)
            .then(results => {
                return results.json();
            }).then(data => {
            console.log("ChosenSeat: ", data);
            this.setState({chosenSeat: data})
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
                <div>
                    {this.state.cinemaHall ? this.state.cinemaHall.seats.map
                    (a =>
                        <li onClick={(event => {
                            this.getSeat(a)
                            this.setState({chosenSeat: a})
                        })}> {this.printSeats(a)}
                        </li>
                    ) : null}
                </div>
                <div>
                    {this.state.chosenSeat ? this.printSeats(this.state.chosenSeat) : null}
                </div>
                <div>
                    <PersonalData/>
                </div>
            </div>
        );
    }
}

export default App;
