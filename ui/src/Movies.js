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
        return `${movie.title}`
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
            <div className={"movies"}>
                <div className={"cinemaImage"}><h1>Nati Cinema</h1>
                    <br></br>
                    <h3>presents</h3>
                </div>
                <div className={"scheduleAndPrices"}>
                    <div className={"moviesSchedule"}>
                        <h2>#movies</h2>
                        <div>
                            {this.state.movies.map((a, idx) =>
                                <div key={idx} className={"responsive"}>
                                    <div className={"gallery"} onClick={(event) => {
                                        this.setState({chosenMovie: a});
                                        this.handleOnClick();
                                    }}>
                                        <img className={"movieImage"} src={a.imageUrl}/><br/>
                                        {this.printMovies(a)}

                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                    <div className={"prices"}>
                        <h2> #prices</h2>
                        <p>Student $10</p>
                        <p>Junior $10</p>
                        <p>Senior $10</p>
                    </div>
                </div>
                <div>
                    {this.state.redirect ?
                        <Redirect push
                                  to={`/movieDetails/${this.state.chosenMovie.movieId}/${encodeURIComponent(this.state.chosenMovie.imageUrl)}`}/> : null}
                </div>
            </div>

        )
    }
}

export default Movies;