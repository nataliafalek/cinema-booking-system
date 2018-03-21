import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";
import _ from 'lodash';


class Schedule extends Component {

    constructor() {
        super();
        this.state = {
            whatsOn: [],
            chosenMovie: null,
            groupedByDay: []
        };
    }


    printMovie = (movie) => {
        return `Movie: ${movie.movieTitle}, Date: ${movie.dateOfProjection}, duration: ${movie.movieDurationInMinutes} minutes`
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


        const group = _.groupBy(this.state.whatsOn, 'dayOfProjection');
        console.log("friday", group)
        return (
            <div className={"schedule"}>
                <div>
                    {
                        Object.keys(group).map((key, keyIdx) => {
                            return (
                                <div key={keyIdx}>
                                    <div className={"scheduledMoviesDay"}>{key}</div>
                                    <div className={"scheduledMovies"}>{
                                        group[key].map((a, idx) =>
                                            <li key={idx} className={"scheduledMovie"} onClick={(event) => {
                                                console.log("aa", a)
                                                this.setState({chosenMovie: a})
                                            }}> {a.movieTitle}, {a.hourOfProjection},
                                                duration: {a.movieDurationInMinutes} minutes</li>)
                                    }
                                    </div>
                                </div>
                            );
                        })

                    }
                </div>

                <div className={"chosenMovie"}>
                    {this.state.chosenMovie ? this.printMovie(this.state.chosenMovie) : null}
                </div>
                <div>

                </div>
                {this.state.redirect ? <Redirect push to={`/seats/${this.state.chosenMovie.scheduledMovieId}`}/> : null}
                <BackButton/>
                <button className={"nextButton"} disabled={!((this.state.chosenMovie || {}).scheduledMovieId)}
                        onClick={this.handleOnClick}
                        type="button">Next
                </button>

            </div>
        );
    }


}

export default Schedule;