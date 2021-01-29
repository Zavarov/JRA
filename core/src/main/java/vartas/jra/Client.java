package vartas.jra;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import okhttp3.*;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.jra.$factory.DuplicateFactory;
import vartas.jra.$factory.SubmissionFactory;
import vartas.jra.$json.JSONToken;
import vartas.jra.exceptions.$factory.HttpExceptionFactory;
import vartas.jra.exceptions.$factory.NotFoundExceptionFactory;
import vartas.jra.exceptions.$factory.RateLimiterExceptionFactory;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.RateLimiterException;
import vartas.jra.http.APIRequest;
import vartas.jra.query.QueryMany;
import vartas.jra.query.QueryOne;
import vartas.jra.types.$factory.MessagingFactory;
import vartas.jra.types.$factory.ThingFactory;
import vartas.jra.types.$json.JSONIdentity;
import vartas.jra.types.$json.JSONPreferences;
import vartas.jra.types.$json.JSONTrendingSubreddits;
import vartas.jra.types.*;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public synchronized Response request(Request request) throws IOException, HttpException, InterruptedException, RateLimiterException {
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
     * Returns the {@link Identity} of the user.
     * @return An instance of the {@link Identity} of the currently logged-in user.
     */
    @Override
    @Nonnull
    public QueryOne<Identity> getMe() {
        return new QueryOne<>(
                source -> JSONIdentity.fromJson(new Identity(), source),
                this,
                Endpoint.GET_ME
        );
    }

    /**
     * Returns a list of all users blocked by the currently logged-in user.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of users.
     * @deprecated This endpoint is no longer supported and will always return 404. In order to get all blocked users,
     * use {@link #getPreferencesBlocked()}
     * @see #getPreferencesBlocked()
     */
    @Override
    @Nonnull
    @Deprecated
    public QueryOne<UserList> getBlocked() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(),
                this,
                Endpoint.GET_ME_BLOCKED
        );
    }

    /**
     * Returns a list of all users the currently logged-in {@link User} is friends with.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of users.
     * @deprecated While this endpoint is still supported, the result is equivalent to the one returned by
     * {@link #getPreferencesFriends()}. So in order to avoid using duplicate methods, we gently encourage everyone to
     * choose the other one instead.
     * @see #getPreferencesFriends()
     */
    @Override
    @Nonnull
    @Deprecated
    public QueryOne<UserList> getFriends() {
        return new QueryOne<>(
            source -> Thing.from(source).toUserList(),
            this,
            Endpoint.GET_ME_FRIENDS
        );
    }


    /**
     * Returns a breakdown of the {@link Karma} received the currently logged-in {@link User}.<p>
     * Each entry contains the number of {@link Link} and {@link Comment} in one of the subreddits the {@link User}
     * has been active at some point.
     * @return A list of {@link Karma} instances.
     */
    @Override
    @Nonnull
    public QueryOne<KarmaList> getKarma() {
        return new QueryOne<>(
                source -> Thing.from(source).toKarmaList(),
                this,
                Endpoint.GET_ME_KARMA
        );
    }

    /**
     * Returns the preference settings of the currently logged-in {@link User}.<p>
     * Those settings contain information such as the default {@link Comment} sort, whether nightmode is enabled or
     * whether they should be notified via email upon mentions or responses.
     * @return An instance of the user preferences.
     */
    @Override
    @Nonnull
    public QueryOne<Preferences> getPreferences() {
        return new QueryOne<>(
                source -> JSONPreferences.fromJson(new Preferences(), source),
                this,
                Endpoint.GET_ME_PREFS
        );
    }

    /**
     * Returns a list of all trophies that have been awarded to the currently logged-in {@link User}.
     * @return A list of trophies.
     */
    @Override
    @Nonnull
    public QueryOne<TrophyList> getTrophies() {
        return new QueryOne<>(
                source -> Thing.from(source).toTrophyList(),
                this,
                Endpoint.GET_ME_TROPHIES
        );
    }

    /**
     * Returns a list of all users blocked by the currently logged-in {@link User}.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of users.
     */
    @Override
    @Nonnull
    public QueryOne<UserList> getPreferencesBlocked() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(),
                this,
                Endpoint.GET_PREFS_BLOCKED
        );
    }

    /**
     * Returns a list of all users the currently logged-in {@link User} is friends with.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of users.
     */
    @Override
    @Nonnull
    public QueryOne<UserList> getPreferencesFriends() {
        //TODO Find a better way to extract friends
        Function<String, UserList> mapper = source -> {
            JSONArray response = new JSONArray(source);

            //I think that's a relic from when /prefs/friends/ used to return both friends and blocked users
            //I.e. The first entry contains all friends
            //And the second entry should always be empty.
            assert response.length() == 2;

            UserList friends = Thing.from(response.getJSONObject(0)).toUserList();
            UserList blocked = Thing.from(response.getJSONObject(1)).toUserList();

            assert blocked.isEmptyData();

            return friends;
        };

        return new QueryOne<>(
                mapper,
                this,
                Endpoint.GET_PREFS_FRIENDS
        );
    }

    /**
     * Returns the message settings of the currently logged-in {@link User}.<p>
     * This configuration contains all users that have been either blacklisted or whitelisted.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return An instance of the message settings.
     */
    @Override
    @Nonnull
    public QueryOne<Messaging> getPreferencesMessaging() {
        //TODO Use JSONMessaging instead
        Function<String, Messaging> mapper = source -> {
            JSONArray response = new JSONArray(source);

            assert response.length() == 2;

            List<User> blocked = Thing.from(response.getJSONObject(0)).toUserList().getData();
            List<User> trusted = Thing.from(response.getJSONObject(1)).toUserList().getData();

            return MessagingFactory.create(blocked, trusted);
        };

        return new QueryOne<>(
                mapper,
                this,
                Endpoint.GET_PREFS_MESSAGING
        );
    }

    /**
     * Returns the whitelist of all users that are able to send messages to the currently logged-in {@link User}. Even
     * if private messages have been disabled.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of users.
     */
    @Override
    @Nonnull
    public QueryOne<UserList> getPreferencesTrusted() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(),
                this,
                Endpoint.GET_PREFS_TRUSTED
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Captcha                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Specifies whether this application needs to solve a captcha before executing API requests.
     * @return {@code true}, if a captcha is required.
     * @deprecated With OAuth2, the need for captchas is no longer exist.
     */
    @Override
    @Deprecated
    public QueryOne<Boolean> needsCaptcha() {
        return new QueryOne<>(
                Boolean::parseBoolean,
                this,
                Endpoint.GET_NEEDS_CAPTCHA
        );
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
     */
    @Override
    @Nonnull
    public TrendingSubreddits getTrendingSubreddits() throws IOException, HttpException, RateLimiterException, InterruptedException {
        String source = new APIRequest.Builder(this).setHost(APIRequest.WWW).setEndpoint(Endpoint.GET_API_TRENDING_SUBREDDITS).build().get();
        return JSONTrendingSubreddits.fromJson(new TrendingSubreddits(), source);
    }

    /**
     * Links sorted by {code best} have the highest ration between upvotes and downvotes.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getBestLinks() {
        return new QueryMany<>(
                Thing::toLink,
                this,
                Endpoint.GET_BEST
        );
    }

    /**
     * Get a listing of links by fullname.
     * @param names A sequence of {@link Link} fullnames.
     * @return A list of links with the specified fullnames.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getLinksById(@Nonnull String... names) {
        return new QueryMany<>(
                Thing::toLink,
                this,
                Endpoint.GET_BY_ID,
                Joiner.on(',').join(names)
        );
    }

    /**
     * Get the comment tree for a given Link article.<p>
     * If a base 36 id is supplied, it will be the (highlighted) focal point of the returned view and context will be
     * the number of parents shown.<p>
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code comment}</th>
     *         <th>(optional) ID36 of a comment</th>
     *     </tr>
     *     <tr>
     *         <th>{@code context}</th>
     *         <th>an integer between 0 and 8</th>
     *     </tr>
     *     <tr>
     *         <th>{@code depth}</th>
     *         <th>(optional) an integer</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>(optional) an integer</th>
     *     </tr>
     *     <tr>
     *         <th>{@code showedits}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code showmedia}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code showmore}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code showtitle}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sort}</th>
     *         <th>one of ({@code confidence}, {@code top}, {@code new}, {@code controversial}, {@code old},
     *         {@code random}, {@code qa}, {@code live})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits</th>
     *     </tr>
     *     <tr>
     *         <th>{@code theme}</th>
     *         <th>one of ({@code default}, {@code dark})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code threaded}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code truncate}</th>
     *         <th>an integer between 0 and 50</th>
     *     </tr>
     * </table>
     * @param article The base 36 id of a {@link Link}.
     * @return A {@link Submission} instance corresponding to the {@link Link}.
     */
    @Override
    @Nonnull
    public QueryOne<Submission> getComments(String article) {
        //TODO Move into Submission class
        Function<String, Submission> mapper = source -> {
            JSONArray response = new JSONArray(source);
            Link link;
            List<Thing> comments;

            //We receive an array consisting of two listings.
            //The first listing contains a randomly fetched submission
            //The second listing contains comments belonging to the fetched submission
            assert response.length() == 2;

            //Extract random submissions
            Listing listing = Thing.from(response.getJSONObject(0)).toListing();
            List<Thing> children = listing.getChildren();

            //Reddit should've only returned a single submission
            assert children.size() == 1;

            link = children.get(0).toLink();

            //Extract comments, if present
            listing = Thing.from(response.getJSONObject(1)).toListing();
            comments = Collections.unmodifiableList(listing.getChildren());

            return SubmissionFactory.create(link, comments);
        };

        return new QueryOne<>(
                mapper,
                this,
                Endpoint.GET_COMMENTS,
                article
        );
    }

    /**
     * Links sorted by {code controversial} have recently received a high amount of upvotes <b>and</b> downvotes.
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code t}</th>
     *         <th>one of ({@code hour}, {@code day}, {@code week}, {@code month}, {@code year}, {@code all})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getControversialLinks() {
        return new QueryMany<>(
                Thing::toLink,
                this,
                Endpoint.GET_CONTROVERSIAL
        );
    }

    /**
     * Return a list of other links of the same URL. This happens, for example, if a link is cross-posted to another
     * {@link Subreddit}.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code crossposts_only}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sort}</th>
     *         <th>one of ({@code num_comments}, {@code new})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr}</th>
     *         <th>subreddit name</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @param article The base 36 id of a {@link Link}.
     * @return An instance containing all duplicate links.
     */
    @Override
    @Nonnull
    public QueryOne<Duplicate> getDuplicates(String article) {
        //TODO Move into Duplicate class
        Function<String, Duplicate> mapper = source -> {
            JSONArray response = new JSONArray(source);
            Link reference;
            List<Link> duplicates;

            //We receive an array consisting of two listings.
            //The first listing contains the original submission
            //The second listing contains duplicates. Duplicates are
            //created e.g. by cross-posting the original submission.
            assert response.length() == 2;

            //Extract source
            Listing listing = Thing.from(response.getJSONObject(0)).toListing();
            List<Thing> children = listing.getChildren();

            //Reddit should've only returned a single submission
            assert children.size() == 1;

            reference = children.get(0).toLink();

            //Duplicates, if present
            listing = Thing.from(response.getJSONObject(1)).toListing();
            children = listing.getChildren();

            duplicates = children.stream().map(Thing::toLink).collect(Collectors.toUnmodifiableList());

            return DuplicateFactory.create(reference, duplicates);
        };

        return new QueryOne<>(
                mapper,
                this,
                Endpoint.GET_DUPLICATES,
                article
        );
    }

    /**
     * Links sorted by {code hot} have recently received a high amount of upvotes and/or comments.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code q}</th>
     *         <th>	one of (GLOBAL, US, AR, AU, BG, CA, CL, CO, HR, CZ, FI, FR, DE, GR, HU, IS, IN, IE, IT, JP, MY, MX,
     *         NZ, PH, PL, PT, PR, RO, RS, SG, ES, SE, TW, TH, TR, GB, US_WA, US_DE, US_DC, US_WI, US_WV, US_HI, US_FL,
     *         US_WY, US_NH, US_NJ, US_NM, US_TX, US_LA, US_NC, US_ND, US_NE, US_TN, US_NY, US_PA, US_CA, US_NV, US_VA,
     *         US_CO, US_AK, US_AL, US_AR, US_VT, US_IL, US_GA, US_IN, US_IA, US_OK, US_AZ, US_ID, US_CT, US_ME, US_MD,
     *         US_MA, US_OH, US_UT, US_MO, US_MN, US_MI, US_RI, US_KS, US_MT, US_MS, US_SC, US_KY, US_OR, US_SD)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getHotLinks() {
        return new QueryMany<>(
                Thing::toLink,
                this,
                Endpoint.GET_HOT
        );
    }

    /**
     * Links sorted by {code new} are ordered according to their submission date.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getNewLinks() {
        return new QueryMany<>(
                Thing::toLink,
                this,
                Endpoint.GET_NEW
        );
    }

    /**
     * The {@link Link} is chosen randomly from the {@code top} links.
     * @return A link.
     * @see #getTopLinks()
     */
    @Override
    @Nonnull
    public QueryOne<Submission> getRandomSubmission() {
        //Move to Submission class
        Function<String, Submission> mapper = source -> {
            JSONArray response = new JSONArray(source);

            Link link;
            List<Thing> comments;

            //We receive an array consisting of two listings.
            //The first listing contains a randomly fetched submission
            //The second listing contains comments belonging to the fetched submission
            //Note that it might be the case that the comments are compressed
            assert response.length() == 2;

            //Extract random submissions
            Listing listing = Thing.from(response.getJSONObject(0)).toListing();
            List<Thing> children = listing.getChildren();

            //Reddit should've only returned a single submission
            assert children.size() == 1;

            link = children.get(0).toLink();

            //Extract comments, if present
            listing = Thing.from(response.getJSONObject(1)).toListing();
            comments = Collections.unmodifiableList(listing.getChildren());

            return SubmissionFactory.create(link, comments);
        };

        return new QueryOne<>(
                mapper,
                this,
                Endpoint.GET_RANDOM
        );
    }

    /**
     * Links sorted by {code rising} have received a high amount of upvotes and/or comments right now.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getRisingLinks() {
        return new QueryMany<>(
                Thing::toLink,
                this,
                Endpoint.GET_RISING
        );
    }

    /**
     * Links sorted by {code top} have received a high amount of upvotes over an unspecified period of time.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code t}</th>
     *         <th>one of ({@code hour}, {@code day}, {@code week}, {@code month}, {@code year}, {@code all})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     * </table>
     * @return A list of links.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getTopLinks() {
        return new QueryMany<>(
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
     * Provides access to the search function for links.<p>
     * This endpoint is a listing and accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code after}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code before}</th>
     *         <th>{@code fullname} of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code category}</th>
     *         <th>a string no longer than 5 characters</th>
     *     </tr>
     *     <tr>
     *         <th>{@code count}</th>
     *         <th>a positive integer (default: 0)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code include_facets}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code limit}</th>
     *         <th>the maximum number of items desired (default: 25, maximum: 100)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code q}</th>
     *         <th>a string no longer than 512 characters</th>
     *     </tr>
     *     <tr>
     *         <th>{@code restrict_sr}</th>
     *         <th>boolean value</th>
     *     </tr>
     *     <tr>
     *         <th>{@code show}</th>
     *         <th>(optional) the string {@code all}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sort}</th>
     *         <th>one of ({@code relevance}, {@code hot}, {@code top}, {@code new}, {@code comments})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code sr_detail}</th>
     *         <th>(optional) expand subreddits (boolean)</th>
     *     </tr>
     *     <tr>
     *         <th>{@code t}</th>
     *         <th>one of ({@code hour}, {@code day}, {@code week}, {@code month}, {@code year}, {@code all})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code type}</th>
     *         <th>(optional) comma-delimited list of result types ({@code sr}, {@code link}, {@code user})</th>
     *     </tr>
     * </table>
     * @return A list of things.
     */
    @Override
    @Nonnull
    public QueryMany<Thing> getSearch() {
        return new QueryMany<>(
                Function.identity(),
                this,
                Endpoint.GET_SEARCH
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Account                                                                                             //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    @Nonnull
    public QueryOne<Account> getAccount(String name){
        return new QueryOne<>(
                source -> Thing.from(source).toAccount(this),
                this,
                Endpoint.GET_USER_USERNAME_ABOUT,
                name
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Utility classes                                                                                             //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public Subreddit getSubreddit(String name) throws HttpException, IOException, InterruptedException {
        String source = new APIRequest.Builder(this).setEndpoint(Endpoint.GET_SUBREDDIT_ABOUT).setArgs(name).build().get();
        Thing thing = ThingFactory.create(Thing::new, new JSONObject(source));

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
