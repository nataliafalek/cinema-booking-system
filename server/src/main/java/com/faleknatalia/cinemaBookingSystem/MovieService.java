package com.faleknatalia.cinemaBookingSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public LocalDateTime countWhenProjectionFinished(ScheduledMovie scheduledMovie) {
        Movie movie = movieRepository.findOne(scheduledMovie.getMovieId());
        return scheduledMovie.getDateOfProjection().plusMinutes(movie.getDurationInMinutes() + 10);
    }

}
