import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";
import _ from 'lodash';


class Schedule extends Component {

  constructor() {
    super();
    this.days = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
    this.state = {
      whatsOn: [],
      chosenMovie: null,
      groupedByDay: [],
      actualDay: this.today(),
    };
  }

  today = () => {
    const date = new Date();
    return this.days[date.getDay()];
  };

  handleOnClick = () => {
    this.setState({redirect: true});
  };

  componentDidMount() {
    HttpService.fetchJson('whatsOn')
      .then(data => {
        this.setState({whatsOn: data})
      })
  }


  render() {
    const grouppedByDay = _.groupBy(this.state.whatsOn, 'dayOfProjection');
    return !_.isEmpty(grouppedByDay) ? (
      <div className={"schedule"}>
        <div className={"daysOfWeek"}>
          {this.days.map((day, idx) => {
              const chosenDay = this.state.actualDay === day ? "actualDay" : "otherDays";
              return <span key={idx} className={chosenDay}
                           onClick={(event) => this.setState({actualDay: day})}>&emsp;{day}</span>
            }
          )}
        </div>
        <table className={"scheduledMovies"}>
          <tbody>
          <tr>
            <th>TITLE</th>
            <th>HOUR</th>
            <th>DURATION</th>
          </tr>
          {grouppedByDay[this.state.actualDay].map((movie, idx) => {
            const selectedMovie = this.state.chosenMovie === movie ? "selectedMovie" : "otherMovies";
            return <tr className={selectedMovie} key={idx} onClick={(event) => {
              this.setState({chosenMovie: movie})
            }}>
              <td> {movie.movieTitle}</td>
              <td> {movie.hourOfProjection}</td>
              <td> {movie.movieDurationInMinutes} minutes</td>
            </tr>
          })}
          </tbody>
        </table>
        <div>
        </div>
        {this.state.redirect ? <Redirect push to={`/seats/${this.state.chosenMovie.scheduledMovieId}`}/> : null}
        <div className={"buttons"}>
          <BackButton/>
          <button className={"nextButton"} disabled={!((this.state.chosenMovie || {}).scheduledMovieId)}
                  onClick={this.handleOnClick}
                  type="button">Next
          </button>
        </div>
      </div>
    ) : null;
  }


}

export default Schedule;