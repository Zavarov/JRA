package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.NotFoundException;
import vartas.jra.types.*;

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

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Account                                                                                                     //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetMe() throws InterruptedException, IOException, HttpException {
        assertThat(client.getMe().query()).isInstanceOf(Identity.class);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetBlocked() {
        assertThatThrownBy(() -> client.getBlocked().query()).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetFriends() throws InterruptedException, IOException, HttpException {
        assertThat(client.getFriends().query()).isInstanceOf(UserList.class);
    }

    @Test
    public void testGetKarma() throws InterruptedException, IOException, HttpException {
        assertThat(client.getKarma().query()).isInstanceOf(KarmaList.class);
    }

    @Test
    public void testGetPreferences() throws InterruptedException, IOException, HttpException {
        assertThat(client.getPreferences().query()).isInstanceOf(Preferences.class);
    }

    @Test
    public void testGetTrophies() throws InterruptedException, IOException, HttpException {
        assertThat(client.getTrophies().query()).isInstanceOf(TrophyList.class);
    }

    @Test
    public void testGetPreferencesBlocked() throws InterruptedException, IOException, HttpException {
        assertThat(client.getPreferencesBlocked().query()).isInstanceOf(UserList.class);
    }

    @Test
    public void testGetPreferencesFriends() throws InterruptedException, IOException, HttpException {
        assertThat(client.getPreferencesFriends().query()).isInstanceOf(UserList.class);
    }

    @Test
    public void testGetPreferencesMessaging() throws InterruptedException, IOException, HttpException {
        assertThat(client.getPreferencesMessaging().query()).isInstanceOf(Messaging.class);
    }

    @Test
    public void testGetPreferencesTrusted() throws InterruptedException, IOException, HttpException {
        assertThat(client.getPreferencesTrusted().query()).isInstanceOf(UserList.class);
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

    @Test
    public void testGetBestLinks() throws InterruptedException, IOException, HttpException {
        client.getBestLinks().query();
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

    @Test
    public void testGetControversialLinks() throws InterruptedException, IOException, HttpException {
        client.getControversialLinks().query();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kvzaot"})
    public void testGetDuplicates(String article) throws InterruptedException, IOException, HttpException {
        client.getDuplicates(article).setParameter("sr","RedditDev").query();
    }

    @Test
    public void testGetHotLinks() throws InterruptedException, IOException, HttpException {
        client.getHotLinks().query();
    }

    @Test
    public void testGetNewLinks() throws InterruptedException, IOException, HttpException {
        client.getNewLinks().query();
    }

    @Test
    public void testGetRandomSubmission() throws InterruptedException, IOException, HttpException {
        client.getRandomSubmission().query();
    }

    @Test
    public void testGetRisingLinks() throws InterruptedException, IOException, HttpException {
        client.getRisingLinks().query();
    }

    @Test
    public void testGetTopLinks() throws InterruptedException, IOException, HttpException {
        client.getTopLinks().query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Search                                                                                                      //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetSearch() throws InterruptedException, IOException, HttpException {
        client.getSearch().setParameter("q", "penguins").query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//


    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetAccount(String name) throws InterruptedException, IOException, HttpException {
        client.getAccount(name).query();
    }
}
