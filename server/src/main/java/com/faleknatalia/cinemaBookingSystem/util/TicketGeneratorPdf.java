package com.faleknatalia.cinemaBookingSystem.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class TicketGeneratorPdf {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TicketGeneratorPdf.class);


    public ByteArrayOutputStream generateTicket(TicketData ticketData) throws Exception {

        //preparing data
        LocalDateTime dateOfProjection = ticketData.getProjectionDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");

        List<Integer> seatNumbers = ticketData.getSeatNumber();


        //image
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        //Creating PDImageXObject object
        PDImageXObject pdImage = PDImageXObject.createFromFile(this.getClass().getResource("/static/logo.png").getPath(), document);
        //Drawing the image in the PDF document
        contentStream.drawImage(pdImage, 60, 618, 100, 100);

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
        contentStream.newLine();
        seatNumbers.stream().forEach(s -> {
            try {
                contentStream.newLine();
                String a = "Seat number: " + s.toString() + ", price: $" + ticketData.getTicketPrice().get(seatNumbers.indexOf(s)).toString();
                contentStream.showText(a);

            } catch (IOException e) {
                logger.info("Can't generate ticket", e);
            }
        });
//        contentStream.newLine();
//        contentStream.showText("Price: " + ticketData.getTicketPrice().stream().map(p -> "$" + p.toString())
//                .collect(Collectors.joining(", ")));
        contentStream.endText();
        contentStream.close();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();
        return baos;
    }

}
