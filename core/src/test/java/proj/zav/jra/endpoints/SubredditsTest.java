package proj.zav.jra.endpoints;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import proj.zav.jra.AbstractTest;
import proj.zav.jra.mock.ClientMock;
import proj.zav.jra.mock.LinkMock;
import proj.zav.jra.mock.SubredditMock;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SubredditsTest extends AbstractTest {
    ClientMock client;

    @BeforeEach
    public void setUp() {
        TARGET_PATH = JSON_PATH.resolve("Subreddits");
        client = new ClientMock();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetBanned() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getBanned(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetBanned(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getBanned(client, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetContributors() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getContributors(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetContributors(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getContributors(client, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetModerators() throws InterruptedException, IOException {
        client.json = readJson("UserList.json");
        assertThat(Subreddits.getModerators(client).query()).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetModerators(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("UserList.json");
        assertThat(Subreddits.getModerators(client, subreddit).query()).hasSize(1);
    }

    @Test
    public void testGetMuted() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getMuted(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetMuted(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getMuted(client, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetWikibanned() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getWikibanned(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetWikibanned(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getWikibanned(client, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetWikicontributors() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getWikicontributors(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetWikicontributors(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getWikicontributors(client, subreddit).query()).isNotNull();
    }

    @Test
    public void testPostDeleteSubredditBanner() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditBanner(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditBanner(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditBanner(client, subreddit).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostDeleteSubredditHeader() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditHeader(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditHeader(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditHeader(client, subreddit).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostDeleteSubredditIcon() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditIcon(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditIcon(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditIcon(client, subreddit).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostDeleteSubredditImage() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditImage(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditImage(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postDeleteSubredditImage(client, subreddit).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    @SuppressWarnings("deprecation")
    public void testGetRecommendSubreddits(String subreddits) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.getRecommendSubreddits(client, subreddits).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSearchRedditNames(String query) throws InterruptedException, IOException {
        client.json = readJson("SearchRedditNames.json");
        assertThat(Subreddits.getSearchRedditNames(client).setParameter("query", query).query()).containsExactly("redditdev");
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostSearchRedditNames(String query) throws InterruptedException, IOException {
        client.json = readJson("SearchRedditNames.json");
        assertThat(Subreddits.postSearchRedditNames(client).setParameter("query", query).query()).containsExactly("redditdev");
    }

    @Test
    public void testPostSiteAdmin() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postSiteAdmin(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostSearchSubreddits(String query) throws InterruptedException, IOException {
        client.json = readJson("SearchSubreddits.json");
        assertThat(Subreddits.postSearchSubreddits(client).setParameter("query", query).query()).isNotNull();
    }

    @Test
    public void testGetSubmitText() throws InterruptedException, IOException {
        client.json = readJson("SubmitText.json");
        assertThat(Subreddits.getSubmitText(client).query()).containsExactly(Pair.of("key", "value"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubmitText(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("SubmitText.json");
        assertThat(Subreddits.getSubmitText(client, subreddit).query()).containsExactly(Pair.of("key", "value"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditAutocomplete(String query) throws InterruptedException, IOException {
        client.json = readJson("Autocomplete.json");
        assertThat(Subreddits.getSubredditAutocomplete(client).setParameter("query", query).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditAutocompleteV2(String query) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getSubredditAutocompleteV2(client).setParameter("query", query).query()).isNotNull();
    }

    @Test
    public void testPostStylesheet() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postStylesheet(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostStylesheet(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postStylesheet(client, subreddit).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostSubscribe(String subreddits) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postSubscribe(client).setParameter("sr", subreddits).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostUploadImage() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postUploadImage(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostUploadImage(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.postUploadImage(client, subreddit).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetPostRequirements(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("PostRequirements.json");
        assertThat(Subreddits.getPostRequirements(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubreddit(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Subreddit.json");
        assertThat(Subreddits.getSubreddit(client, SubredditMock::new ,subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditEdit(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("SubredditEdit.json");
        assertThat(Subreddits.getSubredditEdit(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditRules(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Rules.json");
        assertThat(Subreddits.getSubredditRules(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditTraffic(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Traffic.json");
        assertThat(Subreddits.getTraffic(client, subreddit).query()).isNotEmpty();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetSidebar() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.getSidebar(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    @SuppressWarnings("deprecation")
    public void testGetSidebar(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Subreddits.getSidebar(client, subreddit).query()).isEqualTo(client.json);
    }

    @Test
    public void testGetSticky() throws InterruptedException, IOException {
        client.json = readJson("Submission.json");
        assertThat(Subreddits.getSticky(client, LinkMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSticky(String subreddit) throws InterruptedException, IOException {
        client.json = readJson("Submission.json");
        assertThat(Subreddits.getSticky(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsDefault() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getSubredditsDefault(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsGold() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getSubredditsGold(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsNew() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getSubredditsNew(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsPopular() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getSubredditsPopular(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineContributor() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getMineContributor(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineModerator() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getMineModerator(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineStreams() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getMineStreams(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineSubscriber() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getMineSubscriber(client, SubredditMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"penguins"})
    public void testGetSubredditsSearch(String query) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getSubredditsSearch(client, SubredditMock::from).setParameter("q", query).query()).isNotNull();
    }

    @Test
    public void testGetUsersNew() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getUsersNew(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetUsersPopular() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getUsersPopular(client, SubredditMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"penguins"})
    public void testGetUsersSearch(String query) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Subreddits.getUsersSearch(client, SubredditMock::from).setParameter("q", query).query()).isNotNull();
    }
}
