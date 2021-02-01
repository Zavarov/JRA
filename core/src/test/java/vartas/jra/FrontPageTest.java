package vartas.jra;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.jra.exceptions.HttpException;

import java.io.IOException;

public class FrontPageTest extends AbstractTest{
    static Client client;
    static FrontPage frontPage;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(ClientTest.class.getSimpleName());
        client.login(Client.Duration.TEMPORARY);
        frontPage = client.getFrontPage().query();
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    @Test
    public void testGetBestLinks() throws InterruptedException, IOException, HttpException {
        frontPage.getBestLinks().query();
    }
    @Test
    public void testGetControversialLinks() throws InterruptedException, IOException, HttpException {
        frontPage.getControversialLinks().query();
    }

    @Test
    public void testGetHotLinks() throws InterruptedException, IOException, HttpException {
        frontPage.getHotLinks().query();
    }

    @Test
    public void testGetNewLinks() throws InterruptedException, IOException, HttpException {
        frontPage.getNewLinks().query();
    }

    @Test
    public void testGetRandomSubmission() throws InterruptedException, IOException, HttpException {
        frontPage.getRandomSubmission().query();
    }

    @Test
    public void testGetRisingLinks() throws InterruptedException, IOException, HttpException {
        frontPage.getRisingLinks().query();
    }

    @Test
    public void testGetTopLinks() throws InterruptedException, IOException, HttpException {
        frontPage.getTopLinks().query();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    //                                                                                                                //
    //    Search                                                                                                      //
    //                                                                                                                //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

    @Test
    public void testGetSearch() throws InterruptedException, IOException, HttpException {
        frontPage.getSearch().setParameter("q", "penguins").query();
    }
}
