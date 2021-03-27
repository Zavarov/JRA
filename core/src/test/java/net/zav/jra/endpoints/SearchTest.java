package net.zav.jra.endpoints;

import net.zav.jra.AbstractTest;
import net.zav.jra.mock.ClientMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchTest extends AbstractTest {
    ClientMock client;

    @BeforeEach
    public void setUp() {
        TARGET_PATH = JSON_PATH.resolve("Search");
        client = new ClientMock();
    }

    //------------------------------------------------------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({"RedditDev, penguins"})
    public void testGetSearch(String subreddit, String query) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Search.getSearch(client, subreddit).setParameter("q", query).query()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(strings = {"penguins"})
    public void testGetSearch(String query) throws InterruptedException, IOException {
        client.json = readJson("Listing.json");
        assertThat(Search.getSearch(client).setParameter("q", query).query()).isNotNull();
    }
}
