package main.scraper;


import main.database.LinkManager;
import main.parsers.ParserManager;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Scraper {
    private final String startUrl;
    private final int THREAD_POOL_SIZE = 10;
    private final ExecutorService threadPool;
    private final RobotsHandler robotsHandler;
    private final LinkManager linkManager;
    private final ParserManager parserManager;
    private final Set<String> visitedLinks;
    private final BlockingQueue<String> urlQueue;

    public Scraper(String startUrl) {
        this.startUrl = startUrl;
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.robotsHandler = new RobotsHandler();
        this.linkManager = new LinkManager();
        this.parserManager = new ParserManager();
        this.visitedLinks = ConcurrentHashMap.newKeySet();
        this.urlQueue = new LinkedBlockingQueue<>();
    }

    public void startCrawl() throws IOException {
        if (!robotsHandler.isAllowed(startUrl)) {
            System.out.println("Crawling not allowed by robots.txt");
            return;
        }

        // Initialize the queue with the start URL
        urlQueue.add(startUrl);

        // Submit worker tasks to the thread pool
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            threadPool.submit(this::processQueue);
        }

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(10, TimeUnit.MINUTES)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        saveResults();
        System.out.println("Finished all threads");
    }

    private void processQueue() {
        while (!urlQueue.isEmpty() || !threadPool.isShutdown()) {
            try {
                String url = urlQueue.poll(1, TimeUnit.SECONDS);  // Wait for a URL from the queue
                if (url == null || visitedLinks.contains(url)) continue;  // Skip if the URL is null or already visited

                System.out.println(Thread.currentThread().getName() + " Start processing " + url);
                visitedLinks.add(url);  // Mark URL as visited

                // Parse book names on the page
                List<String> bookNames = parserManager.parseBookNames(url);
                linkManager.addBookNames(bookNames);

                // Discover new links to crawl
                List<String> newLinks = parserManager.discoverLinks(url);
                for (String link : newLinks) {
                    if (!visitedLinks.contains(link)) {
                        urlQueue.add(link);  // Add new link to the queue
                    }
                }

                System.out.println("Crawled: " + url);
                System.out.println(Thread.currentThread().getName() + " End processing " + url);

            } catch (Exception e) {
                System.out.println("Error crawling page: " + e.getMessage());
            }
        }
    }

    private void saveResults() throws IOException {
        try (FileWriter writer = new FileWriter("result.txt")) {
            for (String bookName : linkManager.getBookNames()) {
                writer.write(bookName + "\n");
            }
        }
    }
}
