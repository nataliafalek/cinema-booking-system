import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';


class Movies extends Component {
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

    componentDidMount() {
        HttpService.fetchJson('movies')
            .then(data => {
                console.log("Success - list of movies: ", data);
                this.setState({movies: data})
            })
    }

    handleOnClick = () => {
        this.setState({redirect: true});
    }

    render() {
        return (
            <div>
                <h2>To moje super kino</h2>
                <div>
                    {this.state.movies.map((a, idx) =>
                        <div key={idx} className={"responsive"}>
                            <div className={"gallery"} onClick={(event) => {
                                this.setState({chosenMovie: a});
                                this.handleOnClick();
                            }}>
                                <img className={"movieImage"} src={a.imageUrl}/>
                                {this.printMovies(a)}

                            </div>
                        </div>
                    )}
                </div>

                <div>
                    {this.state.redirect ?
                        <Redirect push to={`/movieDetails/${this.state.chosenMovie.movieId}`}/> : null}
                </div>
            </div>

        )
    }
}

export default Movies;