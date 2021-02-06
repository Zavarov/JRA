package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.exceptions.HttpException;
import vartas.jra.models.TrendingSubreddits;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ClientTest extends AbstractTest{
    static Client client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(ClientTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetFrontPage() throws InterruptedException, IOException, HttpException {
        assertThat(client.getFrontPage().query()).isInstanceOf(FrontPage.class);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Account                                                                                                     //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetMe() throws InterruptedException, IOException, HttpException {
        assertThat(client.getMe().query()).isInstanceOf(SelfAccount.class);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Captcha                                                                                                     //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    @SuppressWarnings("deprecation")
    public void testNeedsCaptcha() {
        assertThatThrownBy(() -> client.needsCaptcha().query()).isInstanceOf(HttpException.class);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetTrendingSubreddits() throws InterruptedException, IOException, HttpException {
        assertThat(client.getTrendingSubreddits()).isInstanceOf(TrendingSubreddits.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"t3_kvzaot"})
    public void testGetLinksById(String name) throws InterruptedException, IOException, HttpException {
        assertThat(client.getLinksById(name).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetComments(String name) throws InterruptedException, IOException, HttpException {
        assertThat(client.getComments(name).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetDuplicates(String article) throws InterruptedException, IOException, HttpException {
        assertThat(client.getDuplicates(article).setParameter("sr","RedditDev").query()).isNotNull();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testGetUserDataByAccountIds(String ids) throws InterruptedException, IOException, HttpException {
        assertThat(client.getUserDataByAccountIds().setParameter("ids", ids).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void getUsernameAvailable(String name) throws InterruptedException, IOException, HttpException {
        assertThat(client.getUsernameAvailable().setParameter("user", name).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetAccount(String name) throws InterruptedException, IOException, HttpException {
        assertThat(client.getAccount(name).query()).isNotNull();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Subreddits                                                                                                  //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    @SuppressWarnings("deprecation")
    public void testGetRecommendSubreddits() throws InterruptedException, IOException, HttpException {
        assertThat(client.getRecommendSubreddits("RedditDev").query()).isNotNull();
    }

    @Test
    public void testGetSearchRedditNames() throws InterruptedException, IOException, HttpException {
        assertThat(client.getSearchRedditNames().setParameter("query", "Reddit").query()).isNotNull();
    }

    @Test
    public void testPostSearchRedditNames() throws InterruptedException, IOException, HttpException {
        assertThat(client.postSearchRedditNames().setParameter("query", "Reddit").query()).isNotNull();
    }

    @Test
    public void testPostSearchSubreddits() throws InterruptedException, IOException, HttpException {
        assertThat(client.postSearchSubreddits().setParameter("query", "reddit").query()).isNotNull();
    }

    //@Test
    public void testPostSiteAdmin() throws InterruptedException, IOException, HttpException {
        //TODO Test once we can get the current settings
        assertThat(client.postSiteAdmin().query()).isNotNull();
    }

    @Test
    public void testGetSubredditAutocomplete() throws InterruptedException, IOException, HttpException {
        assertThat(client.getSubredditAutocomplete().setParameter("query", "Reddit").query()).isNotNull();
    }

    @Test
    public void testGetSubredditAutocompleteV2() throws InterruptedException, IOException, HttpException {
        assertThat(client.getSubredditAutocompleteV2().setParameter("query", "Reddit").query()).isNotNull();
    }

    @Test
    public void testGetSubreddit() throws InterruptedException, IOException, HttpException {
        assertThat(client.getSubreddit("RedditDev").query()).isNotNull();
    }

    //@Test
    public void testPostSubscribe() throws InterruptedException, IOException, HttpException {
        assertThat(client.postSubscribe().query()).isNotNull();
    }

    @Test
    public void testGetSubredditsDefault() throws InterruptedException, IOException, HttpException{
        assertThat(client.getSubredditsDefault().query()).isNotNull();
    }

    @Test
    public void testGetSubredditsGold() throws InterruptedException, IOException, HttpException{
        assertThat(client.getSubredditsGold().query()).isNotNull();
    }

    @Test
    public void testGetSubredditsNew() throws InterruptedException, IOException, HttpException{
        assertThat(client.getSubredditsNew().query()).isNotNull();
    }

    @Test
    public void testGetSubredditsPopular() throws InterruptedException, IOException, HttpException{
        assertThat(client.getSubredditsPopular().query()).isNotNull();
    }

    @Test
    public void testGetSubredditsSearch() throws InterruptedException, IOException, HttpException{
        assertThat(client.getSubredditsSearch().setParameter("q","penguins").query()).isNotNull();
    }

    @Test
    public void testGetUsersNew() throws InterruptedException, IOException, HttpException{
        assertThat(client.getUsersNew().query()).isNotNull();
    }

    @Test
    public void testGetUsersPopular() throws InterruptedException, IOException, HttpException{
        assertThat(client.getUsersPopular().query()).isNotNull();
    }

    @Test
    public void testGetUsersSearch() throws InterruptedException, IOException, HttpException{
        assertThat(client.getUsersSearch().setParameter("q","penguins").query()).isNotNull();
    }
}
