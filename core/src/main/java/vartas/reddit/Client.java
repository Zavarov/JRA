package vartas.reddit;

import com.google.common.net.HttpHeaders;
import okhttp3.*;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.reddit.$factory.MessagingFactory;
import vartas.reddit.$json.JSONIdentity;
import vartas.reddit.$json.JSONPreferences;
import vartas.reddit.$json.JSONToken;
import vartas.reddit.exceptions.$factory.HttpExceptionFactory;
import vartas.reddit.exceptions.$factory.NotFoundExceptionFactory;
import vartas.reddit.exceptions.$factory.RateLimiterExceptionFactory;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;
import vartas.reddit.query.listings.*;
import vartas.reddit.query.search.QuerySearch;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.$factory.TrendingSubredditsFactory;
import vartas.reddit.types.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static vartas.reddit.query.listings.QueryComment.Sort;

@Nonnull
public abstract class Client extends ClientTOP{
    /**
     * The endpoint for requesting an access token.
     */
    @Nonnull
    protected static final String ACCESS_TOKEN = "https://www.reddit.com/api/v1/access_token";
    /**
     * The endpoint for revoking either an access or a refresh token.
     */
    @Nonnull
    protected static final String REVOKE_TOKEN = "https://www.reddit.com/api/v1/revoke_token";
    /**
     * The endpoint used for all requests that don't require OAuth2.<p>
     * Apparently the only endpoint this applies to is {@link Endpoint#GET_API_TRENDING_SUBREDDITS}.
     */
    @Nonnull
    protected static final String WWW = "www.reddit.com";
    /**
     * The protocol used for OAuth requests.
     */
    @Nonnull
    protected static final String HTTPS = "https";
    /**
     * The host accepting all OAuth2 requests.
     */
    @Nonnull
    protected static final String OAUTH2 = "oauth.reddit.com";
    /**
     * The application credentials is derived from the application id and secret.<p>
     * More explicitly, it is the base 64 encoding of "&lt;id&gt;:&lt;secret&gt;".
     */
    @Nonnull
    protected final String credentials;
    /**
     * A random UUID used to identify the hardware.
     */
    @Nonnull
    protected final String uuid = UUID.randomUUID().toString();
    /**
     * A rate limiter to handle sudden bursts of request.
     * Using OAuth2, Reddit only allows us to do up to 60 requests per minute. Violating this rule may get the
     * application banned.
     */
    @Nonnull
    protected final TimedSemaphore rateLimiter = new TimedSemaphore(1, TimeUnit.MINUTES, 60);
    /**
     * The logger for keeping track of the HTTP exchange between Reddit and application.
     */
    @Nonnull
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    /**
     * The actual HTTP client performing the requests.
     */
    @Nonnull
    protected final OkHttpClient http = new OkHttpClient();

