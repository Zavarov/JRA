package vartas.jra;

import okhttp3.*;
import org.json.JSONObject;
import vartas.jra.$json.JSONToken;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.RateLimiterException;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Creates a new instance of the client without user context.<p>
 * The application can still access subreddits and fetch both links and comments. However, it is not able
 * to retrieve user-specific information or submit anything.
 */
@Nonnull
public class UserlessClient extends Client{
    /**
     * Creates a new user-less client.
     * @param userAgent The user agent attached to every request.
     * @param id The application id.
     * @param secret The application "password".
     * @see <a href="https://github.com/reddit-archive/reddit/wiki/OAuth2">here</a>
     */
    @Nonnull
    public UserlessClient(
            @Nonnull UserAgent userAgent,
            @Nonnull String id,
            @Nonnull String secret
    ){
        super(userAgent, id, secret);
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Login                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Requests a new access token.<p>
     * Depending on the value of {@code duration}, Reddit will also return a refresh token with which the client is able
     * to retrieve a new access token once the old one expired.
     * @param duration The lifetime of the token.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are made within a short amount of time.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     */
    @Override
    public synchronized void login(@Nonnull Duration duration) throws IOException, HttpException, RateLimiterException, InterruptedException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.USERLESS.toString())
                .add("device_id", uuid)
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
