package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.model.*;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import com.faleknatalia.cinemaBookingSystem.data.CinemaHallGenerator;
import com.faleknatalia.cinemaBookingSystem.data.ScheduledMovieGenerator;
import com.faleknatalia.cinemaBookingSystem.data.SeatReservationByScheduledMovieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SpringBootApplication
public class CinemaBookingSystemApplication implements CommandLineRunner {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScheduledMovieRepository scheduledMovieRepository;

    @Autowired
    private CinemaHallRepository cinemaHallRepository;

    @Autowired
    private TicketPriceRepository ticketPriceRepository;

    @Autowired
    private ScheduledMovieGenerator scheduledMovieGenerator;

    @Autowired
    private SeatReservationByScheduledMovieGenerator seatReservationByScheduledMovieGenerator;

    @Autowired
    private SeatReservationByScheduledMovieRepository seatReservationByScheduledMovieRepository;

    @Value("${generateSampleData}")
    private boolean generateSampleData;

    public static void main(String[] args) {
        SpringApplication.run(CinemaBookingSystemApplication.class, args);
    }

    public void run(String... strings) throws Exception {

        if (generateSampleData) {

            List<Movie> allMovies =
                    Arrays.asList(
                            new Movie("The Prestige", "After a tragic accident two stage magicians engage in a battle to create the ultimate illusion whilst sacrificing everything they have to outwit the other.", 120, "http://1.fwcdn.pl/po/99/45/259945/7536864.6.jpg"),
                            new Movie("Catch me if you can", "A seasoned FBI agent pursues Frank Abagnale Jr. who, before this 19th birthday, successfully forged millions of dollars' worth of checks while posing as a Pan Am pilot, a doctor, and a legal prosecutor.", 125, "http://1.fwcdn.pl/po/23/62/32362/7519166.6.jpg"),
                            new Movie("12 Angry Men", "A jury holdout attempts to prevent a miscarriage of justice by forcing his colleagues to reconsider the evidence.", 95, "http://1.fwcdn.pl/po/07/01/30701/7492190.6.jpg"),
                            new Movie("The Silence of the Lambs", "A young F.B.I. cadet must receive the help of an incarcerated and manipulative cannibal killer to help catch another serial killer, a madman who skins his victims.", 120, "http://1.fwcdn.pl/po/10/47/1047/7714177.6.jpg"),
                            new Movie("Life of Brian", "Born on the original Christmas in the stable next door to Jesus, Brian of Nazareth spends his life being mistaken for a messiah.", 95, "http://1.fwcdn.pl/po/10/99/1099/7364767.6.jpg"),
                            new Movie("Coco", "Aspiring musician Miguel, confronted with his family's ancestral ban on music, enters the Land of the Dead to find his great-great-grandfather, a legendary singer.", 120, "http://1.fwcdn.pl/po/22/60/752260/7813486.6.jpg")
                    );

            List<CinemaHall> cinemaHalls = Arrays.asList(
                    new CinemaHallGenerator().generateCinemaHall(5, 9, 2),
                    new CinemaHallGenerator().generateCinemaHall(7, 11, 3)
            );

            List<TicketPrice> ticketPrices = Arrays.asList(
                    new TicketPrice("normal", 10),
                    new TicketPrice("student", 7),
                    new TicketPrice("senior", 8)
            );

            cinemaHallRepository.save(cinemaHalls);
            movieRepository.save(allMovies);

            Map<LocalDateTime, List<ScheduledMovie>> weekWhatsOnForFirstCinemaHall =
                    scheduledMovieGenerator.generateWeekWhatsOn(cinemaHalls.get(0).getCinemaHallId(), LocalDateTime.now().withMinute(0).withSecond(0).withNano(0));
            Map<LocalDateTime, List<ScheduledMovie>> weekWhatsOnForSecondCinemaHall =
                    scheduledMovieGenerator.generateWeekWhatsOn(cinemaHalls.get(1).getCinemaHallId(), LocalDateTime.now().withMinute(0).withSecond(0).withNano(0));

            weekWhatsOnForFirstCinemaHall.values().stream()
                    .forEach(scheduledMovies -> scheduledMovieRepository.save(scheduledMovies));
            weekWhatsOnForSecondCinemaHall.values().stream()
                    .forEach(scheduledMovies -> scheduledMovieRepository.save(scheduledMovies));
            ticketPriceRepository.save(ticketPrices);
            seatReservationByScheduledMovieRepository.save(seatReservationByScheduledMovieGenerator.generateSeatsReservationByScheduledMovies());
        }
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
