package net.zav.jra.endpoints;

import net.zav.jra.AbstractClient;
import net.zav.jra.AbstractTest;
import net.zav.jra.exceptions.HttpException;
import net.zav.jra.mock.LinkMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ListingsTest extends AbstractTest {
    static AbstractClient client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(ListingsTest.class.getSimpleName());
        client.login(AbstractClient.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetTrendingSubreddits() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getTrendingSubreddits(client).query()).isNotNull();
    }

    @Test
    public void testGetBestLinks() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getBestLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"t3_kvzaot"})
    public void testGetLinksById(String name) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getLinksById(client, LinkMock::from, name).query()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"RedditDev, kvzaot"})
    public void testGetComments(String subreddit, String article) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getComments(client, LinkMock::from, subreddit, article).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetComments(String article) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getComments(client, LinkMock::from, article).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetControversialLinks(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getControversialLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetControversialLinks() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getControversialLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetDuplicates(String article) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getDuplicates(client, LinkMock::from, article).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetHotLinks(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getHotLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetHotLinks() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getHotLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetNewLinks(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getNewLinks(client, LinkMock::from, subreddit).setParameter("limit",1).query()).isNotNull();
    }

    @Test
    public void testGetNewLinks() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getNewLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetRandomSubmission(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getRandomSubmission(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetRandomSubmission() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getRandomSubmission(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetRisingLinks(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getRisingLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetRisingLinks() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getRisingLinks(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetTopLinks(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getTopLinks(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetTopLinks() throws InterruptedException, IOException, HttpException {
        assertThat(Listings.getTopLinks(client, LinkMock::from).query()).isNotNull();
    }
}
