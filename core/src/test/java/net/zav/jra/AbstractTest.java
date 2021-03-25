package net.zav.jra;

import net.zav.jra._factory.UserAgentFactory;
import net.zav.jra.mock.ClientMock;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class AbstractTest {
    protected static JSONObject getConfig() throws IOException {
        String content = Files.readString(Paths.get("src", "test", "resources", "config.json"));
        return new JSONObject(content);
    }

    protected static AbstractClient getScript(String version) throws IOException {
        JSONObject config = getConfig();

        String platform = config.getString("platform");
        String author = config.getString("name");
        String id = config.getString("id");
        String secret = config.getString("secret");
        String account = config.getString("account");
        String password = config.getString("password");
        UserAgent userAgent = UserAgentFactory.create(platform, AbstractTest.class.getPackageName(), version, author);

        return new ClientMock(userAgent, id, secret, account, password);
    }
}
