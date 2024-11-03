package main.parsers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultParser {
    public List<String> parseBookNames(String url) throws IOException {
        List<String> bookNames = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements bookElements = document.select(".product_pod h3 a");
        bookElements.forEach(element -> bookNames.add(element.attr("title")));
        return bookNames;
    }
}

