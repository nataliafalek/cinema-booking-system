import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Media} from 'react-bootstrap';

class MovieDetails extends Component {
  constructor() {
    super();
    this.days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    this.state = {
      chosenMovieId: [],
    };
  }

  today = () => {
    const date = new Date();
    return this.days[date.getDay()];
  };

  componentDidMount() {
    HttpService.fetchJson(`whatsOn/${this.props.match.params.chosenMovieId}`)
      .then(data => {
        this.setState({whatsOnByMovies: data})
      })
  };

  render() {
    return (
      this.state.whatsOnByMovies ?
        <div className={"container"}>
          <Media>
            <Media.Left>
              <img src={decodeURIComponent(this.state.whatsOnByMovies[0].movieImageUrl)} alt="thumbnail"/>
            </Media.Left>
            <Media.Body>
              <Media.Heading>"{this.state.whatsOnByMovies[0].movieTitle}"</Media.Heading>
              <p> Czas trwania: {this.state.whatsOnByMovies[0].movieDurationInMinutes} min </p>
              <p> Opis: {this.state.whatsOnByMovies[0].movieDescription}</p>
              <p>Dzisiejsze seanse: </p>
              <div>
                {this.state.whatsOnByMovies.map((movie, idx) =>
                  movie.dayOfProjection === this.today() ?
                    <li key={idx}>{movie.hourOfProjection}</li>
                    : null
                )}
              </div>
            </Media.Body>
          </Media>
        </div> : null
    );
  }
}

export default MovieDetails;