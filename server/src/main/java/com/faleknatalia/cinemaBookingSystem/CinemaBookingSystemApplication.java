package com.faleknatalia.cinemaBookingSystem;

import com.faleknatalia.cinemaBookingSystem.data.CinemaHallGenerator;
import com.faleknatalia.cinemaBookingSystem.data.ScheduledMovieGenerator;
import com.faleknatalia.cinemaBookingSystem.data.SeatReservationByScheduledMovieGenerator;
import com.faleknatalia.cinemaBookingSystem.model.CinemaHall;
import com.faleknatalia.cinemaBookingSystem.model.Movie;
import com.faleknatalia.cinemaBookingSystem.model.ScheduledMovie;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
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
                            new Movie(
                                    "Kler",
                                    "Życie trzech księży ulega zmianie, kiedy ich drogi krzyżują się ponownie.",
                                    135,
                                    "https://1.fwcdn.pl/po/04/02/810402/7856555.6.jpg",
                                    uploadImageToDB(new ClassPathResource("/static/rsz_kler.png"))),
                            new Movie("Zimna Wojna",
                                    "Historia wielkiej i trudnej miłości dwojga ludzi, którzy nie potrafią być ze sobą i jednocześnie nie mogą bez siebie żyć. W tle wydarzenia zimnej wojny lat 50. w Polsce, Berlinie, Jugosławii i Paryżu.",
                                    85,
                                    "https://1.fwcdn.pl/po/40/39/764039/7845442.6.jpg",
                                    uploadImageToDB(new ClassPathResource("/static/rsz_1zimna-wojna.jpg"))),
                            new Movie("Bohemian Rhapsody",
                                    "Dzięki oryginalnemu brzmieniu Queen staje się jednym z najpopularniejszych zespołów w historii muzyki.",
                                    135,
                                    "https://1.fwcdn.pl/po/92/01/619201/7863181.6.jpg",
                                    uploadImageToDB(new ClassPathResource("/static/bohemian.jpg"))),
                            new Movie("Ocean ognia",
                                    "Kapitan okrętu podwodnego współpracuje z drużyną Navy SEAL w celu uratowania prezydenta Rosji, który podczas zamachu wzięty został do niewoli.",
                                    125,
                                    "https://1.fwcdn.pl/po/83/54/618354/7860787.6.jpg",
                                    uploadImageToDB(new ClassPathResource("/static/rsz_hunterkiller.jpg")))
                    );

            List<CinemaHall> cinemaHalls = Arrays.asList(
                    new CinemaHallGenerator().generateCinemaHall(8, 10),
                    new CinemaHallGenerator().generateCinemaHall(10, 12)
            );

            List<TicketPrice> ticketPrices = Arrays.asList(
                    new TicketPrice("normalny", 25),
                    new TicketPrice("ulgowy", 18),
                    new TicketPrice("studencki", 17)
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

    private byte[] uploadImageToDB(ClassPathResource classPathResource) throws IOException {
        byte[] arrayData = new byte[(int) classPathResource.contentLength()];
        classPathResource.getInputStream().read(arrayData);
        return arrayData;
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
