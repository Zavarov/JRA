package zav.jra;

import zav.jra._factory.ClientFactory;
import zav.jra._json.JSONToken;
import zav.jra.http.APIAuthentication;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
        ClientFactory.create(() -> this, userAgent, Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8)));
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
     * @throws IOException If the request couldn't be completed.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     */
    @Override
    public synchronized void login(@Nonnull Duration duration) throws IOException, InterruptedException {
        APIAuthentication request = new APIAuthentication.Builder(ACCESS_TOKEN, getCredentials(), this)
                .addParameter("grant_type", GrantType.USERLESS)
                .addParameter("device_id", uuid)
                .addParameter("duration", duration)
                .build();

        String response = request.post();

        setToken(JSONToken.fromJson(new Token(), response));
    }
}
