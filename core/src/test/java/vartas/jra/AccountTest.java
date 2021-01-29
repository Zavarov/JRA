package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.exceptions.HttpException;

import java.io.IOException;

public class AccountTest extends AbstractTest{
    static Client client;
    static Account account;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(AccountTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
        account = client.getAccount("Reddit").query();
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    @Test
    public void testGetComments(){

    }

    @Test
    public void testGetDownvoted(){

    }

    @Test
    public void testGetGilded(){

    }

    @Test
    public void testGetHidden(){

    }

    @Test
    public void testGetOverview(){

    }

    @Test
    public void testGetSaved(){

    }

    @Test
    public void testGetSubmitted(){

    }

    @Test
    public void testGetTrophies(){

    }

    @Test
    public void testGetUpvoted(){

    }
}
