package vartas.jra;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.exceptions.HttpException;
import vartas.jra.query.Query;
import vartas.jra.query.QuerySort;
import vartas.jra.query.listings.QueryDuplicates;
import vartas.jra.types.Thing;
import vartas.jra.types.TrendingSubreddits;

import java.io.IOException;
import java.util.List;

/**
 * Test all endpoints in the Listing section
 */
public class ListingTest extends AbstractTest{
    static Client client;
    static Subreddit subreddit;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getUserless(ListingTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);

        subreddit = client.getSubreddit(SUBREDDIT_NAME);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetTrendingSubreddits() throws InterruptedException, IOException, HttpException {
        TrendingSubreddits trendingSubreddits = client.getTrendingSubreddits();
        trendingSubreddits.getSubredditNames();
        trendingSubreddits.getCommentUrl();
        trendingSubreddits.getCommentCount();
    }

    @Test
    public void testGetBestLinks() throws InterruptedException, IOException, HttpException {
        client.getBestLinks()
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);
    }

    @ParameterizedTest
    @ValueSource(strings = {"t3_kvzaot"})
    public void testGetLinksById(String name) throws InterruptedException, IOException, HttpException {
        client.getLinksById(name).query().forEach(AbstractTest::check);
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetComments(String name) throws InterruptedException, IOException, HttpException {
        Pair<Link, List<Thing>> result;

        result = client.getComments(name).query();
        check(result.getKey());
        result.getValue().forEach(AbstractTest::check);

        result = subreddit.getComments(name).query();
        check(result.getKey());
        result.getValue().forEach(AbstractTest::check);
    }

    @Test
    public void testGetControversialLinks() throws InterruptedException, IOException, HttpException {
        client.getControversialLinks()
                .setTimePeriod(QuerySort.TimePeriod.ALL)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);

        subreddit.getControversialLinks()
                .setTimePeriod(QuerySort.TimePeriod.ALL)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetDuplicates(String article) throws InterruptedException, IOException, HttpException {
        Pair<Link, List<Link>> result;
        result = client.getDuplicates(article)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setCrossPostsOnly(false)
                .setLimit(25)
                .setShow("all")
                .setSort(QueryDuplicates.Sort.NUMBER_OF_COMMENTS)
                .setSubreddit("RedditDev")
                .setExpandSubreddits(false)
                .query();

        check(result.getKey());
        result.getValue().forEach(AbstractTest::check);
    }

    @Test
    public void testGetHotLinks() throws InterruptedException, IOException, HttpException {
        client.getHotLinks()
                .setRegion(Query.GeoLocation.GLOBAL)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);

        subreddit.getHotLinks()
                .setRegion(Query.GeoLocation.GLOBAL)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);
    }

    @Test
    public void testGetNewLinks() throws InterruptedException, IOException, HttpException {
        client.getNewLinks()
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);

        subreddit.getNewLinks()
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);
    }

    @Test
    public void testGetRandomLink() throws InterruptedException, IOException, HttpException {
        Pair<Link, List<Thing>> result;

        result = client.getRandomLink().query();
        check(result.getKey());
        result.getValue().forEach(AbstractTest::check);

        result = subreddit.getRandomLink().query();
        check(result.getKey());
        result.getValue().forEach(AbstractTest::check);
    }

    @Test
    public void testGetRisingLinks() throws InterruptedException, IOException, HttpException {
        client.getRisingLinks()
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);

        subreddit.getRisingLinks()
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);
    }

    @Test
    public void testGetTopLinks() throws InterruptedException, IOException, HttpException {
        client.getTopLinks()
                .setTimePeriod(QuerySort.TimePeriod.ALL)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);

        subreddit.getTopLinks()
                .setTimePeriod(QuerySort.TimePeriod.ALL)
                .setAfter(null)
                .setBefore(null)
                .setCount(0)
                .setLimit(25)
                .setShow("all")
                .setExpandSubreddits(false)
                .query()
                .forEach(AbstractTest::check);
    }
}
