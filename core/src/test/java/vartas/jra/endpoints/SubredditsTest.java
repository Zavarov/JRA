package vartas.jra.endpoints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.AbstractClient;
import vartas.jra.AbstractTest;
import vartas.jra.exceptions.HttpException;
import vartas.jra.mock.LinkMock;
import vartas.jra.mock.SubredditMock;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SubredditsTest extends AbstractTest {
    static AbstractClient client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(SubredditsTest.class.getSimpleName());
        client.login(AbstractClient.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //------------------------------------------------------------------------------------------------------------------

    //@Test
    public void testGetBanned() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getBanned(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetBanned(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getBanned(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testGetContributors() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getContributors(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetContributors(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getContributors(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testGetModerators() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getModerators(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetModerators(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getModerators(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testGetMuted() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getMuted(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetMuted(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getMuted(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testGetWikibanned() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getWikibanned(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetWikibanned(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getWikibanned(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testGetWikicontributors() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getWikicontributors(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetWikicontributors(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getWikicontributors(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testPostDeleteSubredditBanner() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditBanner(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditBanner(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditBanner(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testPostDeleteSubredditHeader() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditHeader(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditHeader(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditHeader(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testPostDeleteSubredditIcon() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditIcon(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditIcon(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditIcon(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testPostDeleteSubredditImage() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditImage(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostDeleteSubredditImage(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postDeleteSubredditImage(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    @SuppressWarnings("deprecation")
    public void testGetRecommendSubreddits(String subreddits) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getRecommendSubreddits(client, subreddits).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSearchRedditNames(String query) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSearchRedditNames(client).setParameter("query", query).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostSearchRedditNames(String query) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postSearchRedditNames(client).setParameter("query", query).query()).isNotNull();
    }

    //@Test
    public void testPostSiteAdmin() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postSiteAdmin(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testPostSearchSubreddits(String query) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postSearchSubreddits(client).setParameter("query", query).query()).isNotNull();
    }

    @Test
    public void testGetSubmitText() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubmitText(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubmitText(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubmitText(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditAutocomplete(String query) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubredditAutocomplete(client).setParameter("query", query).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubredditAutocompleteV2(String query) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubredditAutocompleteV2(client).setParameter("query", query).query()).isNotNull();
    }

    //@Test
    public void testPostStylesheet() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postStylesheet(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostStylesheet(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postStylesheet(client, subreddit).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostSubscribe(String subreddits) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postSubscribe(client).setParameter("sr", subreddits).query()).isNotNull();
    }

    //@Test
    public void testPostUploadImage() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postUploadImage(client).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testPostUploadImage(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.postUploadImage(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetPostRequirements(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getPostRequirements(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    public void testGetSubreddit(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubreddit(client, SubredditMock::new ,subreddit).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetSubredditEdit(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubredditEdit(client, subreddit).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetSubredditRules(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSubredditRules(client, subreddit).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetSubredditTraffic(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getTraffic(client, subreddit).query()).isNotNull();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetSidebar() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSidebar(client).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"RedditDev"})
    @SuppressWarnings("deprecation")
    public void testGetSidebar(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSidebar(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testGetSticky() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSticky(client, LinkMock::from).query()).isNotNull();
    }

    //@ParameterizedTest
    //@ValueSource(strings = {"RedditDev"})
    public void testGetSticky(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getSticky(client, LinkMock::from, subreddit).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsDefault() throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getSubredditsDefault(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsGold() throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getSubredditsGold(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsNew() throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getSubredditsNew(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetSubredditsPopular() throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getSubredditsPopular(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineContributor() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getMineContributor(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineModerator() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getMineModerator(client, SubredditMock::from).query()).isNotNull();
    }

    //@Test
    public void testGetMineStreams() throws InterruptedException, IOException, HttpException {
        //Fails with 403
        assertThat(Subreddits.getMineStreams(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetMineSubscriber() throws InterruptedException, IOException, HttpException {
        assertThat(Subreddits.getMineSubscriber(client, SubredditMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"penguins"})
    public void testGetSubredditsSearch(String query) throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getSubredditsSearch(client, SubredditMock::from).setParameter("q", query).query()).isNotNull();
    }

    @Test
    public void testGetUsersNew() throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getUsersNew(client, SubredditMock::from).query()).isNotNull();
    }

    @Test
    public void testGetUsersPopular() throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getUsersPopular(client, SubredditMock::from).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"penguins"})
    public void testGetUsersSearch(String query) throws InterruptedException, IOException, HttpException{
        assertThat(Subreddits.getUsersSearch(client, SubredditMock::from).setParameter("q", query).query()).isNotNull();
    }
}
