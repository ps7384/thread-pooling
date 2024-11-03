package main.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserManager {
    private final DefaultParser defaultParser;

    public ParserManager() {
        this.defaultParser = new DefaultParser();
    }

    public List<String> parseBookNames(String url) throws IOException {
        return defaultParser.parseBookNames(url);
    }
    public List<String> discoverLinks(String url) throws IOException {
        List<String> links = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements linkElements = document.select("a[href]");
        for (Element link : linkElements) {
            String absUrl = link.absUrl("href");
            if (!absUrl.isEmpty() && absUrl.startsWith("https://books.toscrape.com")) {
                links.add(absUrl);
            }
        }
        return links;
    }

}


