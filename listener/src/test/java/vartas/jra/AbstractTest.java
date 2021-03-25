package vartas.jra;

import org.json.JSONObject;
import vartas.jra._factory.UserAgentFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractTest {

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
        UserAgent userAgent = UserAgentFactory.create(platform, AbstractTest.class.getPackageName(), version, author);

        return new UserlessClient(userAgent, id, secret);
    }
}
