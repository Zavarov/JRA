package vartas.jra;

import okhttp3.*;
import org.json.JSONObject;
import vartas.jra.$json.JSONToken;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.RateLimiterException;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * The simplest type of application.<p>
 * A script is a simple program acts on behalf of a user and therefore needs their credentials.
 */
@Nonnull
public class ScriptClient extends Client{
    /**
     * The user name the program is acting on behalf on.
     */
    @Nonnull
    private final String account;
    /**
     * The password of the user required for authorization.
     */
    @Nonnull
    private final String password;

    /**
     * Creates a new script.
     * @param userAgent The user agent attached to every request.
     * @param id The application id.
     * @param secret The application "password".
     * @param account The user name on which behalf this program is acting.
     * @param password The password necessary for logging into the user account.
     * @see <a href="https://github.com/reddit-archive/reddit/wiki/OAuth2">here</a>
     */
    @Nonnull
    public ScriptClient(
            @Nonnull UserAgent userAgent,
            @Nonnull String id,
            @Nonnull String secret,
            @Nonnull String account,
            @Nonnull String password
    ){
        super(userAgent, id, secret);
        this.account = account;
        this.password = password;
    }
    /**
     * Requests a new access token.<p>
     * It seems like Reddit ignore the value of {@code duration} and never returns a refresh token for scripts. Once the
     * access token requires, the program has to login again.
     * @param duration The lifetime of the token.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are made within a short amount of time.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     */
    @Override
    public synchronized void login(@Nonnull Duration duration) throws IOException, HttpException, RateLimiterException, InterruptedException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.PASSWORD.toString())
                .add("username", account)
                .add("password", password)
                .add("duration", duration.toString())
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
