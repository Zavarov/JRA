package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.exceptions.HttpException;
import vartas.jra.types.TrendingSubreddits;

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
        client.getLinksById(name).query();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetComments(String name) throws InterruptedException, IOException, HttpException {
        client.getComments(name).query();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetDuplicates(String article) throws InterruptedException, IOException, HttpException {
        client.getDuplicates(article).setParameter("sr","RedditDev").query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testGetUserDataByAccountIds(String ids) throws InterruptedException, IOException, HttpException {
        client.getUserDataByAccountIds().setParameter("ids", ids).query();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void getUsernameAvailable(String name) throws InterruptedException, IOException, HttpException {
        client.getUsernameAvailable().setParameter("user", name).query();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetAccount(String name) throws InterruptedException, IOException, HttpException {
        client.getAccount(name).query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Subreddits                                                                                                  //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    @SuppressWarnings("deprecation")
    public void testGetRecommendSubreddits() throws InterruptedException, IOException, HttpException {
        client.getRecommendSubreddits("RedditDev").query();
    }

    @Test
    public void testGetSearchRedditNames() throws InterruptedException, IOException, HttpException {
        client.getSearchRedditNames().setParameter("query", "Reddit").query();
    }

    @Test
    public void testPostSearchRedditNames() throws InterruptedException, IOException, HttpException {
        client.postSearchRedditNames().setParameter("query", "Reddit").query();
    }

    @Test
    public void testPostSearchSubreddits() throws InterruptedException, IOException, HttpException {
        client.postSearchSubreddits()
                .setParameter("query", "reddit")
                .setParameter("sr_detail", false)
                .query();
    }

    //@Test
    public void testPostSiteAdmin() throws InterruptedException, IOException, HttpException {
        //TODO Test once we can get the current settings
        client.postSiteAdmin().query();
    }

    @Test
    public void testGetSubredditAutocomplete() throws InterruptedException, IOException, HttpException {
        client.getSubredditAutocomplete().setParameter("query", "Reddit").query();
    }

    @Test
    public void testGetSubredditAutocompleteV2() throws InterruptedException, IOException, HttpException {
        client.getSubredditAutocompleteV2().setParameter("query", "Reddit").query();
    }

    @Test
    public void testGetSubreddit() throws InterruptedException, IOException, HttpException {
        client.getSubreddit("RedditDev").query();
    }

    //@Test
    public void testPostSubscribe() throws InterruptedException, IOException, HttpException {
        client.postSubscribe().query();
    }

    @Test
    public void testGetSubredditsDefault() throws InterruptedException, IOException, HttpException{
        client.getSubredditsDefault().query();
    }

    @Test
    public void testGetSubredditsGold() throws InterruptedException, IOException, HttpException{
        client.getSubredditsGold().query();
    }

    @Test
    public void testGetSubredditsNew() throws InterruptedException, IOException, HttpException{
        client.getSubredditsNew().query();
    }

    @Test
    public void testGetSubredditsPopular() throws InterruptedException, IOException, HttpException{
        client.getSubredditsPopular().query();
    }

    @Test
    public void testGetSubredditsSearch() throws InterruptedException, IOException, HttpException{
        client.getSubredditsSearch().setParameter("q","penguins").query();
    }

    @Test
    public void testGetUsersNew() throws InterruptedException, IOException, HttpException{
        client.getUsersNew().query();
    }

    @Test
    public void testGetUsersPopular() throws InterruptedException, IOException, HttpException{
        client.getUsersPopular().query();
    }

    @Test
    public void testGetUsersSearch() throws InterruptedException, IOException, HttpException{
        client.getUsersSearch().setParameter("q","penguins").query();
    }
}
