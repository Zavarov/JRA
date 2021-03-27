package proj.zav.jra.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import proj.zav.jra.AbstractTest;
import proj.zav.jra.mock.AccountMock;
import proj.zav.jra.mock.ClientMock;
import proj.zav.jra.mock.CommentMock;
import proj.zav.jra.mock.LinkMock;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersTest extends AbstractTest {
    static final String self = "self";

    ClientMock client;

    @BeforeEach
    public void setUp() {
        TARGET_PATH = JSON_PATH.resolve("Users");
        client = new ClientMock();
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    public void testPostBlock() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postBlockUser(client).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostFriend() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postFriend(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testPostSubredditFriend(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postFriend(client, subreddit).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostReportUser() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postReportUser(client).query()).isEqualTo(client.json);
    }

    @Test
    public void testSetPermission() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postSetPermission(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testPostSubredditSetPermission(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postSetPermission(client, subreddit).query()).isEqualTo(client.json);
    }

    @Test
    public void testPostUnfriend() throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postUnfriend(client).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testPostSubredditUnfriend(String subreddit) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.postUnfriend(client, subreddit).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testGetUserDataByAccountIds(String ids) throws InterruptedException, IOException {
        client.json = readJson("UserData.json");
        assertThat(Users.getUserDataByAccountIds(client).setParameter("ids", ids).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetUsernameAvailable(String name) throws InterruptedException, IOException {
        client.json = readJson("Boolean.json");
        assertThat(Users.getUsernameAvailable(client).setParameter("user", name).query()).isTrue();
    }


    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testDeleteMeFriends(String username) throws InterruptedException, IOException {
        client.json = "null";
        assertThat(Users.deleteMeFriends(client, username).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Zavarov"})
    public void testGetMeFriends(String username) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.getMeFriends(client, username).query()).isEqualTo(client.json);
    }


    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testPutMeFriends(String username) throws InterruptedException, IOException {
        client.json = "{}";
        assertThat(Users.putMeFriends(client, username).query()).isEqualTo(client.json);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetTrophies(String username) throws InterruptedException, IOException {
        client.json = readJson("TrophyList.json");
        assertThat(Users.getTrophies(client, username).query()).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetAccount(String username) throws InterruptedException, IOException {
        client.json = readJson("Account.json");
        assertThat(Users.getAccount(client, AccountMock::new, username).query()).isNotNull();
    }

    @Test
    public void testGetComments() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getComments(client, CommentMock::from, self).query()).isNotNull();
    }

    @Test
    public void testGetDownvoted() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getDownvoted(client, self).query()).isNotNull();
    }

    @Test
    public void testGetGilded() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getGilded(client, self).query()).isNotNull();
    }

    @Test
    public void testGetHidden() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getHidden(client, self).query()).isNotNull();
    }

    @Test
    public void testGetOverview() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getOverview(client, self).query()).isNotNull();
    }

    @Test
    public void testGetSaved() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getSaved(client, self).query()).isNotNull();
    }

    @Test
    public void testGetSubmitted() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getSubmitted(client, LinkMock::from, self).query()).isNotNull();
    }

    @Test
    public void testGetUpvoted() throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Users.getUpvoted(client, self).query()).isNotNull();
    }
}
