package vartas.jra.endpoints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.AbstractClient;
import vartas.jra.AbstractTest;
import vartas.jra.exceptions.HttpException;
import vartas.jra.mock.AccountMock;
import vartas.jra.mock.CommentMock;
import vartas.jra.mock.LinkMock;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersTest extends AbstractTest {
    static AbstractClient client;
    static String self;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(UsersTest.class.getSimpleName());
        client.login(AbstractClient.Duration.TEMPORARY);
        self = getConfig().getString("account");
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //------------------------------------------------------------------------------------------------------------------

    //@Test
    public void testPostBlock() throws InterruptedException, IOException, HttpException {
        assertThat(Users.postBlockUser(client).query()).isNotNull();
    }

    //@Test
    public void testPostFriend() throws InterruptedException, IOException, HttpException {
        assertThat(Users.postFriend(client).query()).isNotNull();
    }

    //@Test
    public void testPostSubredditFriend(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Users.postFriend(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testPostReportUser() throws InterruptedException, IOException, HttpException {
        //Don't report users just for testing the endpoint
        //assertThat(Users.postReportUser(client).query()).isNotNull();
        throw new UnsupportedEncodingException();
    }

    //@Test
    public void testSetPermission() throws InterruptedException, IOException, HttpException {
        assertThat(Users.postSetPermission(client).query()).isNotNull();
    }

    //@Test
    public void testPostSubredditSetPermission(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Users.postSetPermission(client, subreddit).query()).isNotNull();
    }

    //@Test
    public void testPostUnfriend() throws InterruptedException, IOException, HttpException {
        assertThat(Users.postUnfriend(client).query()).isNotNull();
    }

    //@Test
    public void testPostSubredditUnfriend(String subreddit) throws InterruptedException, IOException, HttpException {
        assertThat(Users.postUnfriend(client, subreddit).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"t2_1qwk"})
    public void testGetUserDataByAccountIds(String ids) throws InterruptedException, IOException, HttpException {
        assertThat(Users.getUserDataByAccountIds(client).setParameter("ids", ids).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void getUsernameAvailable(String name) throws InterruptedException, IOException, HttpException {
        assertThat(Users.getUsernameAvailable(client).setParameter("user", name).query()).isNotNull();
    }

    //@Test
    public void testDeleteMeFriends(String username) throws InterruptedException, IOException, HttpException {
        assertThat(Users.deleteMeFriends(client, username).query()).isNotNull();
    }

    //@Test
    public void testGetMeFriends(String username) throws InterruptedException, IOException, HttpException {
        assertThat(Users.getMeFriends(client, username).query()).isNotNull();
    }

    //@Test
    public void testPutMeFriends(String username) throws InterruptedException, IOException, HttpException {
        assertThat(Users.putMeFriends(client, username).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetTrophies(String username) throws InterruptedException, IOException, HttpException {
        assertThat(Users.getTrophies(client, username).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Reddit"})
    public void testGetAccount(String username) throws InterruptedException, IOException, HttpException {
        assertThat(Users.getAccount(client, AccountMock::new, username).query()).isNotNull();
    }

    @Test
    public void testGetComments() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getComments(client, CommentMock::from, self).query()).isNotNull();
    }

    @Test
    public void testGetDownvoted() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getDownvoted(client, self).query()).isNotNull();
    }

    @Test
    public void testGetGilded() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getGilded(client, self).query()).isNotNull();
    }

    @Test
    public void testGetHidden() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getHidden(client, self).query()).isNotNull();
    }

    @Test
    public void testGetOverview() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getOverview(client, self).query()).isNotNull();
    }

    @Test
    public void testGetSaved() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getSaved(client, self).query()).isNotNull();
    }

    @Test
    public void testGetSubmitted() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getSubmitted(client, LinkMock::from, self).query()).isNotNull();
    }

    @Test
    public void testGetUpvoted() throws InterruptedException, IOException, HttpException {
        assertThat(Users.getUpvoted(client, self).query()).isNotNull();
    }
}
