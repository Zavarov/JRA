package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.NotFoundException;

import java.io.IOException;

public class SubredditTest extends AbstractTest{
    static Client client;
    static Subreddit subreddit;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(SubredditTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
        subreddit = client.getSubreddit("RedditDev");
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetRules() throws IOException, HttpException, InterruptedException {
        subreddit.getRules().query();
    }

    @Test
    public void testGetRandomSubmission() throws InterruptedException, IOException, HttpException {
        subreddit.getRandomSubmission().query();
    }

    @Test
    public void testGetRising() throws InterruptedException, IOException, HttpException {
        subreddit.getRisingLinks().query();
    }

    @Test
    public void testGetSearch() throws InterruptedException, IOException, HttpException {
        subreddit.getSearch().setParameter("q", "penguins").query();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetSidebar() throws InterruptedException, IOException, HttpException {
        subreddit.getSidebar().query();
    }

    @Test
    public void testGetSticky() throws InterruptedException, IOException, HttpException {
        try {
            subreddit.getSticky().setParameter("num", 1).query();
        }catch(NotFoundException ignored){}
    }
}
