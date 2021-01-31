package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.exceptions.HttpException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AccountTest extends AbstractTest{
    static Client client;
    static Account account;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(AccountTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
        account = client.getAccount(getConfig().getString("account")).query();
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetComments() throws InterruptedException, IOException, HttpException {
        account.getComments().query();
    }

    @Test
    public void testGetDownvoted() throws InterruptedException, IOException, HttpException {
        account.getDownvoted().query();
    }

    @Test
    public void testGetGilded() throws InterruptedException, IOException, HttpException {
        account.getGilded().query();
    }

    @Test
    public void testGetHidden() throws InterruptedException, IOException, HttpException {
        account.getHidden().query();
    }

    @Test
    public void testGetOverview() throws InterruptedException, IOException, HttpException {
        account.getOverview().query();
    }

    @Test
    public void testGetSaved() throws InterruptedException, IOException, HttpException {
        account.getSaved().query();
    }

    @Test
    public void testGetSubmitted() throws InterruptedException, IOException, HttpException {
        account.getSubmitted().query();
    }

    @Test
    public void testGetTrophies() throws InterruptedException, IOException, HttpException {
        account.getTrophies().query();
    }

    @Test
    public void testGetUpvoted() throws InterruptedException, IOException, HttpException {
        account.getUpvoted().query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    //@Test
    public void testPostBlock() throws InterruptedException, IOException, HttpException {
        //Not tested / Error Code 400
        account.postBlock().query();
    }

    //@Test
    public void testPostFriend() throws InterruptedException, IOException, HttpException {
        //Not tested / Error Code 500
        account.postFriend().query();
    }

    //@Test
    public void testPostReportAccount() throws InterruptedException, IOException, HttpException {
        //Don't report users just for testing the endpoint
        //account.postReport().query();
        throw new UnsupportedEncodingException();
    }

    //@Test
    public void testSetPermission() throws InterruptedException, IOException, HttpException {
        //Not tested
        account.postSetPermission().query();
    }

    //@Test
    public void testPostUnfriend() throws InterruptedException, IOException, HttpException {
        //Not tested / Error Code 500
        account.postFriend().query();
    }

    //@Test
    public void testDeleteFriends() throws InterruptedException, IOException, HttpException {
        //works
        account.deleteFriends().query();
    }

    //@Test
    public void testGetFriends() throws InterruptedException, IOException, HttpException {
        //works
        account.getFriends().query();
    }

    //@Test
    public void testPutFriends() throws InterruptedException, IOException, HttpException {
        //works
        account.putFriends().query();
    }
}
