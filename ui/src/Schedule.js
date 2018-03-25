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
        const d = new Date();
        return this.days[d.getDay()];
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
        return !_.isEmpty(group) ? (
            <div className={"schedule"}>
                <div className={"daysOfWeek"}>
                    {this.days.map(day => {
                            const chosenDay = this.state.actualDay === day ? "actualDay" : "otherDays";
                            return <span className={chosenDay}
                                         onClick={(event) => this.setState({actualDay: day})}>&emsp;{day}</span>
                        }
                    )}
                </div>
                <table className={"scheduledMovies"}>
                    <tr>
                        <th>TITLE</th>
                        <th>HOUR</th>
                        <th>DURATION</th>
                    </tr>

                    {
                        group[this.state.actualDay].map((a, idx) => {
                            const selectedMovie = this.state.chosenMovie === a ? "selectedMovie" : "otherMovies";
                            return <tr className={selectedMovie} key={idx} onClick={(event) => {
                                console.log("aa", a)
                                this.setState({chosenMovie: a})
                            }}>
                                <td> {a.movieTitle}</td>
                                <td> {a.hourOfProjection}</td>
                                <td> {a.movieDurationInMinutes} minutes</td>
                            </tr>
                        })}

                </table>


                {/*<div className={"chosenMovie"}>*/}
                {/*{this.state.chosenMovie ? this.printMovie(this.state.chosenMovie) : null}*/}
                {/*</div>*/}
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