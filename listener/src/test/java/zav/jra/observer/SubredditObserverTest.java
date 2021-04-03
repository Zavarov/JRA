package zav.jra.observer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import zav.jra.AbstractClient;
import zav.jra.AbstractTest;
import zav.jra.Client;
import zav.jra.Subreddit;
import zav.jra.mock.SubredditListenerMock;

import java.io.IOException;

public class SubredditObserverTest extends AbstractTest {
    private static Client client;
    private static Subreddit subreddit;
    private SubredditObserver observer;
    @BeforeAll
    public static void setUpAll() throws IOException, InterruptedException {
        client = getScript("SubredditObserverTest");
        client.login(AbstractClient.Duration.PERMANENT);
        subreddit = client.getSubreddit("RedditDev");
    }

    @AfterAll
    public static void tearDownAll() throws InterruptedException, IOException {
        client.logout();
    }

    @BeforeEach
    public void setUp(){
        observer = new SubredditObserver(subreddit);
        observer.addListener(new SubredditListenerMock());
    }

    //@Test
    public void test() throws InterruptedException, IOException {
        while(true){
            observer.notifyAllListener();
            Thread.sleep(60000);
        }
    }
}
