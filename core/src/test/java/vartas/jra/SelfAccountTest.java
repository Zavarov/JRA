package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.NotFoundException;
import vartas.jra.types.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SelfAccountTest extends AbstractTest{
    static Client client;
    static SelfAccount me;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(SelfAccountTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
        me = client.getMe().query();
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
    @SuppressWarnings("deprecation")
    public void testGetBlocked() {
        assertThatThrownBy(() -> me.getBlocked().query()).isInstanceOf(NotFoundException.class);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testGetFriends() throws InterruptedException, IOException, HttpException {
        assertThat(me.getFriends().query()).isInstanceOf(UserList.class);
    }

    @Test
    public void testGetKarma() throws InterruptedException, IOException, HttpException {
        assertThat(me.getKarma().query()).isInstanceOf(KarmaList.class);
    }

    @Test
    public void testGetPreferences() throws InterruptedException, IOException, HttpException {
        assertThat(me.getPreferences().query()).isInstanceOf(Preferences.class);
    }

    @Test
    public void testPatchPreferences() throws InterruptedException, IOException, HttpException {
        Preferences preferences = me.getPreferences().query();
        assertThat(me.patchPreferences().setParameter("beta", preferences.getBeta()).query()).isInstanceOf(Preferences.class);
    }

    @Test
    public void testGetTrophies() throws InterruptedException, IOException, HttpException {
        assertThat(me.getTrophies().query()).isInstanceOf(TrophyList.class);
    }

    @Test
    public void testGetPreferencesBlocked() throws InterruptedException, IOException, HttpException {
        assertThat(me.getPreferencesBlocked().query()).isInstanceOf(UserList.class);
    }

    @Test
    public void testGetPreferencesFriends() throws InterruptedException, IOException, HttpException {
        assertThat(me.getPreferencesFriends().query()).isInstanceOf(UserList.class);
    }

    @Test
    public void testGetPreferencesMessaging() throws InterruptedException, IOException, HttpException {
        assertThat(me.getPreferencesMessaging().query()).isInstanceOf(Messaging.class);
    }

    @Test
    public void testGetPreferencesTrusted() throws InterruptedException, IOException, HttpException {
        assertThat(me.getPreferencesTrusted().query()).isInstanceOf(UserList.class);
    }
}
