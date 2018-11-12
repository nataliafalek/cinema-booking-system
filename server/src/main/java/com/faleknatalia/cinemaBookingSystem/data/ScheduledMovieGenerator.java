package com.faleknatalia.cinemaBookingSystem.data;

import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ScheduledMovieGenerator {

    @Autowired
    private MovieRepository movieRepository;

    public Map<LocalDateTime, List<ScheduledMovie>> generateWeekWhatsOn(long cinemaHallId, LocalDateTime day) {
        List<LocalDateTime> allDates = Stream.iterate(day, date -> date.plusDays(1)).limit(7).collect(Collectors.toList());
        Map<LocalDateTime, List<ScheduledMovie>> mappedWeek = allDates.stream().collect(Collectors
                .toMap(dateTime -> dateTime, dateTime -> generateWhatsOn(cinemaHallId, dateTime)));
        return mappedWeek;
    }

    private List<ScheduledMovie> generateWhatsOn(long cinemaHallId, LocalDateTime day) {
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

}
