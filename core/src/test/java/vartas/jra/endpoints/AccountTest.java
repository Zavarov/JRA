package vartas.jra.endpoints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.AbstractClient;
import vartas.jra.AbstractTest;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.NotFoundException;
import vartas.jra.mock.PreferencesMock;
import vartas.jra.mock.SelfAccountMock;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AccountTest extends AbstractTest {
    static AbstractClient client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(AccountTest.class.getSimpleName());
        client.login(AbstractClient.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetMe() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getMe(client, SelfAccountMock::new).query()).isNotNull();
    }
    @Test
    @SuppressWarnings("deprecation")
    public void testGetBlocked() {
        assertThatThrownBy(() -> Account.getBlocked(client).query()).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetFriends() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getFriends(client).query()).isNotNull();
    }

    @Test
    public void testGetKarma() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getKarma(client).query()).isNotNull();
    }

    @Test
    public void testGetPreferences() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getPreferences(client, PreferencesMock::new).query()).isNotNull();
    }

    @Test
    public void testPatchPreferences() throws InterruptedException, IOException, HttpException {
        PreferencesMock before = Account.getPreferences(client, PreferencesMock::new).query();
        assertThat(before).isNotNull();
        PreferencesMock after = Account.patchPreferences(client, PreferencesMock::new).setParameter("beta", before.getBeta()).query();
        assertThat(after).isNotNull();
    }

    @Test
    public void testGetTrophies() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getTrophies(client).query()).isNotNull();
    }

    @Test
    public void testGetPreferencesBlocked() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getPreferencesBlocked(client).query()).isNotNull();
    }

    @Test
    public void testGetPreferencesFriends() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getPreferencesFriends(client).query()).isNotNull();
    }

    @Test
    public void testGetPreferencesMessaging() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getPreferencesMessaging(client).query()).isNotNull();
    }

    @Test
    public void testGetPreferencesTrusted() throws InterruptedException, IOException, HttpException {
        assertThat(Account.getPreferencesTrusted(client).query()).isNotNull();
    }
}
