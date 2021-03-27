package proj.zav.jra.observer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import proj.zav.jra.AbstractClient;
import proj.zav.jra.AbstractTest;
import proj.zav.jra.Client;
import proj.zav.jra.Subreddit;
import proj.zav.jra.mock.SubredditListenerMock;

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
    public void test() throws InterruptedException {
        while(true){
            observer.notifyAllListener();
            Thread.sleep(60000);
        }
    }
}
