package vartas.jra;

import org.json.JSONObject;
import vartas.jra.exceptions.HttpException;
import vartas.jra.http.APIRequest;
import vartas.jra.query.listings.*;
import vartas.jra.query.search.QuerySearch;
import vartas.jra.query.subreddits.QuerySticky;
import vartas.jra.types.$json.JSONRules;
import vartas.jra.types.Rules;
import vartas.jra.types.Thing;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * The communities on Reddit are divided into several subreddits.<p>
 * Each subreddit can be uniquely identified by its name.<p>
 * This class implements all subreddit-specific endpoints.
 */
@Nonnull
public class Subreddit extends SubredditTOP{
    /**
     * A reference to the client required for communicating with the subreddit-specific endpoints.
     */
    @Nonnull
    private final Client client;

    /**
     * Creates a new instance of a subreddit.
     * @param client The client communicating with the endpoints.
     */
    @Nonnull
    public Subreddit(@Nonnull Client client){
        this.client = client;
    }

    /**
     * Returns a reference to the instance of this class.
     * @return {@code this}.
     */
    @Override
    @Nonnull
    public Subreddit getRealThis() {
        return this;
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Listings                                                                                                    //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Get the comment tree for a given {@link Link}.
     * @param article The id36 of a link.
     * @return A query over all comments of the specified {@link Link}.
     */
    @Override
    @Nonnull
    public QueryComment getComments(@Nonnull String article){
        return new QueryComment(
                client,
                Endpoint.GET_SUBREDDIT_COMMENTS,
                getDisplayName(),
                article
        );
    }

    /**
     * Get the most recent links, sorted by most controversial first.<p>
     * Controversial links receive both a high amount of upvotes and downvotes.
     * @return A query over the most controversial links.
     */
    @Override
    @Nonnull
    public QueryControversial<Link> getControversialLinks(){
        return new QueryControversial<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_CONTROVERSIAL,
                getDisplayName()
        );
    }


    /**
     * Get the most recent links, sorted by hottest first.<p>
     * Hot links gain a large amount of upvotes within a short period of time.
     * @return A query over the hottest links.
     */
    @Override
    @Nonnull
    public QueryHot<Link> getHotLinks(){
        return new QueryHot<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_HOT,
                getDisplayName()
        );
    }


    /**
     * Get the most recent links, sorted by new.<p>
     * As the name suggests, new links are sorted by their submission time.
     * @return A query over most recently submitted links.
     */
    @Nonnull
    @Override
    public QueryNew<Link> getNewLinks(){
        return new QueryNew<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_NEW,
                getDisplayName()
        );
    }

    /**
     * Fetches a random {@link Link} from the {@link Subreddit}.<p>
     * The {@link Link} corresponds to one of the current top links.
     * @return A query over a random {@link Link}.
     */
    @Override
    public QueryRandom getRandomLink() {
        return new QueryRandom(
                client,
                Endpoint.GET_SUBREDDIT_RANDOM,
                getDisplayName()
        );
    }


    /**
     * Get the most recent links, sorted by rising.<p>
     * Rising links have recently received a lot of traffic, i.e. comments and upvotes.
     * @return A query over currently rising links.
     */
    @Override
    @Nonnull
    public QueryRising<Link> getRisingLinks(){
        return new QueryRising<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_RISING,
                getDisplayName()
        );
    }


    /**
     * Get the most recent links, sorted by top.<p>
     * Out of all links, top links have receive the most amount of upvotes. Additionally, they have been posted
     * recently, to continuously get new entries.
     * @return A query over the top links.
     */
    @Override
    @Nonnull
    public QueryTop<Link> getTopLinks(){
        return new QueryTop<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_TOP,
                getDisplayName()
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
        return new QuerySearch(client, Endpoint.GET_SUBREDDIT_SEARCH, getDisplayName());
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the rules for this subreddit.<p>
     * The returned instance contains three entries. The first one is a list over all rules specific to this subreddit,
     * followed by the site-wide rules. Additionally, it also the provides the report function.
     *
     * @return The rules for this subreddit.
     * @throws InterruptedException If the query got interrupted while waiting to be executed.
     * @throws IOException If an exception occurred during the request.
     * @throws HttpException If the request got rejected by the server.
     */
    @Override
    @Nonnull
    public Rules getRules() throws InterruptedException, IOException, HttpException {
        String source = new APIRequest.Builder(client)
                .setEndpoint(Endpoint.GET_SUBREDDIT_ABOUT_RULES)
                .setArgs(getDisplayName())
                .build()
                .get();
        JSONObject data = new JSONObject(source);
        return JSONRules.fromJson(new Rules(), data);
    }

    /**
     * Get the sidebar for the this {@link Subreddit}.
     * @deprecated The sidebar endpoint seems to be deprecated in favor of #getDescription()
     */
    @Override
    @Nonnull
    @Deprecated()
    public String getSidebar() {
        return getDescription();
    }

    /**
     * Gets one of the posts stickied in this {@link Subreddit}.<p>
     * A {@link Subreddit} allows up to two submissions to be stickied at a time.
     * @return A query for one of the stickied links, if present.
     */
    @Override
    @Nonnull
    public QuerySticky getSticky(){
        return new QuerySticky(client, getDisplayName());
    }
}
