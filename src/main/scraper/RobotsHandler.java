package main.scraper;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class RobotsHandler {
    public boolean isAllowed(String url) {
        try {
            URI uri = new URI(url);
            URI robotsUri = uri.resolve("/robots.txt");
            URL robotsUrl = robotsUri.toURL();
            
            HttpURLConnection connection = (HttpURLConnection) robotsUrl.openConnection();
            connection.setRequestMethod("GET");
            return connection.getResponseCode() != 403;
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error accessing robots.txt: " + e.getMessage());
            return false;
        }
    }
}


