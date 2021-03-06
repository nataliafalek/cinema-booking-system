import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import {Carousel, Table} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/css/bootstrap-theme.min.css';

class Movies extends Component {
  constructor() {
    super();
    this.state = {
      movies: [],
      chosenMovie: null,
      ticketPrices: []
    };
  }

  printMovies = (movie) => {
    return `${movie.title}`
  };

  componentDidMount() {
    HttpService.fetchJson('movies')
      .then(data => {
        this.setState({movies: data})
      });
    HttpService.fetchJson('ticketPriceList')
      .then(data => {
        this.setState({ticketPrices: data})
      })
  }

  handleOnClick = () => {
    this.setState({redirect: true});
  };

  render() {
    return (
      <div>
        <Carousel>
          {this.state.movies ? this.state.movies.map(movie => {
            return (
              <Carousel.Item>
                <img src={movie.carouselImageUrl} onClick={(event) => {
                  this.setState({chosenMovie: movie});
                  this.handleOnClick();
                }}/>
                <Carousel.Caption>
                  <h1>{movie.title}</h1>
                </Carousel.Caption>
              </Carousel.Item>
            );
          })
          : null}
        </Carousel>
        <h2>#filmy</h2>
        <div className="container">
          <div className={"moviesSchedule"}>
            {this.state.movies.map((movie, idx) =>
              <div key={idx} className={"responsive"}>
                <div className={"gallery"} onClick={(event) => {
                  this.setState({chosenMovie: movie});
                  this.handleOnClick();
                }}>
                  <img className={"movieImage"} src={movie.imageUrl} alt=""/><br/>
                  {this.printMovies(movie)}
                </div>
              </div>
            )}
          </div>
        </div>
        <div className={"container"}>
          <h2>#cennik</h2>
          <Table responsive>
            <tbody>
            {this.state.ticketPrices.map((price, idx) =>
              <tr key={idx}>
                <td>{price.ticketType}</td>
                <td> {price.ticketValue} zł</td>
              </tr>)}
            </tbody>
          </Table>
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