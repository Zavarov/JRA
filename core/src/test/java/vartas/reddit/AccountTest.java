package vartas.reddit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.NotFoundException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountTest extends AbstractTest{
    static Client client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(CaptchaTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetMe() throws InterruptedException, IOException, HttpException {
        client.getMe();
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetBlocked() {
        assertThatThrownBy(() -> client.getBlocked()).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void testGetFriends() throws InterruptedException, IOException, HttpException {
        client.getFriends().forEach(AbstractTest::check);
    }

    @Test
    public void testGetKarma() throws InterruptedException, IOException, HttpException {
        client.getKarma().forEach(AbstractTest::check);
    }

    @Test
    public void testGetPreferences() throws InterruptedException, IOException, HttpException {
        client.getPreferences();
    }

    @Test
    public void testGetTrophies() throws InterruptedException, IOException, HttpException {
        client.getTrophies().forEach(AbstractTest::check);
    }

    @Test
    public void testGetPreferencesBlocked() throws InterruptedException, IOException, HttpException {
        client.getPreferencesBlocked().forEach(AbstractTest::check);
    }

    @Test
    public void testGetPreferencesFriends() throws InterruptedException, IOException, HttpException {
        client.getPreferencesFriends().forEach(AbstractTest::check);
    }

    @Test
    public void testGetPreferencesMessaging() throws InterruptedException, IOException, HttpException {
        Messaging messaging = client.getPreferencesMessaging();
        messaging.getBlocked().forEach(AbstractTest::check);
        messaging.getTrusted().forEach(AbstractTest::check);
    }

    @Test
    public void testGetPreferencesTrusted() throws InterruptedException, IOException, HttpException {
        client.getPreferencesTrusted().forEach(AbstractTest::check);
    }
}
