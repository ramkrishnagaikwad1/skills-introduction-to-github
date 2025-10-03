package com.open.html.to.pdf.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
@Component
@SpringBootApplication
public class OpenhtmltopdfdemoApplication implements CommandLineRunner{
	
	public static void main(String[] args) {
		SpringApplication.run(OpenhtmltopdfdemoApplication.class, args);
	}
	@Override
    public void run(String... args) throws Exception {try {
        String imageUrl = "https://cdn.pixabay.com/photo/2025/09/23/19/27/nature-9851216_1280.jpg"; // <-- this works well

        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        try (InputStream in = connection.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
            String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(baos.toByteArray()));
            String html = "<html><body>"
                    + "<h1>Image from URL (embedded as base64)</h1>"
                    + "<img src=\"data:" + mimeType + ";base64," + base64Image + "\" "
                    + "width=\"400\" height=\"300\" />"
                    + "</body></html>";

            try (OutputStream os = new FileOutputStream("downloaded-url-image.pdf")) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withHtmlContent(html, null); // no baseUri needed
                builder.toStream(os);
                builder.run();
            }

            System.out.println("PDF created with image from URL (embedded): downloaded-url-image.pdf");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }}
}
