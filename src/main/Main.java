package main;

import main.scraper.Scraper;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Scraper scraper = new Scraper("https://books.toscrape.com/");
        try {
            scraper.startCrawl();
        } catch (IOException e) {
            System.out.println("Error starting the crawl: " + e.getMessage());
        }
    }
}
