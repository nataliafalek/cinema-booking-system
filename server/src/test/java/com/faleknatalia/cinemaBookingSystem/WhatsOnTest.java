package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.controllers.WhatsOnController;
import com.faleknatalia.cinemaBookingSystem.dto.ScheduledMovieDetailsDto;
import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.SeatReservationByScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WhatsOnTest {

    @Autowired
    private WhatsOnController whatsOnController;

    @Autowired
    private MovieRepository movieRepository;


    @Test
    public void whatsOnTest() {
        //when
        ResponseEntity<List<ScheduledMovieDetailsDto>> listResponseEntity = whatsOnController.whatsOn();

        //then
        List<ScheduledMovieDetailsDto> listResponse = listResponseEntity.getBody();
        List<String> movieTitles = listResponse.stream().map(ScheduledMovieDetailsDto::getMovieTitle).collect(Collectors.toList());
        Assert.assertEquals(200, listResponseEntity.getStatusCode().value());
        //Assert.assertEquals(74, listResponse.size());
        Assert.assertThat(movieTitles, CoreMatchers.hasItem("Coco"));
        Assert.assertThat(movieTitles, CoreMatchers.hasItem("12 Angry Men"));

    }

    //TODO test na whatsOnByMovieId
    @Test
    public void whatsOnByMovieId() {
        //given
        Movie movie = movieRepository.findMovieByTitle("Coco");
        long chosenMovieId = movie.getMovieId();

        //when
        ResponseEntity<List<ScheduledMovieDetailsDto>> listResponseEntity = whatsOnController.whatsOnByMovieId(chosenMovieId);

        //then
        List<ScheduledMovieDetailsDto> listResponse = listResponseEntity.getBody();
        ScheduledMovieDetailsDto scheduledMovieDetailsDto = listResponse.get(1);
        ScheduledMovieDetailsDto anotherScheduledMovieDetailsDto = listResponse.get(3);
        String movieTitle = scheduledMovieDetailsDto.getMovieTitle();
        int movieDurationInMinutes = scheduledMovieDetailsDto.getMovieDurationInMinutes();
        String movieDescription = scheduledMovieDetailsDto.getMovieDescription();
        long scheduledMovieId = scheduledMovieDetailsDto.getScheduledMovieId();
        long anotherScheduledMovieId = anotherScheduledMovieDetailsDto.getScheduledMovieId();
        Assert.assertEquals(200, listResponseEntity.getStatusCode().value());
        Assert.assertEquals(movieTitle, "Coco");
        Assert.assertEquals(movieDurationInMinutes, 120);
        Assert.assertTrue(movieDescription.contains("Miguel"));

        Assert.assertFalse(scheduledMovieId == anotherScheduledMovieId);


    }

}
