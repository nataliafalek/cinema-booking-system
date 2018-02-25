import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';


class Movie extends Component {

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

    handleOnClick = () => {
        this.setState({redirect: true});
    }

    componentDidMount() {
        HttpService.fetchJson('whatsOn')
            .then(data => {
                console.log("Success", data);
                this.setState({whatsOn: data})
            })
    }


    render() {
        return (
            <div>
                {this.state.whatsOn.map(a =>
                    <li onClick={(event) => {
                        this.setState({chosenMovie: a})
                    }}> {this.printMovie(a)}</li>
                )}
                <div>
                    {this.state.chosenMovie ? this.printMovie(this.state.chosenMovie) : null}
                </div>
                <div>

                </div>
                {this.state.redirect ? <Redirect push to={`/seats/${this.state.chosenMovie.scheduledMovieId}`}/> : null}

                <button disabled={!((this.state.chosenMovie || {}).scheduledMovieId)} onClick={this.handleOnClick}
                        type="button">Next
                </button>
            </div>
        );
    }


}

export default Movie;