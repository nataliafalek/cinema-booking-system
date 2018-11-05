package com.faleknatalia.cinemaBookingSystem.controllers;

import com.faleknatalia.cinemaBookingSystem.dto.ScheduledMovieDetailsDto;
import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class WhatsOnController {
    private static final DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @RequestMapping(value = "/whatsOn", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetailsDto>> whatsOn() {
        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAllByOrderByDateOfProjection();
        List<ScheduledMovieDetailsDto> scheduledMovieDetailDtos = joinScheduledMovieWithMovieData(scheduledMovies);
        return new ResponseEntity<>(scheduledMovieDetailDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/whatsOn/{chosenMovieId}", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetailsDto>> whatsOnByMovieId(@PathVariable long chosenMovieId) {
        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAllByMovieId(chosenMovieId);
        List<ScheduledMovieDetailsDto> scheduledMovieDetailDtos = joinScheduledMovieWithMovieData(scheduledMovies);
        return new ResponseEntity<>(scheduledMovieDetailDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/movieInfo/{chosenMovieId}", method = RequestMethod.GET)
    public ResponseEntity<Movie> getMovieDetails(@PathVariable long chosenMovieId) {
        Long movieId = scheduledMovieRepository.findOne(chosenMovieId).getMovieId();
        return new ResponseEntity<>(movieRepository.findOne(movieId), HttpStatus.OK);
    }

    private List<ScheduledMovieDetailsDto> joinScheduledMovieWithMovieData(List<ScheduledMovie> scheduledMovies) {
        List<ScheduledMovieDetailsDto> scheduledMovieDetailsDtos = scheduledMovies.stream().map(
                scheduledMovie -> {
                    Movie movie = movieRepository.findOne(scheduledMovie.getMovieId());
                    return new ScheduledMovieDetailsDto(
                            movie.getTitle(),
                            movie.getDurationInMinutes(),
                            scheduledMovie.getDateOfProjection(),
                            scheduledMovie.getScheduledMovieId(),
                            scheduledMovie.getDateOfProjection().getDayOfWeek().name(),
                            scheduledMovie.getDateOfProjection().format(formatterHour),
                            movie.getDescription(),
                            movie.getImageUrl()
                    );
                }).collect(Collectors.toList());
        return scheduledMovieDetailsDtos.stream().filter(scheduledMovieDetailsDto -> scheduledMovieDetailsDto.getDateOfProjection().isAfter(LocalDateTime.now())).collect(Collectors.toList());
    }
}
