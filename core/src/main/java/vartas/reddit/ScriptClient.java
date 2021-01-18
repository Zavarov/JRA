package vartas.reddit;

import okhttp3.*;
import org.json.JSONObject;
import vartas.reddit.$json.JSONToken;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;

import javax.annotation.Nonnull;
import java.io.IOException;

public class ScriptClient extends Client{
    @Nonnull
    private final String account;
    @Nonnull
    private final String password;

    public ScriptClient(
            @Nonnull String account,
            @Nonnull String password,
            @Nonnull String platform,
            @Nonnull String author,
            @Nonnull String version,
            @Nonnull String id,
            @Nonnull String secret
    ){
        super(platform, version, author, id, secret);
        this.account = account;
        this.password = password;
    }

    @Override
    public synchronized void login(Duration duration) throws IOException, HttpException, RateLimiterException, InterruptedException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.PASSWORD.toString())
                .add("username", account)
                .add("password", password)
                .build();

        Request request = getAuthentication(ACCESS_TOKEN, body);
        //Call execute directly to avoid checking the non-existent token for validity
        Response response = execute(request);
        ResponseBody data = response.body();

        //Call.execute() supposedly always returns a non-null response with non-null body
        assert data != null;

        //data.string() automatically closes the response
        setToken(JSONToken.fromJson(new Token(), new JSONObject(data.string())));
    }
}