    /**
     * Creates a new instance.
     * @param userAgent The user agent attached to every request.
     * @param id The application id.
     * @param secret The application "password".
     * @see <a href="https://github.com/reddit-archive/reddit/wiki/OAuth2">here</a>
     */
    @Nonnull
    public Client(@Nonnull UserAgent userAgent, @Nonnull String id, @Nonnull String secret){
        setUserAgent(userAgent);
        this.credentials = Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8));
    }


    /**
     * Returns a reference to the instance of this class.
     * @return {@code this}.
     */
    @Override
    @Nonnull
    public Client getRealThis() {
        return this;
    }

    /**
     * Creates the URL for an {@link Endpoint}. It specifies the address the corresponding request has to be directed
     * to.
     * @param host The host address of the URL. Usually {@link #OAUTH2}.
     * @param query Additional parameter appended to the URL. Those may contain additional information, such as the
     *              index when requesting stickied posts.
     * @param endpoint The endpoint targeted by the URL.
     * @param args Additional arguments for the {@link Endpoint}. E.g. a  {@link Subreddit} name.
     * @return The qualified address of the {@link Request}.
     */
    @Nonnull
    protected String buildUrl(
            @Nonnull String host,
            @Nonnull Map<?, ?> query,
            @Nonnull Endpoint endpoint,
            @Nonnull Object... args
    ){
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(HTTPS)
                .host(host);

        //Append the endpoint URL
        endpoint.getPath(args).forEach(builder::addPathSegment);

        //Append any additional parameter
        query.forEach((k,v) -> builder.addQueryParameter(Objects.toString(k), Objects.toString(v)));

        return builder.build().toString();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Get                                                                                                         //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//


    /**
     * Creates a GET {@link Request} for the corresponding {@link Endpoint}.
     * @param host The host address of the URL. Usually {@link #OAUTH2}.
     * @param query Additional parameter appended to the URL. Those may contain additional information, such as the
     *              index when requesting stickied posts.
     * @param endpoint The endpoint targeted by the URL.
     * @param args Additional arguments for the {@link Endpoint}. E.g. a  {@link Subreddit} name.
     * @return A GET {@link Request} for the specified {@link Endpoint}.
     */
    @Nonnull
    private Request buildGet(
            @Nonnull String host,
            @Nonnull Map<?, ?> query,
            @Nonnull Endpoint endpoint,
            @Nonnull Object... args
    ){
        assert isPresentToken();

        String url = buildUrl(host, query, endpoint, args);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.USER_AGENT, getUserAgent().toString());

        if(Objects.equals(host, OAUTH2))
            builder.addHeader(HttpHeaders.AUTHORIZATION, "bearer " + orElseThrowToken().getAccessToken());

        return builder.get().build();
    }

    /**
     * Executes a GET {@link Request}.
     * @param endpoint The endpoint targeted by the URL.
     * @param args Additional arguments for the {@link Endpoint}. E.g. a  {@link Subreddit} name.
     * @return The content of the {@link Response} body.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     */
    @Nonnull
    public String get(@Nonnull Endpoint endpoint, @Nonnull Object... args) throws InterruptedException, IOException, HttpException {
        return get(OAUTH2, endpoint, args);
    }

    /**
     * Executes a GET {@link Request}.
     * @param host The host address of the URL. Usually {@link #OAUTH2}.
     *              index when requesting stickied posts.
     * @param endpoint The endpoint targeted by the URL.
     * @param args Additional arguments for the {@link Endpoint}. E.g. a  {@link Subreddit} name.
     * @return The content of the {@link Response} body.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     */
    @Nonnull
    public String get(@Nonnull String host, @Nonnull Endpoint endpoint, @Nonnull Object... args) throws InterruptedException, IOException, HttpException {
        return get(host, Collections.emptyMap(), endpoint, args);
    }

    /**
     * Executes a GET {@link Request}.
     * @param query Additional parameter appended to the URL. Those may contain additional information, such as the
     *              index when requesting stickied posts.
     * @param endpoint The endpoint targeted by the URL.
     * @param args Additional arguments for the {@link Endpoint}. E.g. a  {@link Subreddit} name.
     * @return The content of the {@link Response} body.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     */
    @Nonnull
    public String get(@Nonnull Map<?, ?> query, @Nonnull Endpoint endpoint, @Nonnull Object... args) throws InterruptedException, IOException, HttpException {
        return get(OAUTH2, query, endpoint, args);
    }

    /**
     * Executes a GET {@link Request}.
     * @param host The host address of the URL. Usually {@link #OAUTH2}.
     * @param query Additional parameter appended to the URL. Those may contain additional information, such as the
     *              index when requesting stickied posts.
     * @param endpoint The endpoint targeted by the URL.
     * @param args Additional arguments for the {@link Endpoint}. E.g. a  {@link Subreddit} name.
     * @return The content of the {@link Response} body.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     */
    @Nonnull
    public String get(
            @Nonnull String host,
            @Nonnull Map<?, ?> query,
            @Nonnull Endpoint endpoint,
            @Nonnull Object... args
    ) throws InterruptedException, IOException, HttpException {
        Request request = buildGet(host, query, endpoint, args);
        Response response = request(request);
        ResponseBody body = response.body();

        assert body != null;

        return body.string();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Post                                                                                                        //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //protected JSONObject post(){
    //    throw new UnsupportedOperationException();
    //}

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    HTTP Requests                                                                                               //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * This function has two purposes. The primary purpose is to execute the provided {@link Request}. However it also
     * checks if the current access token is still valid. In case it expired, a new one will be fetched automatically.
     * @param request The request transmitted to Reddit.
     * @return The HTTP {@link Response} corresponding to the {@link Request}.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    protected synchronized Response request(Request request) throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        //Make sure that the token is still valid
        if(orElseThrowToken().isExpired())
            refresh();

        return execute(request);
    }

    /**
     * This method serves three purposes. The primary purpose is to execute the provided {@link Request}. In addition,
     * it also makes sure that all requests are made within the rate limit and, if necessary, waits until the next
     * {@link Request} can be made.<p>
     * It also checks if the {@link Request} was accepted and, upon error, throws the corresponding exception.
     * @param request The request transmitted to Reddit.
     * @return The HTTP {@link Response} corresponding to the {@link Request}.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    protected synchronized Response execute(Request request) throws InterruptedException, IOException, HttpException, RateLimiterException {
        //Wait if we're making too many requests at once
        rateLimiter.acquire();

        log.debug("--> {}", request);
        Response response = http.newCall(request).execute();
        log.debug("<-- {}", response);

        if(!response.isSuccessful()){
            switch(response.code()){
                //Not Found
                case 404:
                    throw NotFoundExceptionFactory.create(response.code(), response.message());
                //Too Many Requests
                case 429:
                    throw RateLimiterExceptionFactory.create();
                default:
                    throw HttpExceptionFactory.create(response.code(), response.message());
            }
        }

        return response;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Login                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Requests a new access and refresh token.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     */
    @Override
    public void login() throws InterruptedException, IOException, HttpException {
        login(Duration.PERMANENT);
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Logout                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//


    /**
     * Invalidates the access token and -if present- the refresh token. It is highly recommended to always invalidate
     * tokens once the they are no longer needed. Not only prevents the token to be misused, in case it gets leaked on
     * accident, but also minimizes the overhead since Reddit can safely delete the tokens from their database.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    public synchronized void logout() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        revokeRefreshToken();
        revokeAccessToken();
        setToken(Optional.empty());
    }

    /**
     * A helper method invalidating the refresh token, if present.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    private void revokeRefreshToken() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        if(orElseThrowToken().isEmptyRefreshToken())
            return;

        RequestBody body = new FormBody.Builder()
                .add("token", orElseThrowToken().orElseThrowRefreshToken())
                .add("token_type_hint", TokenType.REFRESH_TOKEN.name)
                .build();

        request(getAuthentication(REVOKE_TOKEN, body)).close();
    }

    /**
     * A helper method invalidating the access token.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    private void revokeAccessToken() throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        RequestBody body = new FormBody.Builder()
                .add("token", orElseThrowToken().getAccessToken())
                .add("token_type_hint", TokenType.ACCESS_TOKEN.toString())
                .build();

        request(getAuthentication(REVOKE_TOKEN, body)).close();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Refresh                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Requests a new access token.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    protected synchronized void refresh() throws IOException, HttpException, RateLimiterException, InterruptedException {
        assert isPresentToken() && orElseThrowToken().isPresentRefreshToken();

        RequestBody body = new FormBody.Builder()
                .add("grant_type", GrantType.REFRESH.toString())
                .add("refresh_token", orElseThrowToken().orElseThrowRefreshToken())
                .build();

        Response response = request(getAuthentication(ACCESS_TOKEN, body));
        ResponseBody data = response.body();

        assert data != null;

        //On February 15th 2021, the refresh response will contain a new refresh token.
        //Until then, we reuse the initial token.
        //@see https://redd.it/kvzaot
        String refreshToken = orElseThrowToken().orElseThrowRefreshToken();

        setToken(JSONToken.fromJson(new Token(), new JSONObject(data.string())));
        //#TODO Remove after February 15th 2021
        if(orElseThrowToken().isEmptyRefreshToken())
            orElseThrowToken().setRefreshToken(refreshToken);
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Account                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Returns the identity of the user.
     * @return An instance of the currently logged-in {@link User}.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public Identity getMe() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return JSONIdentity.fromJson(new Identity(), new JSONObject(get(Endpoint.GET_ME)));
    }

    /**
     * Returns a list of all users blocked by the currently logged-in {@link User}.
     * @return A list of users.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     * @deprecated This endpoint is no longer supported and will always return 404. In order to get all blocked users,
     * use {@link #getPreferencesBlocked()}
     * @see #getPreferencesBlocked()
     */
    @Override
    @Nonnull
    @Deprecated
    public List<User> getBlocked() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return Thing.from(new JSONObject(get(Endpoint.GET_ME_BLOCKED))).toUserList().getData();
    }

    /**
     * Returns a list of all users the currently logged-in {@link User} is friends with.
     * @return A list of users.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     * @deprecated While this endpoint is still supported, the result is equivalent to the one returned by
     * {@link #getPreferencesFriends()}. So in order to avoid using duplicate methods, we gently encourage everyone to
     * choose the other one instead.
     * @see #getPreferencesFriends()
     */
    @Override
    @Nonnull
    @Deprecated
    public List<User> getFriends() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return Thing.from(new JSONObject(get(Endpoint.GET_ME_FRIENDS))).toUserList().getData();
    }


    /**
     * Returns a breakdown of the {@link Karma} received the currently logged-in {@link User}.<p>
     * Each entry contains the number of {@link Link} and {@link Comment} in one of the subreddits the {@link User}
     * has been active at some point.
     * @return A list of {@link Karma} instances.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public List<Karma> getKarma() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return Thing.from(new JSONObject(get(Endpoint.GET_ME_KARMA))).toKarmaList().getData();
    }

    /**
     * Returns the preference settings of the currently logged-in {@link User}.<p>
     * Those settings contain information such as the default {@link Comment} {@link Sort}, whether nightmode is enabled
     * or whether they should be notified via email upon mentions or responses.
     * @return An instance of the user preferences.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public Preferences getPreferences() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return JSONPreferences.fromJson(new Preferences(), new JSONObject(get(Endpoint.GET_ME_PREFS)));
    }

    /**
     * Returns a list of all trophies that have been awarded to the currently logged-in {@link User}.
     * @return A list of trophies.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public List<Trophy> getTrophies() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return Thing.from(new JSONObject(get(Endpoint.GET_ME_TROPHIES))).toTrophyList().getData();
    }

    /**
     * Returns a list of all users blocked by the currently logged-in {@link User}.
     * @return A list of users.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public List<User> getPreferencesBlocked() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return Thing.from(new JSONObject(get(Endpoint.GET_PREFS_BLOCKED))).toUserList().getData();
    }

    /**
     * Returns a list of all users the currently logged-in {@link User} is friends with.
     * @return A list of users.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public List<User> getPreferencesFriends() throws InterruptedException, IOException, HttpException, RateLimiterException {
        JSONArray response = new JSONArray(get(Endpoint.GET_PREFS_FRIENDS));

        //I think that's a relic from when /prefs/friends/ used to return both friends and blocked users
        //I.e. The first entry contains all friends
        //And the second entry should always be empty.
        assert response.length() == 2;

        List<User> friends = Thing.from(response.getJSONObject(0)).toUserList().getData();
        List<User> blocked = Thing.from(response.getJSONObject(1)).toUserList().getData();

        assert blocked.isEmpty();

        return friends;
    }

    /**
     * Returns the message settings of the currently logged-in {@link User}.<p>
     * This configuration contains all users that have been either blacklisted or whitelisted.
     * @return An instance of the message settings.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public Messaging getPreferencesMessaging() throws InterruptedException, IOException, HttpException, RateLimiterException {
        JSONArray response = new JSONArray(get(Endpoint.GET_PREFS_MESSAGING));

        assert response.length() == 2;

        List<User> blocked = Thing.from(response.getJSONObject(0)).toUserList().getData();
        List<User> trusted = Thing.from(response.getJSONObject(1)).toUserList().getData();

        return MessagingFactory.create(blocked, trusted);
    }

    /**
     * Returns the whitelist of all users that are able to send messages to the currently logged-in {@link User}. Even
     * if private messages have been disabled.
     * @return A list of users.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public List<User> getPreferencesTrusted() throws InterruptedException, IOException, HttpException, RateLimiterException {
        return Thing.from(new JSONObject(get(Endpoint.GET_PREFS_TRUSTED))).toUserList().getData();
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Captcha                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Specifies whether this application needs to solve a captcha before executing API requests.
     * @return {@code true}, if a captcha is required.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     * @deprecated With OAuth2, the need for captchas is no longer exist.
     */
    @Override
    @Deprecated
    public boolean needsCaptcha() throws IOException, HttpException, RateLimiterException, InterruptedException {
        return Boolean.parseBoolean(get(Endpoint.GET_NEEDS_CAPTCHA));
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Return a list of trending subreddits, link to the {@link Comment} in {@code r/trendingsubreddits}, and the
     * {@link Comment} count of that {@link Link}.
     * @return An instance of the trending subreddits.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     * @throws RateLimiterException If too many requests are performed within short succession.
     */
    @Override
    @Nonnull
    public TrendingSubreddits getTrendingSubreddits() throws IOException, HttpException, RateLimiterException, InterruptedException {
        //Because for some reason trending subreddits don't require OAuth2 and return an 400 if used
        JSONObject response = new JSONObject(get(WWW, Endpoint.GET_API_TRENDING_SUBREDDITS));
        return TrendingSubredditsFactory.create(TrendingSubreddits::new, response);
    }

    /**
     * Links sorted by {code best} have the highest ration between upvotes and downvotes.
     * @return A query over the latest links.
     */
    @Override
    @Nonnull
    public QueryBest<Link> getBestLinks() {
        return new QueryBest<>(
                Thing::toLink,
                this,
                Endpoint.GET_BEST
        );
    }

    /**
     * Get a listing of links by fullname.
     * @param names A sequence of {@link Link} fullnames.
     * @return A query over the links with the specified fullnames.
     */
    @Override
    @Nonnull
    public QueryById getLinksById(@Nonnull String... names) {
        return new QueryById(this, names);
    }

    /**
     * Get the comment tree for a given Link article.<p>
     * If a base 36 id is supplied, it will be the (highlighted) focal point of the returned view and context will be
     * the number of parents shown.
     * @param article The base 36 id of a {@link Link}.
     * @return A query over all comments in the provided {@link Link}.
     */
    @Override
    @Nonnull
    public QueryComment getComments(String article) {
        return new QueryComment(this, Endpoint.GET_COMMENTS, article);
    }

    /**
     * Links sorted by {code controversial} have recently received a high amount of upvotes <b>and</b> downvotes.
     * @return A query over the latest links.
     */
    @Override
    @Nonnull
    public QueryControversial<Link> getControversialLinks() {
        return new QueryControversial<>(
                Thing::toLink,
                this,
                Endpoint.GET_CONTROVERSIAL
        );
    }

    /**
     * Return a list of other links of the same URL. This happens, for example, if a link is cross-posted to another
     * {@link Subreddit}.
     * @param article The base 36 id of a {@link Link}.
     * @return A query over all links identical to the one provided.
     */
    @Override
    @Nonnull
    public QueryDuplicates getDuplicates(String article) {
        return new QueryDuplicates(this, article);
    }

    /**
     * Links sorted by {code hot} have recently received a high amount of upvotes and/or comments.
     * @return A query over the latest links.
     */
    @Override
    @Nonnull
    public QueryHot<Link> getHotLinks() {
        return new QueryHot<>(
                Thing::toLink,
                this,
                Endpoint.GET_HOT
        );
    }

    /**
     * Links sorted by {code new} are ordered according to their submission date.
     * @return A query over the latest links.
     */
    @Override
    @Nonnull
    public QueryNew<Link> getNewLinks() {
        return new QueryNew<>(
                Thing::toLink,
                this,
                Endpoint.GET_NEW
        );
    }

    /**
     * The {@link Link} is chosen randomly from the {@code top} links.
     * @return A query over a single {@link Link}.
     * @see #getTopLinks()
     */
    @Override
    @Nonnull
    public QueryRandom getRandomLink() {
        return new QueryRandom(this, Endpoint.GET_RANDOM);
    }

    /**
     * Links sorted by {code rising} have received a high amount of upvotes and/or comments right now.
     * @return A query over the latest links.
     */
    @Override
    @Nonnull
    public QueryRising<Link> getRisingLinks() {
        return new QueryRising<>(
                Thing::toLink,
                this,
                Endpoint.GET_RISING
        );
    }

    /**
     * Links sorted by {code top} have received a high amount of upvotes over an unspecified period of time.
     * @return A query over the latest links.
     */
    @Override
    @Nonnull
    public QueryTop<Link> getTopLinks() {
        return new QueryTop<>(
                Thing::toLink,
                this,
                Endpoint.GET_TOP
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Search                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Provides access to the search function for links.
     * @return A search query over links.
     */
    @Override
    @Nonnull
    public QuerySearch getSearch() {
        return new QuerySearch(this, Endpoint.GET_SEARCH);
    }


    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Utility classes                                                                                             //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public Subreddit getSubreddit(String name) throws HttpException, IOException, InterruptedException {
        Thing thing = ThingFactory.create(Thing::new, new JSONObject(get(Endpoint.GET_SUBREDDIT_ABOUT, name)));

        //TODO Check
        //In case a subreddit with the specified name doesn't exist, the return Thing may be arbitrary
        if(Thing.Kind.Subreddit.matches(thing.getKind()))
            return thing.toSubreddit(this);
        else
            throw NotFoundExceptionFactory.create(HttpURLConnection.HTTP_NOT_FOUND, "A subreddit with this name doesn't exist");

    }

    protected Request getAuthentication(String url, RequestBody body){
        return new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.AUTHORIZATION, "Basic "+credentials)
                .addHeader(HttpHeaders.USER_AGENT, getUserAgent().toString())
                .post(body)
                .build();
    }

    public enum Duration {
        PERMANENT("permanent"),
        TEMPORARY("temporary");

        private final String name;
        Duration(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    }

    public enum GrantType {
        USERLESS("https://oauth.reddit.com/grants/installed_client"),
        PASSWORD("password"),
        CLIENT("client_credentials"),
        REFRESH("refresh_token");

        private final String value;
        GrantType(String value){
            this.value = value;
        }

        @Override
        public String toString(){
            return value;
        }
    }

    /**
     * The token type is used to inform Reddit about the kind of token that is transmitted. It is required when
     * refreshing the access token or invalidating already existing tokens.
     */
    public enum TokenType {
        /**
         * The access token is required to authenticate the application when using the OAuth2 endpoints.
         */
        @Nonnull
        ACCESS_TOKEN("access_token"),
        /**
         * The refresh token is required when requesting a new access token, once the previous one expired.
         */
        @Nonnull
        REFRESH_TOKEN("refresh_token");

        /**
         * The type name.
         */
        @Nonnull
        public final String name;

        /**
         * Assigns each token type a name. The name matches the one used by Reddit.
         * @param name The type name.
         */
        @Nonnull
        TokenType(@Nonnull String name){
            this.name = name;
        }
    }

    /**
     * A list containing all supported Reddit scopes.
     * See https://www.reddit.com/api/v1/scopes for more.
     */
    @Nonnull
    public enum Scope {
        /**
         * Spend my reddit gold creddits on giving gold to other users.
         */
        @Nonnull
        CREDDITS("creddits"),
        /**
         * Add/remove users to approved user lists and ban/unban or mute/unmute users from subreddits I moderate.
         */
        @Nonnull
        MODCONTRIBUTORS("modcontributors"),
        /**
         * Access and manage modmail via mod.reddit.com.
         */
        @Nonnull
        MODMAIL("modmail"),
        /**
         * Manage the configuration, sidebar, and CSS of subreddits I moderate.
         */
        @Nonnull
        MODCONFIG("modconfig"),
        /**
         * Manage my subreddit subscriptions. Manage \"friends\" - users whose content I follow.
         */
        @Nonnull
        SUBSCRIBE("subscribe"),
        /**
         * Edit structured styles for a subreddit I moderate.
         */
        @Nonnull
        STRUCTUREDSTYLES("structuredstyles"),
        /**
         * Submit and change my votes on comments and submissions.
         */
        @Nonnull
        VOTE("vote"),
        /**
         * Edit wiki pages on my behalf.
         */
        @Nonnull
        WIKIEDIT("wikiedit"),
        /**
         * Access the list of subreddits I moderate, contribute to, and subscribe to.
         */
        @Nonnull
        MYSUBREDDITS("mysubreddits"),
        /**
         * Submit links and comments from my account.
         */
        @Nonnull
        SUBMIT("submit"),
        /**
         * Access the moderation log in subreddits I moderate.
         */
        @Nonnull
        MODLOG("modlog"),
        /**
         * Approve, remove, mark nsfw, and distinguish content in subreddits I moderate.
         */
        @Nonnull
        MODPOST("modpost"),
        /**
         * Manage and assign flair in subreddits I moderate.
         */
        @Nonnull
        MODFLAIR("modflair"),
        /**
         * Save and unsave comments and submissions.
         */
        @Nonnull
        SAVE("save"),
        /**
         * Invite or remove other moderators from subreddits I moderate.
         */
        @Nonnull
        MODOTHERS("modothers"),
        /**
         * Access posts and comments through my account.
         */
        @Nonnull
        READ("read"),
        /**
         * Access my inbox and send private messages to other users.
         */
        @Nonnull
        PRIVATEMESSAGES("privatemessages"),
        /**
         * Report content for rules violations. Hide &amp; show individual submissions.
         */
        @Nonnull
        REPORT("report"),
        /**
         * Access my reddit username and signup date.
         */
        @Nonnull
        IDENTITY("identity"),
        /**
         * Manage settings and contributors of live threads I contribute to.
         */
        @Nonnull
        LIVEMANAGE("livemanage"),
        /**
         * Update preferences and related account information. Will not have access to your email or password.
         */
        @Nonnull
        ACCOUNT("account"),
        /**
         * Access traffic stats in subreddits I moderate.
         */
        @Nonnull
        MODTRAFFIC("modtraffic"),
        /**
         * Read wiki pages through my account.
         */
        @Nonnull
        WIKIREAD("wikiread"),
        /**
         * Edit and delete my comments and submissions.
         */
        @Nonnull
        EDIT("edit"),
        /**
         * Change editors and visibility of wiki pages in subreddits I moderate.
         */
        @Nonnull
        MODWIKI("modwiki"),
        /**
         * Accept invitations to moderate a subreddit. Remove myself as a moderator or contributor of subreddits I moderate
         * or contribute to.
         */
        @Nonnull
        MODSELF("modself"),
        /**
         * Access my voting history and comments or submissions I've saved or hidden.
         */
        @Nonnull
        HISTORY("history"),
        /**
         * Select my subreddit flair. Change link flair on my submissions.
         */
        @Nonnull
        FLAIR("flair"),

        /**
         * A wildcard indicating that all scopes are used.
         */
        @Nonnull
        ANY("*");

        /**
         * The scope name.
         */
        @Nonnull
        public final String name;

        /**
         * Assigns each scope a name. The name matches the one used by Reddit.
         * @param name The name of the scope.
         */
        @Nonnull
        Scope(@Nonnull String name){
            this.name = name;
        }
    }
}
