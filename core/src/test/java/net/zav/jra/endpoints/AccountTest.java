package net.zav.jra.endpoints;

import net.zav.jra.AbstractTest;
import net.zav.jra.mock.ClientMock;
import net.zav.jra.mock.PreferencesMock;
import net.zav.jra.mock.SelfAccountMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest extends AbstractTest {
    ClientMock client;

    @BeforeEach
    public void setUp() {
        TARGET_PATH = JSON_PATH.resolve("Account");
        client = new ClientMock();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetMe() throws InterruptedException, IOException {
        client.json = readJson("SelfAccount.json");
        assertThat(Account.getMe(client, SelfAccountMock::new).query()).isNotNull();
    }
    @Test
    @SuppressWarnings("deprecation")
    public void testGetBlocked() throws InterruptedException, IOException {
        client.json = readJson("UserList.json");
        assertThat(Account.getBlocked(client).query()).hasSize(1);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetFriends() throws InterruptedException, IOException {
        client.json = readJson("UserList.json");
        assertThat(Account.getFriends(client).query()).hasSize(1);
    }

    @Test
    public void testGetKarma() throws InterruptedException, IOException {
        client.json = readJson("KarmaList.json");
        assertThat(Account.getKarma(client).query()).hasSize(1);
    }

    @Test
    public void testGetPreferences() throws InterruptedException, IOException {
        client.json = readJson("Preferences.json");
        assertThat(Account.getPreferences(client, PreferencesMock::new).query()).isNotNull();
    }

    @Test
    public void testPatchPreferences() throws InterruptedException, IOException {
        client.json = readJson("Preferences.json");
        assertThat(Account.patchPreferences(client, PreferencesMock::new).query()).isNotNull();
    }

    @Test
    public void testGetTrophies() throws InterruptedException, IOException {
        client.json = readJson("TrophyList.json");
        assertThat(Account.getTrophies(client).query()).hasSize(2);
    }

    @Test
    public void testGetPreferencesBlocked() throws InterruptedException, IOException {
        client.json = readJson("UserList.json");
        assertThat(Account.getPreferencesBlocked(client).query()).hasSize(1);
    }

    @Test
    public void testGetPreferencesFriends() throws InterruptedException, IOException {
        client.json = readJson("PreferencesFriends.json");
        assertThat(Account.getPreferencesFriends(client).query()).hasSize(1);
    }

    @Test
    public void testGetPreferencesMessaging() throws InterruptedException, IOException {
        client.json = readJson("Messaging.json");
        assertThat(Account.getPreferencesMessaging(client).query()).isNotNull();
    }

    @Test
    public void testGetPreferencesTrusted() throws InterruptedException, IOException {
        client.json = readJson("UserList.json");
        assertThat(Account.getPreferencesTrusted(client).query()).hasSize(1);
    }
}
