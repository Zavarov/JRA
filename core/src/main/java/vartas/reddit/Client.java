package vartas.reddit;

import com.google.common.base.Joiner;
import com.google.common.net.HttpHeaders;
import okhttp3.*;
import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vartas.reddit.$factory.SubredditFactory;
import vartas.reddit.$factory.UserAgentFactory;
import vartas.reddit.exceptions.$factory.HttpExceptionFactory;
import vartas.reddit.exceptions.$factory.NotFoundExceptionFactory;
import vartas.reddit.exceptions.$factory.RateLimiterExceptionFactory;
import vartas.reddit.exceptions.HttpException;
import vartas.reddit.exceptions.RateLimiterException;
import vartas.reddit.query.*;
import vartas.reddit.types.$factory.ListingFactory;
import vartas.reddit.types.$factory.ThingFactory;
import vartas.reddit.types.$factory.TrendingSubredditsFactory;
import vartas.reddit.types.Listing;
import vartas.reddit.types.Thing;
import vartas.reddit.types.TrendingSubreddits;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class Client extends ClientTOP{
    @Nonnull
    protected static final String ACCESS_TOKEN = "https://www.reddit.com/api/v1/access_token";
    @Nonnull
    protected static final String REVOKE_TOKEN = "https://www.reddit.com/api/v1/revoke_token";
    @Nonnull
    protected static final String HTTPS = "https";
    @Nonnull
    protected static final String OAUTH2 = "oauth.reddit.com";
    @Nonnull
    protected static final String WWW = "www.reddit.com";
    @Nonnull
    protected final String uuid = UUID.randomUUID().toString();
    @Nonnull
    protected final TimedSemaphore rateLimiter = new TimedSemaphore(1, TimeUnit.MINUTES, 60);
    @Nonnull
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    @Nonnull
    protected final OkHttpClient http = new OkHttpClient();

    public Client(@Nonnull String platform, @Nonnull String version, @Nonnull String author){
        setUserAgent(UserAgentFactory.create(platform, getClass().getPackage().getName(), version, author));
    }

    @Override
    public Client getRealThis() {
        return this;
    }

    protected String buildUrl(String host, Map<?, ?> query, Endpoint endpoint, Object... args){
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

    private Request buildGet(String host, Map<?, ?> query, Endpoint endpoint, Object... args){
        assert isPresentToken();

        String url = buildUrl(host, query, endpoint, args);

        Request.Builder builder = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.USER_AGENT, getUserAgent().toString());

        if(Objects.equals(host, OAUTH2))
            builder.addHeader(HttpHeaders.AUTHORIZATION, "bearer " + orElseThrowToken().getAccessToken());

        return builder.get().build();
    }

    public String get(Endpoint endpoint, Object... args) throws InterruptedException, IOException, HttpException {
        return get(OAUTH2, endpoint, args);
    }

    public String get(String host, Endpoint endpoint, Object... args) throws InterruptedException, IOException, HttpException {
        return get(host, Collections.emptyMap(), endpoint, args);
    }

    public String get(Map<?, ?> query, Endpoint endpoint, Object... args) throws InterruptedException, IOException, HttpException {
        return get(OAUTH2, query, endpoint, args);
    }

    public String get(String host, Map<?, ?> query, Endpoint endpoint, Object... args) throws InterruptedException, IOException, HttpException {
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

    protected synchronized Response request(Request request) throws IOException, HttpException, InterruptedException, RateLimiterException {
        assert isPresentToken();

        return execute(request);
    }

    protected Response execute(Request request) throws InterruptedException, IOException, HttpException, RateLimiterException {
        //Wait if we're making too many requests at once
        rateLimiter.acquire();

        log.debug("--> {}", request);
        Response response = http.newCall(request).execute();
        log.debug("<-- {}", response);

        if(!response.isSuccessful()){
            System.out.println(response.code());
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
    //    API Calls                                                                                                   //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public void login() throws InterruptedException, IOException, HttpException {
        login(Duration.PERMANENT);
    }

    @Override
    public Subreddit getSubreddit(String name) throws HttpException, IOException, InterruptedException {
        Thing thing = ThingFactory.create(Thing::new, new JSONObject(get(Endpoint.GET_SUBREDDIT_ABOUT, name)));

        //TODO Check
        //In case a subreddit with the specified name doesn't exist, the return Thing may be arbitrary
        if(Thing.Kind.Subreddit.matches(thing.getKind()))
            return SubredditFactory.create(() -> new Subreddit(this), thing.getData());
        else
            throw NotFoundExceptionFactory.create(HttpURLConnection.HTTP_NOT_FOUND, "A subreddit with this name doesn't exist");

    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public TrendingSubreddits getTrendingSubreddits() throws IOException, HttpException, InterruptedException {
        //Because for some reason trending subreddits don't require OAuth2 and return an 400 if used
        JSONObject response = new JSONObject(get(WWW, Endpoint.GET_API_TRENDING_SUBREDDITS));
        return TrendingSubredditsFactory.create(TrendingSubreddits::new, response);
    }

    @Override
    public QueryBest<Link> getBestLinks() {
        return new QueryBest<>(
                Thing::toLink,
                this,
                Endpoint.GET_BEST
        );
    }

    //@Override
    public List<Link> getLinksById(@Nonnull String... names) throws InterruptedException, IOException, HttpException {
        JSONObject response = new JSONObject(get(Endpoint.GET_BY_ID, Joiner.on(",").join(names)));

        Thing thing = Thing.from(response);
        Listing listing = ListingFactory.create(Listing::new, thing.getData());
        return listing.getChildren().stream().map(Thing::toLink).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public QueryComments getComments(String article) {
        return new QueryComments(this, Endpoint.GET_COMMENTS, article);
    }

    @Override
    public QueryControversial<Link> getControversialLinks() {
        return new QueryControversial<>(
                Thing::toLink,
                this,
                Endpoint.GET_CONTROVERSIAL
        );
    }

    @Override
    public QueryDuplicates getDuplicates(String article) {
        return new QueryDuplicates(this, article);
    }

    @Override
    public QueryHot<Link> getHotLinks() {
        return new QueryHot<>(
                Thing::toLink,
                this,
                Endpoint.GET_HOT
        );
    }

    @Override
    public QueryNew<Link> getNewLinks() {
        return new QueryNew<>(
                Thing::toLink,
                this,
                Endpoint.GET_NEW
        );
    }

    @Override
    public QueryRandom getRandomLink() {
        return new QueryRandom(this, Endpoint.GET_RANDOM);
    }

    @Override
    public QueryRising<Link> getRisingLinks() {
        return new QueryRising<>(
                Thing::toLink,
                this,
                Endpoint.GET_RISING
        );
    }

    @Override
    public QueryTop<Link> getTopLinks() {
        return new QueryTop<>(
                Thing::toLink,
                this,
                Endpoint.GET_TOP
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Utility classes                                                                                             //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

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

    public enum TokenType {
        ACCESS_TOKEN("access_token"),
        REFRESH_TOKEN("refresh_token");

        private final String value;
        TokenType(String value){
            this.value = value;
        }

        @Override
        public String toString(){
            return value;
        }
    }

    /**
     * A list containing all supported Reddit scopes.
     * See https://www.reddit.com/api/v1/scopes for more.
     */
    public enum Scope {
        /**
         * Spend my reddit gold creddits on giving gold to other users.
         */
        CREDDITS("creddits"),
        /**
         * Add/remove users to approved user lists and ban/unban or mute/unmute users from subreddits I moderate.
         */
        MODCONTRIBUTORS("modcontributors"),
        /**
         * Access and manage modmail via mod.reddit.com.
         */
        MODMAIL("modmail"),
        /**
         * Manage the configuration, sidebar, and CSS of subreddits I moderate.
         */
        MODCONFIG("modconfig"),
        /**
         * Manage my subreddit subscriptions. Manage \"friends\" - users whose content I follow.
         */
        SUBSCRIBE("subscribe"),
        /**
         * Edit structured styles for a subreddit I moderate.
         */
        STRUCTUREDSTYLES("structuredstyles"),
        /**
         * Submit and change my votes on comments and submissions.
         */
        VOTE("vote"),
        /**
         * Edit wiki pages on my behalf.
         */
        WIKIEDIT("wikiedit"),
        /**
         * Access the list of subreddits I moderate, contribute to, and subscribe to.
         */
        MYSUBREDDITS("mysubreddits"),
        /**
         * Submit links and comments from my account.
         */
        SUBMIT("submit"),
        /**
         * Access the moderation log in subreddits I moderate.
         */
        MODLOG("modlog"),
        /**
         * Approve, remove, mark nsfw, and distinguish content in subreddits I moderate.
         */
        MODPOST("modpost"),
        /**
         * Manage and assign flair in subreddits I moderate.
         */
        MODFLAIR("modflair"),
        /**
         * Save and unsave comments and submissions.
         */
        SAVE("save"),
        /**
         * Invite or remove other moderators from subreddits I moderate.
         */
        MODOTHERS("modothers"),
        /**
         * Access posts and comments through my account.
         */
        READ("read"),
        /**
         * Access my inbox and send private messages to other users.
         */
        PRIVATEMESSAGES("privatemessages"),
        /**
         * Report content for rules violations. Hide &amp; show individual submissions.
         */
        REPORT("report"),
        /**
         * Access my reddit username and signup date.
         */
        IDENTITY("identity"),
        /**
         * Manage settings and contributors of live threads I contribute to.
         */
        LIVEMANAGE("livemanage"),
        /**
         * Update preferences and related account information. Will not have access to your email or password.
         */
        ACCOUNT("account"),
        /**
         * Access traffic stats in subreddits I moderate.
         */
        MODTRAFFIC("modtraffic"),
        /**
         * Read wiki pages through my account.
         */
        WIKIREAD("wikiread"),
        /**
         * Edit and delete my comments and submissions.
         */
        EDIT("edit"),
        /**
         * Change editors and visibility of wiki pages in subreddits I moderate.
         */
        MODWIKI("modwiki"),
        /**
         * Accept invitations to moderate a subreddit. Remove myself as a moderator or contributor of subreddits I moderate
         * or contribute to.
         */
        MODSELF("modself"),
        /**
         * Access my voting history and comments or submissions I've saved or hidden.
         */
        HISTORY("history"),
        /**
         * Select my subreddit flair. Change link flair on my submissions.
         */
        FLAIR("flair"),

        ANY("*");

        private final String name;

        Scope(String name){
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
    }
}
