import React, {Component} from 'react';
import * as HttpService from "./HttpService";

class MovieDetails extends Component {
    constructor() {
        super();
        this.state = {
            chosenMovieId: []
        };
    }

    componentDidMount() {
        HttpService.fetchJson(`whatsOn/${this.props.match.params.chosenMovieId}`)
            .then(data => {
                console.log("What's on by movies: ", data);
                this.setState({whatsOnByMovies: data})
            })
    };


    render() {
        return (

            <div>
                {this.state.whatsOnByMovies ? this.state.whatsOnByMovies.map((date, idx) =>
                    <li key={idx}>{date.dateOfProjection}</li>) : null}
            </div>

        );
    }
}


export default MovieDetails;