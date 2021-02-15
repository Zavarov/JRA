package vartas.jra.endpoints;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import vartas.jra.AbstractClient;
import vartas.jra.AbstractTest;
import vartas.jra.exceptions.HttpException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends AbstractTest {
    static AbstractClient client;

    @BeforeAll
    public static void setUpAll() throws IOException, HttpException, InterruptedException {
        client = getScript(ListingsTest.class.getSimpleName());
        client.login(AbstractClient.Duration.TEMPORARY);
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException, HttpException {
        client.logout();
    }

    //------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({"RedditDev, penguins"})
    public void testGetSearch(String subreddit, String query) throws InterruptedException, IOException, HttpException {
        assertThat(Search.getSearch(client, subreddit).setParameter("q", query).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"penguins"})
    public void testGetSearch(String query) throws InterruptedException, IOException, HttpException {
        assertThat(Search.getSearch(client).setParameter("q", query).query()).isNotNull();
    }
}
