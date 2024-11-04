# Thread Pooling Web Scraper

## Project Overview

This project demonstrates the use of **thread pooling** to efficiently scrape a website that contains information about books. It uses multiple threads to speed up the process of crawling web pages, parsing book names, and discovering new links to crawl. The target website in this example is [Books to Scrape](https://books.toscrape.com/).

The scraper is designed to:
- Crawl web pages concurrently using a thread pool.
- Parse book names from each page.
- Discover new links on each page and add them to the queue for further crawling.
- Respect `robots.txt` rules for ethical web scraping.

## Features

- **Thread Pooling**: Utilizes a fixed-size thread pool (`THREAD_POOL_SIZE = 10`) to manage concurrent tasks.
- **Robust Web Scraping**: Uses the [Jsoup](https://jsoup.org/) library for HTML parsing.
- **Link Management**: Ensures that each link is crawled only once using a `Set` of visited links.
- **Robots.txt Handling**: Respects website rules by checking `robots.txt` before crawling.
- **Result Saving**: Saves the scraped book names into a `result.txt` file.



## How It Works

1. **Initialization**: The `Scraper` class is initialized with a starting URL (`https://books.toscrape.com/`). It checks if crawling is allowed by reading the site's `robots.txt`.

2. **Thread Pool Setup**: A thread pool with 10 threads is created. Each thread processes URLs from a shared queue (`urlQueue`).

3. **Crawling Process**:
    - Each thread picks a URL from the queue and checks if it has already been visited.
    - The thread parses the page to extract book names and adds them to the `LinkManager`.
    - New links are discovered on the page and added to the queue for further crawling.

4. **Termination**: After all URLs are processed, the results (book names) are saved to `result.txt`.

## How to Run

### Prerequisites

- Java 8 or higher
- [Jsoup library](https://jsoup.org/download) 

### Steps

1. Clone this repository:
    ```
    git clone https://github.com/ps7384/thread-pooling.git
    ```

2. Navigate into the project directory:
    ```
    cd thread-pooling
    ```

3. Compile and run the project:
    ```
    javac -cp ".:libs/jsoup-1.18.1.jar" -d out src/main//*.java
    java -cp ".:out:libs/jsoup-1.18.1.jar" main.Main
    ```

4. The scraper will start crawling from [Books to Scrape](https://books.toscrape.com/) and save book titles in `result.txt`.

### Example Output

The output will be saved in `result.txt`. 
