package com.faleknatalia.cinemaBookingSystem;


import com.faleknatalia.cinemaBookingSystem.model.Seat;
import com.faleknatalia.cinemaBookingSystem.model.TicketPrice;
import com.faleknatalia.cinemaBookingSystem.ticket.SeatAndPriceDetails;
import com.faleknatalia.cinemaBookingSystem.ticket.TicketData;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//TODO OD NOWA TEST!!!!
public class PdfGeneratorTest {

    @Test
    public void generatePdf() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        //Before
        List<Seat> seats = new ArrayList<Seat>() {{
            add(new Seat(12,1,1));
            add(new Seat(13,2,2));
        }};
        List<SeatAndPriceDetails> seatAndPriceDetails = new ArrayList<>();
        seats.stream().map(seat ->
             seatAndPriceDetails.add(new SeatAndPriceDetails(seat,new TicketPrice("normal",10)))
        ).collect(Collectors.toList());

        LocalDateTime now = LocalDateTime.now();
        TicketData ticketData = new TicketData("The Prestige", now.format(formatter), now.format(formatterHour), 1l, seatAndPriceDetails);

        //
        generateTicket(ticketData);

    }


    private PDDocument generateTicket(TicketData ticketData) throws Exception {


        String dateOfProjection = ticketData.getProjectionDate();
        String hourOfProjection = ticketData.getProjectionHour();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        byte[] image = IOUtils.toByteArray(new ClassPathResource("/static/logo-palmy.jpg").getInputStream());
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, image, null);
        contentStream.drawImage(pdImage, 60, 621, 100, 100);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(200, 700);
        contentStream.setFont(PDType1Font.COURIER, 16);

        contentStream.showText("Movie: " + ticketData.getMovieTitle());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Date: " + dateOfProjection + "          ");
        contentStream.showText("Hour: " + hourOfProjection);
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Cinema Hall: " + ticketData.getCinemaHallId() + "  ");
        contentStream.showText("Price: $" + ticketData.getSeatAndPriceDetails());
        contentStream.endText();
        contentStream.close();

        document.save("./src/test/resources/static/ticketTest.pdf");
        document.close();

        return document;
    }


}
