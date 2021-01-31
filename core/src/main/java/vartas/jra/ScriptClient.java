package vartas.jra;

import vartas.jra.$json.JSONToken;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.RateLimiterException;
import vartas.jra.http.APIAuthentication;

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
            @Nonnull String password,
            @Nonnull Scope... scope
    ){
        super(userAgent, id, secret, scope);
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
        APIAuthentication request = new APIAuthentication.Builder(ACCESS_TOKEN, credentials, this)
                .addParameter("grant_type", GrantType.PASSWORD)
                .addParameter("username", account)
                .addParameter("password", password)
                .addParameter("duration", duration)
                .addScope(scope)
                .build();

        String response = request.post();

        setToken(JSONToken.fromJson(new Token(), response));
    }
}
