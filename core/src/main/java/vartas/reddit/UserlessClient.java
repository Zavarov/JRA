package vartas.reddit;

import okhttp3.*;
import org.json.JSONObject;
import vartas.reddit.$json.JSONToken;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;

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
     * @param secret The "password".
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
