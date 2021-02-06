package vartas.jra;

import vartas.jra.exceptions.NotFoundException;
import vartas.jra.query.QueryMany;
import vartas.jra.query.QueryOne;
import vartas.jra.query.QueryPost;
import vartas.jra.types.Rules;
import vartas.jra.types.Thing;
import vartas.jra.types.User;
import vartas.jra.types.UserList;
import vartas.jra.types._json.JSONRules;
import vartas.jra.types._json.JSONUser;

import javax.annotation.Nonnull;
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
        return new QueryOne<>(
                Submission::from,
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
                source -> Thing.from(source).toLink(),
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
                source -> Thing.from(source).toLink(),
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
                source -> Thing.from(source).toLink(),
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
        return new QueryOne<>(
                Submission::from,
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
                source -> Thing.from(source).toLink(),
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
                source -> Thing.from(source).toLink(),
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
                Thing::from,
                client,
                Endpoint.GET_SUBREDDIT_SEARCH,
                getDisplayName()
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//


    /**
     * Create a relationship between a user and another user or subreddit.<p>
     * OAuth2 use requires appropriate scope based on the 'type' of the relationship:<p>
     * <ul>
     *     <li>moderator: Use "moderator_invite"</li>
     *     <li>moderator_invite: modothers</li>
     *     <li>contributor: modcontributors</li>
     *     <li>banned: modcontributors</li>
     *     <li>muted: modcontributors</li>
     *     <li>wikibanned: modcontributors and modwiki</li>
     *     <li>wikicontributor: modcontributors and modwiki</li>
     *     <li>friend: Use /api/v1/me/friends/{username}</li>
     *     <li>enemy: Use /api/block</li>
     * </ul>
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code api_type}</th>
     *         <th>The string {@code json}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code ban_context}</th>
     *         <th>fullname of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code ban_message}</th>
     *         <th>raw markdown text</th>
     *     </tr>
     *     <tr>
     *         <th>{@code ban_reason}</th>
     *         <th>a string no longer than 100 characters</th>
     *     </tr>
     *     <tr>
     *         <th>{@code container}</th>
     *         <th></th>
     *     </tr>
     *     <tr>
     *         <th>{@code duration}</th>
     *         <th>an integer between 1 and 999</th>
     *     </tr>
     *     <tr>
     *         <th>{@code name}</th>
     *         <th>the name of an existing user</th>
     *     </tr>
     *     <tr>
     *         <th>{@code note}</th>
     *         <th>a string no longer than 300 characters</th>
     *     </tr>
     *     <tr>
     *         <th>{@code permissions}</th>
     *         <th></th>
     *     </tr>
     *     <tr>
     *         <th>{@code type}</th>
     *         <th>	one of ({@code friend}, {@code moderator}, {@code moderator_invite}, {@code contributor},
     *         {@code banned}, {@code muted}, {@code wikibanned}, {@code wikicontributor})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code uh / X-Modhash header}</th>
     *         <th>a modhash</th>
     *     </tr>
     * </table>
     * Complement to {@link #postUnfriend()}
     * @see #postUnfriend()
     */
    @Override
    public QueryPost<String> postFriend() {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_FRIEND);
    }

    /**
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code api_type}</th>
     *         <th>the string {@code json}</th>
     *     </tr>
     *     <tr>
     *         <th>{@code name}</th>
     *         <th>the name of an existing user</th>
     *     </tr>
     *     <tr>
     *         <th>{@code permission}</th>
     *         <th></th>
     *     </tr>
     *     <tr>
     *         <th>{@code type}</th>
     *         <th></th>
     *     </tr>
     *     <tr>
     *         <th>{@code uh / X-Modhash header}</th>
     *         <th>a modhash</th>
     *     </tr>
     * </table>
     */
    @Override
    public QueryPost<String> postSetPermission() {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_SETPERMISSION);
    }

    /**
     * Remove a relationship between a user and another user or subreddit<p>
     * The user can either be passed in by name (nuser) or by fullname (iuser). If type is friend or enemy, 'container'
     * MUST be the current user's fullname; for other types, the subreddit must be set via URL
     * (e.g., /r/funny/api/unfriend).<p>
     * OAuth2 use requires appropriate scope based on the 'type' of the relationship:
     * <ul>
     *     <li>moderator: modothers</li>
     *     <li>moderator_invite: modothers</li>
     *     <li>contributor: modcontributors</li>
     *     <li>banned: modcontributors</li>
     *     <li>muted: modcontributors</li>
     *     <li>wikibanned: modcontributors and modwiki</li>
     *     <li>wikicontributor: modcontributors and modwiki</li>
     *     <li>friend: Use /api/v1/me/friends/{username}</li>
     *     <li>enemy: privatemessages<li>
     * </ul>
     * This endpoint accepts the following arguments:
     * <table>
     *     <tr>
     *         <th>{@code container}</th>
     *         <th></th>
     *     </tr>
     *     <tr>
     *         <th>{@code id}</th>
     *         <th>fullname of a thing</th>
     *     </tr>
     *     <tr>
     *         <th>{@code name}</th>
     *         <th>the name of an existing user</th>
     *     </tr>
     *     <tr>
     *         <th>{@code type}</th>
     *         <th>	one of ({@code friend}, {@code enemy}, {@code moderator}, {@code moderator_invite},
     *         {@code contributor}, {@code banned}, {@code muted}, {@code wikibanned}, {@code wikicontributor})</th>
     *     </tr>
     *     <tr>
     *         <th>{@code uh / X-Modhash header}</th>
     *         <th>a modhash</th>
     *     </tr>
     * </table>
     *
     * Complement to {@link #postFriend()}
     * @see #postFriend()
     */
    @Override
    public QueryPost<String> postUnfriend() {
        return new QueryPost<>(Function.identity(), client, Endpoint.POST_UNFRIEND);
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
        return new QueryOne<>(
                Submission::from,
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_STICKY,
                getDisplayName()
        );
    }

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    Users                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    @Override
    public QueryMany<User> getBanned() {
        return new QueryMany<>(
                source -> JSONUser.fromJson(new User(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_BANNED,
                getDisplayName()
        );

    }

    @Override
    public QueryMany<User> getContributors() {
        return new QueryMany<>(
                source -> JSONUser.fromJson(new User(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_CONTRIBUTORS,
                getDisplayName()
        );
    }

    @Override
    public QueryOne<UserList> getModerators() {
        return new QueryOne<>(
                source -> Thing.from(source).toUserList(),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_MODERATORS,
                getDisplayName()
        );
    }

    @Override
    public QueryMany<User> getMuted() {
        return new QueryMany<>(
                source -> JSONUser.fromJson(new User(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_MUTED,
                getDisplayName()
        );
    }

    @Override
    public QueryMany<User> getWikibanned() {
        return new QueryMany<>(
                source -> JSONUser.fromJson(new User(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_WIKIBANNED,
                getDisplayName()
        );
    }

    @Override
    public QueryMany<User> getWikicontributors() {
        return new QueryMany<>(
                source -> JSONUser.fromJson(new User(), source),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_WIKICONTRIBUTORS,
                getDisplayName()
        );
    }

    @Override
    public QueryPost<String> postDeleteBanner() {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_BANNER,
                getDisplayName()
        );
    }

    @Override
    public QueryPost<String> postDeleteHeader() {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_HEADER,
                getDisplayName()
        );
    }

    @Override
    public QueryPost<String> postDeleteIcon() {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_ICON,
                getDisplayName()
        );
    }

    @Override
    public QueryPost<String> postDeleteImage() {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_DELETE_IMAGE,
                getDisplayName()
        );
    }

    @Override
    public QueryPost<String> postStylesheet() {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_STYLESHEET,
                getDisplayName()
        );
    }

    @Override
    public QueryPost<String> postUploadImage() {
        return new QueryPost<>(
                Function.identity(),
                client,
                Endpoint.POST_SUBREDDIT_UPLOAD_IMAGE,
                getDisplayName()
        );
    }

    @Override
    public QueryOne<String> getPostRequirements() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDIT_POST_REQUIREMENTS,
                getDisplayName()
        );
    }

    @Override
    public QueryOne<String> getSubmitText() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDIT_SUBMIT_TEXT,
                getDisplayName()
        );
    }

    @Override
    public QueryOne<String> getEdit() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_EDIT,
                getDisplayName()
        );
    }

    @Override
    public QueryOne<String> getTraffic() {
        return new QueryOne<>(
                Function.identity(),
                client,
                Endpoint.GET_SUBREDDIT_ABOUT_TRAFFIC,
                getDisplayName()
        );
    }
}
