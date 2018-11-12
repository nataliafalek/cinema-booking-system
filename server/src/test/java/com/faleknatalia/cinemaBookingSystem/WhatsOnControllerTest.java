package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.controllers.WhatsOnController;
import com.faleknatalia.cinemaBookingSystem.dto.ScheduledMovieDetailsDto;
import com.faleknatalia.cinemaBookingSystem.repository.MovieRepository;
import com.faleknatalia.cinemaBookingSystem.repository.ScheduledMovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class WhatsOnControllerTest {

    @Autowired
    WhatsOnController whatsOnController;

    @Autowired
    ScheduledMovieRepository scheduledMovieRepository;

    @Test
    public void movieDetailsTest() {
        ResponseEntity<List<ScheduledMovieDetailsDto>> listResponseEntity = whatsOnController.whatsOn();
        List<ScheduledMovieDetailsDto> scheduledMovieDetailsDtos = listResponseEntity.getBody();
        ScheduledMovieDetailsDto scheduledMovieDetailsDto = scheduledMovieDetailsDtos.get(0);
        long scheduledMovieId = scheduledMovieDetailsDto.getScheduledMovieId();
        ResponseEntity<ScheduledMovieDetailsDto> movieDetails = whatsOnController.getMovieDetails(scheduledMovieId);
        long scheduledMovieDetailsId = movieDetails.getBody().getScheduledMovieId();
        assertEquals(scheduledMovieDetailsId, scheduledMovieId);
    }

}
