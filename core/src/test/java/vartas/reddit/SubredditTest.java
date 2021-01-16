package vartas.reddit;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.NotFoundException;
import vartas.reddit.exceptions.RateLimiterException;
import vartas.reddit.types.Rules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class SubredditTest {
    static Client client;
    static Subreddit redditdev;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        String content = Files.readString(Paths.get("src", "test", "resources", "config.json"));
        JSONObject config = new JSONObject(content);

        String version = SubredditTest.class.getSimpleName();
        String platform = config.getString("platform");
        String author = config.getString("name");
        String id = config.getString("id");
        String secret = config.getString("secret");

        client = new UserlessClient(platform,author,version,id,secret);
        client.login(Client.Duration.TEMPORARY);


        redditdev = client.getSubreddit("RedditDev");
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetRules() throws IOException, HttpException, RateLimiterException, InterruptedException {
        Rules rules = redditdev.getRules();

        assertThat(rules.getSiteRules()).isNotEmpty();

        rules.getRules().forEach(rule -> {
            rule.getCreatedUtc();
            rule.getDescription();
            rule.getDescriptionHtml();
            rule.getKind();
            rule.getPriority();
            rule.getShortName();
            rule.getViolationReason();
        });

        rules.getSiteRulesFlow().forEach(nextStepReason -> {
            nextStepReason.getReasonTextToShow();
            nextStepReason.getReasonText();
            nextStepReason.getNextStepReasons();
            nextStepReason.getNextStepHeader();
        });
    }

    @Test
    public void testGetRandom() throws InterruptedException, IOException, HttpException {
        redditdev.getRandomLink().query();
    }

    @Test
    public void testGetRising() throws InterruptedException, IOException, HttpException {
        redditdev.getRisingLinks().query();
    }

    @Test
    public void testGetSearch() throws InterruptedException, IOException, HttpException {
        redditdev.getSearch()
                .setAfter(null)
                .setBefore(null)
                //.setCategory("foo") //May result in an 500 error (Internal Server Error)
                .setCount(0)
                .includeFacets(true)
                .setLimit(25)
                .setQuery("Snoo")
                .restrictSubreddit(true)
                .show("all")
                .setSort(Subreddit.Query.Sort.NEW)
                .expandSubreddits(false)
                .setTimePeriod(Subreddit.Query.TimePeriod.ALL)
                .setTypes(Subreddit.Query.Type.LINK)
                .query();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetSidebar() {
        redditdev.getSidebar();
    }

    @Test
    public void testGetSticky() throws InterruptedException, IOException, HttpException {
        try {
            redditdev.getSticky(1);
        }catch(NotFoundException ignored){}
    }
}
