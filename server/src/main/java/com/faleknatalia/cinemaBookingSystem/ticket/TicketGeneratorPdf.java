package com.faleknatalia.cinemaBookingSystem.ticket;

import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class TicketGeneratorPdf {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TicketGeneratorPdf.class);

    public ByteArrayOutputStream generateTicket(TicketData ticketData) throws Exception {
        List<SeatAndPriceDetails> chosenSeatAndPrices = ticketData.getSeatAndPriceDetails();
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        byte[] image = IOUtils.toByteArray(new ClassPathResource("/static/logo-nati.png").getInputStream());
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, image, null);
        contentStream.drawImage(pdImage, 160, 700, 200, 50);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(50, 670);
        contentStream.setFont(PDType1Font.COURIER, 16);
        contentStream.showText("Tytul: " + ticketData.getMovieTitle());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Data: " + ticketData.getProjectionDate() + "       ");
        contentStream.showText("Godzina: " + ticketData.getProjectionHour());
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText("Sala: " + ticketData.getCinemaHallId() + "  ");
        contentStream.newLine();
        chosenSeatAndPrices.stream().forEach(seatAndPriceDetails -> {
            try {
                contentStream.newLine();
                String ticketDetails = "Numer: " + Integer.toString(seatAndPriceDetails.getSeat().getSeatNumber())
                        + ", rzad: " + Integer.toString(seatAndPriceDetails.getSeat().getRowNumber()) + ", " + seatAndPriceDetails.getTicketPrice().getTicketType() +
                        " " + Integer.toString(seatAndPriceDetails.getTicketPrice().getTicketValue()) + " zl";
                contentStream.showText(ticketDetails);

            } catch (IOException e) {
                logger.info("Can't generate ticket", e);
            }
        });

        contentStream.endText();
        contentStream.close();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();
        return baos;
    }
}
