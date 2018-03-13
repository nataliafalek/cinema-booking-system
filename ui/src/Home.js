import React, {Component} from 'react';
import * as HttpService from "./HttpService";

class Home extends Component {
    constructor() {
        super();
        this.state = {
            movies: [],
            chosenMovie: null,
            whatsOnByMovies: []
        };
    }

    printMovies = (movie) => {
        return `${movie.title}, Description: ${movie.description}, duration: ${movie.durationInMinutes} minutes`
    };

    getMovie = (chosenMovieId) => {
        HttpService.fetchJson(`whatsOn/${chosenMovieId.movieId}`)
            .then(data => {
                console.log("What's on by movies: ", data);
                this.setState({whatsOnByMovies: data})
            })
    };

    componentDidMount() {
        HttpService.fetchJson('movies')
            .then(data => {
                console.log("Success - list of movies: ", data);
                this.setState({movies: data})
            })
    }

    render() {
        return (
            <div>
                <h2>To moje super kino</h2>
                <div>
                    {this.state.movies.map(a =>
                        <li onClick={(event) => {
                            this.setState({chosenMovie: a});
                            this.getMovie(a)
                        }}> {this.printMovies(a)}
                        </li>
                    )}
                </div>
                <div>
                    {this.state.whatsOnByMovies ? this.state.whatsOnByMovies.map(date =>
                        <li>{date.dateOfProjection}</li>) : null}
                </div>
            </div>

        )
    }
}

export default Home;