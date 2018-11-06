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
        List<ScheduledMovieDetailsDto> scheduledMovieDetailDtos = joinMovieInfoWithScheduledDetailsList(scheduledMovies);
        return new ResponseEntity<>(scheduledMovieDetailDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/whatsOn/{chosenMovieId}", method = RequestMethod.GET)
    public ResponseEntity<List<ScheduledMovieDetailsDto>> whatsOnByMovieId(@PathVariable long chosenMovieId) {
        List<ScheduledMovie> scheduledMovies = scheduledMovieRepository.findAllByMovieId(chosenMovieId);
        List<ScheduledMovieDetailsDto> scheduledMovieDetailDtos = joinMovieInfoWithScheduledDetailsList(scheduledMovies);
        return new ResponseEntity<>(scheduledMovieDetailDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/movieInfo/{chosenMovieId}", method = RequestMethod.GET)
    public ResponseEntity<ScheduledMovieDetailsDto> getMovieDetails(@PathVariable long chosenMovieId) {
        ScheduledMovie scheduledMovie = scheduledMovieRepository.findOne(chosenMovieId);
        ScheduledMovieDetailsDto movie = joinMovieInfoWithScheduledDetails(scheduledMovie);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }


    private List<ScheduledMovieDetailsDto> joinMovieInfoWithScheduledDetailsList(List<ScheduledMovie> scheduledMovies) {
        List<ScheduledMovieDetailsDto> scheduledMovieDetailsDtos = scheduledMovies.stream().map(
                scheduledMovie -> joinMovieInfoWithScheduledDetails(scheduledMovie)).collect(Collectors.toList());
        return scheduledMovieDetailsDtos.stream().filter(scheduledMovieDetailsDto -> scheduledMovieDetailsDto.getDateOfProjection().isAfter(LocalDateTime.now())).collect(Collectors.toList());
    }

    private ScheduledMovieDetailsDto joinMovieInfoWithScheduledDetails(ScheduledMovie scheduledMovie) {
        Movie movie = movieRepository.findOne(scheduledMovie.getMovieId());
        ScheduledMovieDetailsDto scheduledMovieDetailsDto = new ScheduledMovieDetailsDto(
                movie.getTitle(),
                movie.getDurationInMinutes(),
                scheduledMovie.getDateOfProjection(),
                scheduledMovie.getScheduledMovieId(),
                scheduledMovie.getDateOfProjection().getDayOfWeek().name(),
                scheduledMovie.getDateOfProjection().format(formatterHour),
                movie.getDescription(),
                movie.getImageUrl()
        );
        return scheduledMovieDetailsDto;
    }
}
