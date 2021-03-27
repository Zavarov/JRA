package net.zav.jra.endpoints;

import net.zav.jra.AbstractTest;
import net.zav.jra.mock.ClientMock;
import net.zav.jra.mock.LinkMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ListingsTest extends AbstractTest {
    ClientMock client;

    @BeforeEach
    public void setUp() {
        TARGET_PATH = JSON_PATH.resolve("Listings");
        client = new ClientMock();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetTrendingSubreddits() throws InterruptedException, IOException {
        client.json = readJson("TrendingSubreddits.json");
        assertThat(Listings.getTrendingSubreddits(client).query()).isNotNull();
    }

    @Test
    public void testGetBestLinks() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getBestLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"t3_kvzaot"})
    public void testGetLinksById(String name) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getLinksById(client, LinkMock::from, name).query()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"RedditDev, kvzaot"})
    public void testGetComments(String subreddit, String article) throws InterruptedException, IOException {
        client.json = readJson("Submission.json");
        assertThat(Listings.getComments(client, LinkMock::from, subreddit, article).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetComments(String article) throws InterruptedException, IOException {
        client.json = readJson("Submission.json");
        assertThat(Listings.getComments(client, LinkMock::from, article).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetControversialLinks(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getControversialLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetControversialLinks() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getControversialLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetDuplicates(String article) throws InterruptedException, IOException {
        client.json = readJson("Duplicates.json");
        assertThat(Listings.getDuplicates(client, LinkMock::from, article).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetHotLinks(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getHotLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetHotLinks() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getHotLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetNewLinks(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getNewLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetNewLinks() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getNewLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetRandomSubmission(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Submission.json");
        assertThat(Listings.getRandomSubmission(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetRandomSubmission() throws InterruptedException, IOException {
        client.json = readJson("Submission.json");
        assertThat(Listings.getRandomSubmission(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetRisingLinks(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getRisingLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetRisingLinks() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getRisingLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetTopLinks(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getTopLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetTopLinks() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Listings.getTopLinks(client, LinkMock::from).query()).isNotNull();
    }
}
