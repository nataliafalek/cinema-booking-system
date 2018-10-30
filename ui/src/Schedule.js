import React, {Component} from 'react';
import * as HttpService from "./HttpService";
import {Redirect} from 'react-router-dom';
import BackButton from "./BackButton";
import _ from 'lodash';
import {Table} from 'react-bootstrap';


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
      });

    HttpService.fetchJson('session')
      .then(data => {
        if (data && data.status !== 500) {
          this.setState({chosenMovie: data.chosenMovieId})
          this.setState({actualDay: data.dayOfProjection})
        }
      })
  }

  orderByActualDay = () => {
    const days = _.cloneDeep(this.days)
    const daysOrder = days.splice(0, days.indexOf(days[new Date().getDay()]));
    return days.concat(daysOrder);
  };

  render() {
    const grouppedByDay = _.groupBy(this.state.whatsOn, 'dayOfProjection');
    return !_.isEmpty(grouppedByDay) ? (
      <div className={"schedule"}>
        <div className={"container"}>
          <div className={"daysOfWeek"}>
            {this.orderByActualDay().map((day, idx) => {
              const dayClass = this.state.actualDay === day ? "actualDay" : "otherDays";
              return <span key={idx} className={dayClass}
                           onClick={(event) => this.setState({actualDay: day})}>&emsp;{day}</span>
            })}
          </div>
          <Table responsive>
            <tbody>
            <tr>
              <th>TYTUŁ</th>
              <th>GODZINA</th>
              <th>CZAS TRWANIA</th>
            </tr>
            {grouppedByDay[this.state.actualDay] ? grouppedByDay[this.state.actualDay].map((movie, idx) => {
              const movieClass = this.state.chosenMovie === movie.scheduledMovieId ? "selectedMovie" : "otherMovies";
              return <tr className={movieClass}
                         key={idx} onClick={(event) => {
                this.setState({chosenMovie: movie.scheduledMovieId})
              }}>
                <td> {movie.movieTitle}</td>
                <td> {movie.hourOfProjection}</td>
                <td> {movie.movieDurationInMinutes} min</td>
              </tr>
            }) : null}
            </tbody>
          </Table>
        </div>
        {this.state.redirect ? <Redirect push to={`/seats/${this.state.chosenMovie}`}/> : null}
        <div className={"container"}>
          <div className={"buttons"}>
            <BackButton/>
            <button className={"nextButton"} disabled={!((this.state.chosenMovie || {}))}
                    onClick={this.handleOnClick}
                    type="button">Dalej
            </button>
          </div>
        </div>
      </div>
    ) : null;
  }

}

export default Schedule;