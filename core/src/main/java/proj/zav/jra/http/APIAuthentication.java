package proj.zav.jra.http;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import okhttp3.*;
import proj.zav.jra.AbstractClient;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.*;

/**
 * The authentication is used to register or deregister this application with the Reddit. The access token gained during
 * the registration is required for any further API requests. While it is not required to explicitly deregister the
 * access when it is no longer required, it is good practice to always do this when terminating the program.<p>

 * This prevents third parties from misuse the token should it be accidentally leaked but also informs Reddit that it
 * can be safely removed from their database.
 */
@Nonnull
public class APIAuthentication {
    /**
     * The endpoint used for the request. May be either {@code https://www.reddit.com/api/v1/access_token} when
     * requesting a new token or {@code https://www.reddit.com/api/v1/revoke_token} when revoking a valid token.
     */
    @Nonnull
    private final String url;
    /**
     * The credentials are derived from the application {@code id} and {@code secret} and are used to identify the
     * application.
     */
    @Nonnull
    private final String credentials;
    /**
     * The client performing the request.
     */
    @Nonnull
    private final AbstractClient client;
    /**
     * A collection of additional parameter attached to the request.<p>
     *
     * For example the scope or duration of the token.
     */
    @Nonnull
    private final Map<String, String> params = new HashMap<>();
    /**
     * A collection of the scope the application is allowed to operate in.<p>
     *
     * The scope is used to restrict the number of endpoints the application to reduce the damage an application can do,
     * for example when the access token gets revealed by accident.<p>
     *
     * Not specifying a scope is treated as giving the application full access.
     */
    @Nonnull
    private final Collection<AbstractClient.Scope> scopes = new HashSet<>();

    /**
     * Use the {@link Builder} to create a new instance of this class.
     * @param url The endpoint used for the request.
     * @param credentials The application credentials.
     * @param client The client performing the request.
     */
    @Nonnull
    private APIAuthentication(@Nonnull String url, @Nonnull String credentials, @Nonnull AbstractClient client){
        this.url = url;
        this.credentials = credentials;
        this.client = client;
    }

    /**
     * The request body consists of all provided parameters as well as the application scope.
     * @return a new request body.
     */
    @Nonnull
    private RequestBody body(){
        FormBody.Builder builder = new FormBody.Builder();

        params.forEach(builder::add);

        if(!scopes.isEmpty())
            builder.add("scope", Joiner.on(',').join(scopes));

        return builder.build();
    }

    /**
     * Performs the API request and returns the response.<p>
     * In case a fresh token was requested, it is most likely a JSON object containing the new access token.
     * @return The API response.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If the request couldn't be completed.
     */
    @Nonnull
    public String post() throws InterruptedException, IOException {
         Request request = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.AUTHORIZATION, "Basic "+credentials)
                .addHeader(HttpHeaders.USER_AGENT, client.getUserAgent().toString())
                .post(body())
                .build();

        //Call execute directly to avoid checking the non-existent token for validity
        Response response = client.execute(request);

        ResponseBody data = response.body();

        //Call.execute() supposedly always returns a non-null response with non-null body
        assert data != null;

        //Close the response
        return data.string();
    }

    /**
     * This class implements the Builder pattern for
     * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder Pattern</a>
     */
    @Nonnull
    public static class Builder{
        /**
         * The underlying instance.
         */
        @Nonnull
        private final APIAuthentication source;

        /**
         * Creates a new builder instance.
         * @param url The endpoint used for the request.
         * @param credentials The application credentials.
         * @param client The client performing the request.
         */
        @Nonnull
        public Builder(@Nonnull String url, @Nonnull String credentials, @Nonnull AbstractClient client){
            source = new APIAuthentication(url, credentials, client);
        }

        /**
         * Attaches a new parameter to the request.<p>
         *
         * A parameter consists of a name and a value which is attached to the request body. For example
         * {@code client_id} and {@code "foo"} in the following case:
         * {@code https://www.reddit.com/api/v1/authorize?client_id="foo"}<p>
         *
         * In case the name is used multiple times, only the latest entry is considered.
         *
         * @param key The parameter name.
         * @param value The parameter value.
         * @return This builder instance.
         */
        @Nonnull
        public Builder addParameter(@Nonnull String key, @Nonnull Object value){
            source.params.put(key, value.toString());
            return this;
        }

        /**
         * Attaches the scopes the application is allowed to use with the requested access tokens
         *
         * In case a scope is added multiple times, it is only counted once.
         *
         * @param scope an array of scopes the application is allowed to use.
         * @return This builder instance.
         */
        @Nonnull
        public Builder addScope(@Nonnull AbstractClient.Scope... scope){
            source.scopes.addAll(Arrays.asList(scope));
            return this;
        }

        /**
         * Constructs the token request.
         * @return An instance of {@link APIAuthentication}.
         */
        @Nonnull
        public APIAuthentication build(){
            return source;
        }
    }
}
