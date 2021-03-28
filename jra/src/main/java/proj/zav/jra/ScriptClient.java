package proj.zav.jra;

import proj.zav.jra._factory.ClientFactory;
import proj.zav.jra._json.JSONToken;
import proj.zav.jra.http.APIAuthentication;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
        ClientFactory.create(() -> this, userAgent, Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8)));
        this.account = account;
        this.password = password;
    }
    /**
     * Requests a new access token.<p>
     * It seems like Reddit ignore the value of {@code duration} and never returns a refresh token for scripts. Once the
     * access token requires, the program has to login again.
     * @param duration The lifetime of the token.
     * @throws IOException If the request couldn't be completed.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     */
    @Override
    public synchronized void login(@Nonnull Duration duration) throws IOException, InterruptedException {
        APIAuthentication request = new APIAuthentication.Builder(ACCESS_TOKEN, getCredentials(), this)
                .addParameter("grant_type", GrantType.PASSWORD)
                .addParameter("username", account)
                .addParameter("password", password)
                .addParameter("duration", duration)
                .build();

        String response = request.post();

        setToken(JSONToken.fromJson(new Token(), response));
    }
}
