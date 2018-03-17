package com.faleknatalia.reservationsystem;

import com.faleknatalia.cinemaBookingSystem.util.TicketData;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PdfGeneratorTest {

    @Test
    public void generatePdf() throws Exception {

        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));

        //Before
        List<Integer> seats = new ArrayList<Integer>() {{
            add(12);
        }};
        TicketData ticketData = new TicketData("The Prestige", LocalDateTime.now(), 1l, seats, 22);

        //
        generateTicket(ticketData);

    }

    @Test
    public void generateWhatsOn() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1).withHour(23).withMinute(0).withSecond(0).withNano(0);


        for (LocalDateTime date = startDate; date.isBefore(endDate); date = date.plusMinutes(45)) {
            System.out.println(date);
            //losuje nowy film
            //iteruje o jego czas trwania

        }

    }

    private PDDocument generateTicket(TicketData ticketData) throws Exception {


        //preparing data
        LocalDateTime dateOfProjection = ticketData.getProjectionDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

        //image

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        //Creating PDImageXObject object
        PDImageXObject pdImage = PDImageXObject.createFromFile("./src/test/resources/static/logo.png", document);
        //Drawing the image in the PDF document
        contentStream.drawImage(pdImage, 60, 621, 100, 100);

        contentStream.beginText();

        //Setting the leading
        contentStream.setLeading(14.5f);

        //Setting the position for the line
        contentStream.newLineAtOffset(200, 700);


        contentStream.setFont(PDType1Font.COURIER, 16);


        contentStream.showText("Movie: " + ticketData.getMovieTitle());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Date: " + dateOfProjection.format(formatter) + "          ");
        contentStream.showText("Hour: " + dateOfProjection.format(formatterHour));
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Cinema Hall: " + ticketData.getCinemaHallId() + "  ");
        contentStream.showText("Seat: " + ticketData.getSeatNumber() + "  ");
        contentStream.showText("Price: $" + ticketData.getTicketPrice());
        contentStream.endText();
        contentStream.close();

        document.save("./src/test/resources/static/ticketTest.pdf");
        document.close();

        return document;
    }


}
