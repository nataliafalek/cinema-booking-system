package com.faleknatalia.cinemaBookingSystem.data;

import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.CinemaHallRepository;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ScheduledMovieGenerator {

    @Autowired
    CinemaHallRepository cinemaHallRepository;

    @Autowired
    MovieRepository movieRepository;


    public List<ScheduledMovie> generateWhatsOn(long cinemaHallId, LocalDateTime day) {
        LocalDateTime lastMovieEnd = day.withHour(12);
        List<ScheduledMovie> scheduledMovies = new ArrayList<>();
        List<Movie> movies = movieRepository.findAll();

        for (int i = 0; i < 6; i++) {
            int randomNumber = ThreadLocalRandom.current().nextInt(0, movies.size());
            Movie movie = movies.get(randomNumber);
            LocalDateTime endOfProjection = lastMovieEnd.plusMinutes(movie.getDurationInMinutes() + 15);
            scheduledMovies.add(new ScheduledMovie(lastMovieEnd, cinemaHallId, movie.getMovieId()));
            lastMovieEnd = endOfProjection;

        }

        return scheduledMovies;
    }

    public Map<LocalDateTime, List<ScheduledMovie>> generateWeekWhatsOn(long cinemaHallId, LocalDateTime day) {
        LocalDateTime stop = day.plusWeeks(1);

        List<LocalDateTime> allDates = new ArrayList<>();
        LocalDateTime ld = day;
        while (ld.isBefore(stop)) {
            allDates.add(ld);
            ld = ld.plusDays(1);
        }

        Map<LocalDateTime, List<ScheduledMovie>> mappedWeek = new HashMap<>();
        for (LocalDateTime dateTime : allDates) {
            mappedWeek.put(dateTime, generateWhatsOn(cinemaHallId, dateTime));
        }
        return mappedWeek;
    }

}
