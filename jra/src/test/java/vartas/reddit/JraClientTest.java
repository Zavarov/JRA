package vartas.reddit;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JraClientTest {
    static String author;
    static String version;
    static String id;
    static String secret;

    @BeforeAll
    public static void setUpAll() throws IOException {
        String content = Files.readString(Paths.get("src", "test", "resources", "config.json"));
        JSONObject config = new JSONObject(content);

        author = config.getString("name");
        version = config.getString("version");
        id = config.getString("clientId");
        secret = config.getString("secret");
    }

    @Test
    public void test() throws IOException, HttpException, RateLimiterException, InterruptedException {
        JraClient client = new UserlessClient("linux",author,version,id,secret);
        client.login();
        try {
            Subreddit subreddit = client.getSubreddit("redditdev").orElseThrow();
            System.out.println(subreddit.getSource().toString(2));
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            client.logout();
        }
    }
}
