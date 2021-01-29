package vartas.jra;

import org.json.JSONArray;
import vartas.jra.$factory.SubmissionFactory;
import vartas.jra.exceptions.NotFoundException;
import vartas.jra.query.QueryMany;
import vartas.jra.query.QueryOne;
import vartas.jra.types.$json.JSONRules;
import vartas.jra.types.Listing;
import vartas.jra.types.Rules;
import vartas.jra.types.Thing;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
                client,
                Endpoint.GET_SUBREDDIT_COMMENTS,
                getDisplayName(),
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
                client,
                Endpoint.GET_SUBREDDIT_CONTROVERSIAL,
                getDisplayName()
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
    public QueryMany<Link> getHotLinks(){
        return new QueryMany<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_HOT,
                getDisplayName()
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
    @Nonnull
    @Override
    public QueryMany<Link> getNewLinks(){
        return new QueryMany<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_NEW,
                getDisplayName()
        );
    }

    /**
     * Fetches a random {@link Submission} from the {@link Subreddit}.<p>
     * The {@link Submission} corresponds to one of the current top links.
     * @return A random {@link Submission}.
     */
    @Override
    public QueryOne<Submission> getRandomSubmission() {
        //TODO Move to Submission class
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
                client,
                Endpoint.GET_SUBREDDIT_RANDOM,
                getDisplayName()
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
    public QueryMany<Link> getRisingLinks(){
        return new QueryMany<>(
                Thing::toLink,
                client,
                Endpoint.GET_SUBREDDIT_RISING,
                getDisplayName()
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
    public QueryMany<Link> getTopLinks(){
        return new QueryMany<>(
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
                client,
                Endpoint.GET_SUBREDDIT_SEARCH,
                getDisplayName()
        );
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the rules for this subreddit.<p>
     * The returned instance contains three entries. The first one is a list over all rules specific to this subreddit,
     * followed by the site-wide rules. Additionally, it also the provides the report function.
     *
     * @return The rules for this subreddit.
     */
    @Override
    @Nonnull
    public QueryOne<Rules> getRules() {
        return new QueryOne<>(
                source -> JSONRules.fromJson(new Rules(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_RULES,
                getDisplayName()
        );
    }

    /**
     * Get the sidebar for the this {@link Subreddit}.
     * @deprecated The sidebar endpoint seems to be deprecated in favor of #getDescription()
     * @return The description of the sidebar.
     */
    @Override
    @Nonnull
    @Deprecated()
    public QueryOne<String> getSidebar() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDIT_SIDEBAR,
                getDisplayName()
        );
    }

    /**
     * Gets one of the posts stickied in this {@link Subreddit}.<p>
     * A {@link Subreddit} allows up to two submissions to be stickied at a time. The query throws an
     * {@link NotFoundException} in case no link has been stickied.<p>
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code num}</th>
     *         <th>an integer between 1 and 2 (default: 1)</th>
     *     </tr>
     * </table>
     * @return One of the stickied submissions.
     */
    @Override
    @Nonnull
    public QueryOne<Submission> getSticky(){
        //TODO Move to Submission class
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
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_STICKY,
                getDisplayName()
        );
    }
}
