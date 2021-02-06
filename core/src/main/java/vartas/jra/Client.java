package vartas.jra;

import com.google.common.base.Joiner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.jra._factory.RateLimiterFactory;
import vartas.jra._factory.SubmissionFactory;
import vartas.jra._json.JSONSelfAccount;
import vartas.jra._json.JSONToken;
import vartas.jra.exceptions.HttpException;
import vartas.jra.exceptions.RateLimiterException;
import vartas.jra.exceptions._factory.*;
import vartas.jra.http.APIAuthentication;
import vartas.jra.http.APIRequest;
import vartas.jra.query.QueryLocal;
import vartas.jra.query.QueryMany;
import vartas.jra.query.QueryOne;
import vartas.jra.query.QueryPost;
import vartas.jra.types.*;
import vartas.jra.types._factory.DuplicateFactory;
import vartas.jra.types._json.JSONTrendingSubreddits;
import vartas.jra.types._json.JSONUserDataMap;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    protected final RateLimiter rateLimiter = RateLimiterFactory.create(RateLimiter::new, 0, 60, 60);
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
    @Nonnull
    protected final Scope[] scope;

    /**
     * Creates a new instance.
     * @param userAgent The user agent attached to every request.
     * @param id The application id.
     * @param secret The application "password".
     * @see <a href="https://github.com/reddit-archive/reddit/wiki/OAuth2">here</a>
     */
    @Nonnull
    public Client(@Nonnull UserAgent userAgent, @Nonnull String id, @Nonnull String secret, @Nonnull Scope... scope){
        setUserAgent(userAgent);
        this.credentials = Base64.getEncoder().encodeToString((id+":"+secret).getBytes(StandardCharsets.UTF_8));
        this.scope = scope;
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
    public synchronized Response execute(Request request) throws InterruptedException, IOException, HttpException, RateLimiterException {
        //Wait if we're making too many requests at once
        rateLimiter.acquire();

        log.debug("--> {}", request);
        Response response = http.newCall(request).execute();
        rateLimiter.update(response);
        log.debug("<-- {}", response);
        log.debug("{} used, {} remain, {} seconds until next period", rateLimiter.getUsed(), rateLimiter.getRemaining(), rateLimiter.getReset());


        if(!response.isSuccessful()){
            switch(response.code()){
                //Unauthorized
                case 401:
                    throw UnauthorizedExceptionFactory.create(response.code(), response.message());
                //Forbidden
                case 403:
                    throw ForbiddenExceptionFactory.create(response.code(), response.message());
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

        new APIAuthentication.Builder(REVOKE_TOKEN, credentials, this)
                .addParameter("token", orElseThrowToken().orElseThrowRefreshToken())
                .addParameter("token_type_hint", TokenType.REFRESH_TOKEN)
                .build()
                .post();
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

        new APIAuthentication.Builder(REVOKE_TOKEN, credentials, this)
                .addParameter("token", orElseThrowToken().getAccessToken())
                .addParameter("token_type_hint", TokenType.REFRESH_TOKEN)
                .build()
                .post();
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

        APIAuthentication request = new APIAuthentication.Builder(ACCESS_TOKEN, credentials, this)
                .addParameter("grant_type", GrantType.REFRESH)
                .addParameter("refresh_token", orElseThrowToken().orElseThrowRefreshToken())
                .build();

        String response = request.post();

        //On February 15th 2021, the refresh response will contain a new refresh token.
        //Until then, we reuse the initial token.
        //@see https://redd.it/kvzaot
        String refreshToken = orElseThrowToken().orElseThrowRefreshToken();

        setToken(JSONToken.fromJson(new Token(), response));
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
     * Returns the {@link SelfAccount} of the client.
     * @return An instance of the {@link SelfAccount} of the currently logged-in user.
     * @see Endpoint#GET_ME
     */
    @Override
    @Nonnull
    public QueryOne<SelfAccount> getMe() {
        return new QueryOne<>(
                source -> JSONSelfAccount.fromJson(new SelfAccount(this), source),
                this,
                Endpoint.GET_ME
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

    @Override
    public QueryLocal<FrontPage> getFrontPage(){
        return new QueryLocal<>(new FrontPage(this));
    }

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
     * Get a listing of links by fullname.
     * @param names A sequence of {@link Link} fullnames.
     * @return A list of links with the specified fullnames.
     */
    @Override
    @Nonnull
    public QueryMany<Link> getLinksById(@Nonnull String... names) {
        return new QueryMany<>(
                source -> Thing.from(source).toLink(),
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
            List<String> children = listing.getChildren();

            //Reddit should've only returned a single submission
            assert children.size() == 1;

            link = Thing.from(children.get(0)).toLink();

            //Extract comments, if present
            listing = Thing.from(response.getJSONObject(1)).toListing();
            comments = listing.streamChildren().map(Thing::from).collect(Collectors.toUnmodifiableList());

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
            List<String> children = listing.getChildren();

            //Reddit should've only returned a single submission
            assert children.size() == 1;

            reference = Thing.from(children.get(0)).toLink();

            //Duplicates, if present
            listing = Thing.from(response.getJSONObject(1)).toListing();
            duplicates = listing.streamChildren().map(Thing::from).map(Thing::toLink).collect(Collectors.toUnmodifiableList());

            return DuplicateFactory.create(reference, duplicates);
        };

        return new QueryOne<>(
                mapper,
                this,
                Endpoint.GET_DUPLICATES,
                article
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code ids}</th>
     *         <th>A comma-separated list of account fullnames</th>
     *     </tr>
     * </table>
     */
    @Override
    public QueryOne<UserDataMap> getUserDataByAccountIds() {
        return new QueryOne<>(
                source -> JSONUserDataMap.fromJson(new UserDataMap(), source),
                this,
                Endpoint.GET_USER_DATA_BY_ACCOUNT_IDS
        );
    }

    /**
     * Check whether a username is available for registration.<p>
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code user}</th>
     *         <th>a valid, unused, username</th>
     *     </tr>
     * </table>
     */
    @Override
    public QueryOne<Boolean> getUsernameAvailable() {
        return new QueryOne<>(
                Boolean::parseBoolean,
                this,
                Endpoint.GET_USERNAME_AVAILABLE
        );
    }

    /**
     * Return information about the user, including karma and gold status.
     */
    @Override
    @Nonnull
    public QueryOne<Account> getAccount(String username){
        return new QueryOne<>(
                source -> Thing.from(source).toAccount(this),
                this,
                Endpoint.GET_USER_USERNAME_ABOUT,
                username
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Subreddits                                                                                                  //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    @Deprecated
    public QueryOne<String> getRecommendSubreddits(String... subreddits) {
        return new QueryOne<>(
                Function.identity(),
                this,
                Endpoint.GET_RECOMMEND_SUBREDDITS,
                Joiner.on(',').join(subreddits)
        );
    }

    @Override
    public QueryOne<String> getSearchRedditNames() {
        return new QueryOne<>(
                Function.identity(),
                this,
                Endpoint.GET_SEARCH_REDDIT_NAMES
        );
    }

    @Override
    public QueryPost<String> postSearchRedditNames() {
        return new QueryPost<>(
                Function.identity(),
                this,
                Endpoint.POST_SEARCH_REDDIT_NAMES
        );
    }

    @Override
    public QueryPost<String> postSearchSubreddits() {
        return new QueryPost<>(
                Function.identity(),
                this,
                Endpoint.POST_SEARCH_SUBREDDITS
        );
    }

    @Override
    public QueryPost<String> postSiteAdmin() {
        return new QueryPost<>(
                Function.identity(),
                this,
                Endpoint.POST_SITE_ADMIN
        );
    }

    @Override
    public QueryOne<Subreddit> getSubreddit(String name){
        return new QueryOne<>(
                source -> Thing.from(source).toSubreddit(this),
                this,
                Endpoint.GET_SUBREDDIT_ABOUT,
                name
        );
    }

    @Override
    public QueryOne<String> getSubredditAutocomplete() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDIT_AUTOCOMPLETE
        );
    }

    @Override
    public QueryOne<String> getSubredditAutocompleteV2() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDIT_AUTOCOMPLETE_V2
        );
    }

    @Override
    public QueryPost<String> postSubscribe() {
        return new QueryPost<>(
                Function.identity(),
                this,
                Endpoint.POST_SUBSCRIBE
        );
    }

    @Override
    public QueryOne<String> getSubredditsPopular() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDITS_POPULAR
        );
    }

    @Override
    public QueryOne<String> getSubredditsNew() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDITS_NEW
        );
    }

    @Override
    public QueryOne<String> getSubredditsGold() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDITS_GOLD
        );
    }

    @Override
    public QueryOne<String> getSubredditsDefault() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDITS_DEFAULT
        );
    }

    @Override
    public QueryOne<String> getSubredditsSearch() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_SUBREDDITS_SEARCH
        );
    }

    @Override
    public QueryOne<String> getUsersNew() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_USERS_NEW
        );
    }

    @Override
    public QueryOne<String> getUsersPopular() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_USERS_POPULAR
        );
    }

    @Override
    public QueryOne<String> getUsersSearch() {
        return new QueryOne<>(
                source -> source,
                this,
                Endpoint.GET_USERS_SEARCH
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Utility classes                                                                                             //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    public enum Duration {
        PERMANENT,
        TEMPORARY;

        @Override
        public String toString(){
            return name().toLowerCase(Locale.ENGLISH);
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
        ACCESS_TOKEN,
        /**
         * The refresh token is required when requesting a new access token, once the previous one expired.
         */
        @Nonnull
        REFRESH_TOKEN;

        @Override
        public String toString(){
            return name().toLowerCase(Locale.ENGLISH);
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
        private final String name;

        /**
         * Assigns each scope a name. The name matches the one used by Reddit.
         * @param name The name of the scope.
         */
        @Nonnull
        Scope(@Nonnull String name){
            this.name = name;
        }

        @Nonnull
        @Override
        public String toString(){
            return name;
        }
    }
}
