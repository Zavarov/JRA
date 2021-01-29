package vartas.jra;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.jra.$factory.UserAgentFactory;
import vartas.jra.$json.JSONComment;
import vartas.jra.$json.JSONLink;
import vartas.jra.types.$json.JSONKarma;
import vartas.jra.types.$json.JSONTrophy;
import vartas.jra.types.$json.JSONUser;
import vartas.jra.types.Karma;
import vartas.jra.types.Thing;
import vartas.jra.types.Trophy;
import vartas.jra.types.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AbstractTest {
    protected static final Logger log = LoggerFactory.getLogger(AbstractTest.class);
    protected static final String SUBREDDIT_NAME = "RedditDev";

    protected static JSONObject getConfig() throws IOException {
        String content = Files.readString(Paths.get("src", "test", "resources", "config.json"));
        return new JSONObject(content);
    }

    protected static Client getScript(String version) throws IOException {
        JSONObject config = getConfig();

        String platform = config.getString("platform");
        String author = config.getString("name");
        String id = config.getString("id");
        String secret = config.getString("secret");
        String account = config.getString("account");
        String password = config.getString("password");
        UserAgent userAgent = UserAgentFactory.create(platform, AbstractTest.class.getPackageName(), version, author);

        return new ScriptClient(userAgent, id, secret, account, password);
    }

    protected static Client getUserless(String version) throws IOException {
        JSONObject config = getConfig();

        String platform = config.getString("platform");
        String author = config.getString("name");
        String id = config.getString("id");
        String secret = config.getString("secret");
        UserAgent userAgent = UserAgentFactory.create(platform, AbstractTest.class.getPackageName(), version, author);

        return new UserlessClient(userAgent, id, secret);
    }

    protected static void check(Thing thing){
        switch(thing.getKind()){
            case "t1":
                check(thing.toComment());
                break;
            case "t3":
                check(thing.toLink());
                break;
            default:
                throw new IllegalArgumentException(thing.getKind());
        }
    }

    protected static void check(Trophy trophy){
        log.info(JSONTrophy.toJson(trophy, new JSONObject()).toString(2));
    }

    protected static void check(Karma karma){
        log.info(JSONKarma.toJson(karma, new JSONObject()).toString(2));
    }

    protected static void check(User user){
        log.info(JSONUser.toJson(user, new JSONObject()).toString(2));
    }

    protected static void check(Link link){
        log.info(JSONLink.toJson(link, new JSONObject()).toString(2));
    }

    protected static void check(Comment comment){
        log.info(JSONComment.toJson(comment, new JSONObject()).toString(2));
    }
}
