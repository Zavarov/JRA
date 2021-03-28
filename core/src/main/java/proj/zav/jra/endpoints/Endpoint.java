package proj.zav.jra.endpoints;

import proj.zav.jra.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A collection of all Reddit Endpoints usable by this application.
 */
public enum Endpoint {

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    account                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * <p>
     * The identity of the authenticated user.
     * </p>
     * <p>
     * This endpoint is a {@link AbstractSelfAccount SelfAccount}.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me">here</a>
     */
    GET_ME("api","v1","me"),
    /**
     * <p>
     * Returns all blocked users.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_blocked">here</a>
     * @deprecated Deprecated in favor of {@link #GET_PREFS_BLOCKED}.
     */
    @Deprecated
    GET_ME_BLOCKED("api","v1","me","blocked"),
    /**
     * <p>
     * Returns all friends.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_friends">here</a>
     * @deprecated Deprecated in favor of {@link #GET_PREFS_FRIENDS}.
     */
    @Deprecated
    GET_ME_FRIENDS("api","v1","me","friends"),
    /**
     * <p>
     * Return a breakdown of subreddit karma.
     * </p>
     * <p>
     * This endpoint is a {@link KarmaList}.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_karma">here</a>
     */
    GET_ME_KARMA("api","v1","me","karma"),
    /**
     * <p>
     * Return the preference settings of the logged in user.
     * </p>
     * <p>
     * This endpoint is an {@link AbstractPreferences}.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_prefs">here</a>
     */
    GET_ME_PREFS("api","v1","me","prefs"),
    /**
     * <p>
     * Return a list of trophies for the current user.
     * </p>
     * <p>
     * This endpoint is a {@link TrophyList}.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_trophies">here</a>
     */
    GET_ME_TROPHIES("api","v1","me","trophies"),
    /**
     * <p>
     * The endpoint for all user preferences.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_{where}">here</a>
     */
    GET_PREFS_WHERE("prefs","{where}"),
    /**
     * <p>
     * Returns all blocked users.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_blocked">here</a>
     */
    GET_PREFS_BLOCKED(GET_PREFS_WHERE.getPath("blocked")),
    /**
     * <p>
     * Returns all friends.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_friends">here</a>
     */
    GET_PREFS_FRIENDS(GET_PREFS_WHERE.getPath("friends")),
    /**
     * <p>
     * Returns all blocked and trusted users.
     * </p>
     * <p>
     * This endpoint is a {@link Messaging}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_messaging">here</a>
     */
    GET_PREFS_MESSAGING(GET_PREFS_WHERE.getPath("messaging")),
    /**
     * <p>
     * Returns all trusted users.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_trusted">here</a>
     */
    GET_PREFS_TRUSTED(GET_PREFS_WHERE.getPath("trusted")),

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    captcha                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * <p>
     * Check whether ReCAPTCHAs are needed for API methods.<p>
     * </p>
     * <p>
     * This endpoint returns a boolean.
     * </p>
     * @deprecated Reddit no longer requires captchas and thus this endpoint returns 403
     */
    @Deprecated
    GET_NEEDS_CAPTCHA("api","needs_captcha"),

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    collections                                                                                                 //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    emoji                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    flair                                                                                                       //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    reddit gold                                                                                                 //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    links & comments                                                                                            //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    listings                                                                                                    //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * <p>
     * Return a list of trending subreddits, link to the comment in r/trendingsubreddits, and the comment count of that
     * link.
     * </p>
     * <p>
     * Note: The actual endpoint deviates from the documentation as in that it ends with {@code .json}. It also seems to
     * be one of the few endpoints that can't be accessed via OAuth2.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_trending_subreddits">here</a>
     */
    GET_API_TRENDING_SUBREDDITS("api","trending_subreddits.json"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_best">here</a>
     */
    GET_BEST("best"),
    /**
     * <p>
     * Get a listing of links by fullname.
     * </p>
     * <p>
     * {@code names} is a list of fullnames for links separated by commas or spaces.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_by_id_{names}">here</a>
     */
    GET_BY_ID("by_id","{names}"),
    /**
     * <p>
     * Get the comment tree for a given Link {@code article}.
     * </p>
     * <p>
     * If supplied, {@code comment} is the ID36 of a comment in the comment tree for {@code article}. This comment will
     * be the (highlighted) focal point of the returned view and context will be the number of parents shown.
     * </p>
     * <p>
     * {@code depth} is the maximum depth of subtrees in the thread.
     * </p>
     * <p>
     * {@code limit} is the maximum number of comments to return.
     * </p>
     *
     * TODO see also /api/morechildren
     * TODO see also /api/comment
     * @see #GET_SUBREDDIT_COMMENTS
     * @see <a href="https://www.reddit.com/dev/api#GET_comments_{article}">here</a>
     */
    GET_COMMENTS("comments","{article}"),
    /**
     * <p>
     * Get the comment tree for a given Link {@code article}.
     * </p>
     * <p>
     * If supplied, {@code comment} is the ID36 of a comment in the comment tree for {@code article}. This comment will
     * be the (highlighted) focal point of the returned view and context will be the number of parents shown.
     * </p>
     * <p>
     * {@code depth} is the maximum depth of subtrees in the thread.
     * </p>
     * <p>
     * {@code limit} is the maximum number of comments to return.
     * </p>
     *
     * TODO see also /api/morechildren
     * TODO see also /api/comment
     * @see #GET_COMMENTS
     * @see <a href="https://www.reddit.com/dev/api#GET_comments_{article}">here</a>
     */
    GET_SUBREDDIT_COMMENTS("r","{subreddit}","comments","{article}"),
    /**
     * Return a list of other submissions of the same URL.
     *
     * @see <a href="https://www.reddit.com/dev/api#GET_duplicates_{article}">here</a>
     */
    GET_DUPLICATES("duplicates","{article}"),
    /**
     * <p>
     * Get all links.
     * </p>
     * <p>
     * In case the user is logged in, the links will be fetched from all subscribed subreddits, otherwise from the
     * frontpage.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_hot">here</a>
     */
    GET_HOT("hot"),
    /**
     * Get all links from the given subreddit.
     * @see <a href="https://www.reddit.com/dev/api#GET_hot">here</a>
     */
    GET_SUBREDDIT_HOT("r","{subreddit}","hot"),
    /**
     * <p>
     * Get all links.
     * </p>
     * <p>
     * In case the user is logged in, the links will be fetched from all subscribed subreddits, otherwise from the
     * frontpage.
     * </p>
     * <p>
     * {@code new} sorts the subreddits based on their creation date, newest first.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_new">here</a>
     */
    GET_NEW("new"),
    /**
     * <p>
     * Get all links from the given subreddit.
     * </p>
     * <p>
     * {@code new} sorts the submissions based on their creation date, newest first.
     * </p>
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_new">here</a>
     */
    GET_SUBREDDIT_NEW("r", "{subreddit}","new"),
    /**
     * The Serendipity button. I.e. it fetches a random {@link AbstractLink Link}.
     * @see <a href="https://www.reddit.com/dev/api#GET_random">here</a>
     */
    GET_RANDOM("random"),
    /**
     * The Serendipity button. I.e. it fetches a random {@link AbstractLink Link} from the
     * {@link AbstractSubreddit Subreddit}.
     * @see <a href="https://www.reddit.com/dev/api#GET_random">here</a>
     */
    GET_SUBREDDIT_RANDOM("r", "{subreddit}", "random"),
    /**
     * <p>
     * Get all links.
     * </p>
     * <p>
     * In case the user is logged in, the links will be fetched from all subscribed subreddits, otherwise from the
     * frontpage.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_rising">here</a>
     */
    GET_RISING("rising"),
    /**
     * Get all links from the specified subreddit.
     * @see <a href="https://www.reddit.com/dev/api#GET_rising">here</a>
     */
    GET_SUBREDDIT_RISING("r", "{subreddit}", "rising"),
    /**
     * <p>
     * Get all links.
     * </p>
     * <p>
     * In case the user is logged in, the links will be fetched from all subscribed subreddits, otherwise from the
     * frontpage.
     * </p>
     * {@code sort} indicates the order in which the links are sorted.
     * @see <a href="https://www.reddit.com/dev/api#GET_%28sort%29">here</a>
     */
    GET_SORT("{sort}"),
    /**
     * <p>
     * Get all links from the specified subreddit.
     * </p>
     * <p>
     * {@code sort} indicates the order in which the links are sorted.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_%28sort%29">here</a>
     */
    GET_SUBREDDIT_SORT("r","{subreddit}","{sort}"),
    /**
     * <p>
     * Get all links.
     * </p>
     * <p>
     * In case the user is logged in, the links will be fetched from all subscribed subreddits, otherwise from the
     * frontpage.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_top">here</a>
     */
    GET_TOP(GET_SORT.getPath("top")),
    /**
     * Get all links from the specified subreddit.
     * @see <a href="https://www.reddit.com/dev/api#GET_top">here</a>
     */
    GET_SUBREDDIT_TOP(GET_SUBREDDIT_SORT.getPath("{subreddit}", "top")),
    /**
     * TODO What does it do?
     * @see #GET_SORT
     * @see <a href="https://www.reddit.com/dev/api#GET_controversial">here</a>
     */
    GET_CONTROVERSIAL(GET_SORT.getPath("controversial")),
    /**
     * TODO What does it do?
     * @see #GET_SUBREDDIT_SORT
     * @see <a href="https://www.reddit.com/dev/api#GET_controversial">here</a>
     */
    GET_SUBREDDIT_CONTROVERSIAL(GET_SUBREDDIT_SORT.getPath("{subreddit}", "controversial")),

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    live threads                                                                                                //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    private messages                                                                                            //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    misc                                                                                                        //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    moderation                                                                                                  //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    new modmail                                                                                                 //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    multis                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    search                                                                                                      //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Search links page.
     * @see <a href="https://www.reddit.com/dev/api#GET_search">here</a>
     */
    GET_SUBREDDIT_SEARCH("r", "{subreddit}", "search"),
    /**
     * Search links page.
     * @see <a href="https://www.reddit.com/dev/api#GET_search">here</a>
     */
    GET_SEARCH("search"),

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    subreddits                                                                                                  //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//
    /**
     * <p>
     * The endpoint for retrieving information about a subreddit with respect to a user.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_{where}">here</a>
     */
    GET_SUBREDDIT_ABOUT_WHERE("r","{subreddit}","about","{where}"),
    /**
     * <p>
     * The endpoint for retrieving information about a subreddit with respect to a user.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_{where}">here</a>
     */
    GET_ABOUT_WHERE("about","{where}"),
    /**
     * <p>
     * All currently banned users.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_banned">here</a>
     */
    GET_SUBREDDIT_ABOUT_BANNED(GET_SUBREDDIT_ABOUT_WHERE.getPath("{subreddit}","banned")),
    /**
     * <p>
     * All currently banned users.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_banned">here</a>
     */
    GET_ABOUT_BANNED(GET_ABOUT_WHERE.getPath("banned")),
    /**
     * <p>
     * All contributors.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_contributors">here</a>
     */
    GET_SUBREDDIT_ABOUT_CONTRIBUTORS(GET_SUBREDDIT_ABOUT_WHERE.getPath("{subreddit}","contributors")),
    /**
     * <p>
     * All contributors.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_contributors">here</a>
     */
    GET_ABOUT_CONTRIBUTORS(GET_ABOUT_WHERE.getPath("contributors")),
    /**
     * <p>
     * All moderators.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_moderators">here</a>
     */
    GET_SUBREDDIT_ABOUT_MODERATORS(GET_SUBREDDIT_ABOUT_WHERE.getPath("{subreddit}","moderators")),
    /**
     * <p>
     * All moderators.
     * </p>
     * <p>
     * This endpoint is a {@link UserList}.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_moderators">here</a>
     */
    GET_ABOUT_MODERATORS(GET_ABOUT_WHERE.getPath("moderators")),
    /**
     * <p>
     * All currently muted users.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_muted">here</a>
     */
    GET_SUBREDDIT_ABOUT_MUTED(GET_SUBREDDIT_ABOUT_WHERE.getPath("{subreddit}","muted")),
    /**
     * <p>
     * All currently muted users.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_muted">here</a>
     */
    GET_ABOUT_MUTED(GET_ABOUT_WHERE.getPath("muted")),
    /**
     * <p>
     * All users banned from the wiki.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_wikibanned">here</a>
     */
    GET_SUBREDDIT_ABOUT_WIKIBANNED(GET_SUBREDDIT_ABOUT_WHERE.getPath("{subreddit}","wikibanned")),
    /**
     * <p>
     * All users banned from the wiki.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_wikibanned">here</a>
     */
    GET_ABOUT_WIKIBANNED(GET_ABOUT_WHERE.getPath("wikibanned")),
    /**
     * <p>
     * All users contributing to the wiki.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_wikicontributors">here</a>
     */
    GET_SUBREDDIT_ABOUT_WIKICONTRIBUTORS(GET_SUBREDDIT_ABOUT_WHERE.getPath("{subreddit}","wikicontributors")),
    /**
     * <p>
     * All users contributing to the wiki.
     * </p>
     * <p>
     * This endpoint is a list of users.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code user}</th><th>A valid, existing reddit username</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_about_wikicontributors">here</a>
     */
    GET_ABOUT_WIKICONTRIBUTORS(GET_ABOUT_WHERE.getPath("wikicontributors")),
    /**
     * <p>
     * Remove the subreddit's custom mobile banner.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_banner">here</a>
     */
    POST_SUBREDDIT_DELETE_SUBREDDIT_BANNER("r","{subreddit}","api","delete_sr_banner"),
    /**
     * <p>
     * Remove the subreddit's custom mobile banner.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_banner">here</a>
     */
    POST_DELETE_SUBREDDIT_BANNER("api","delete_sr_banner"),
    /**
     * <p>
     * Remove the subreddit's custom header image.
     * </p>
     * <p>
     * The sitewide-default header image will be shown again after this call.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_header">here</a>
     */
    POST_SUBREDDIT_DELETE_SUBREDDIT_HEADER("r","{subreddit}","api","delete_sr_header"),
    /**
     * <p>
     * Remove the subreddit's custom header image.
     * </p>
     * <p>
     * The sitewide-default header image will be shown again after this call.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_header">here</a>
     */
    POST_DELETE_SUBREDDIT_HEADER("api","delete_sr_header"),
    /**
     * <p>
     * Remove the subreddit's custom mobile icon.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_icon">here</a>
     */
    POST_SUBREDDIT_DELETE_SUBREDDIT_ICON("r","{subreddit}","api","delete_sr_icon"),
    /**
     * <p>
     * Remove the subreddit's custom mobile icon.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_icon">here</a>
     */
    POST_DELETE_SUBREDDIT_ICON("api","delete_sr_icon"),
    /**
     * <p>
     * Remove an image from the subreddit's custom image set.
     * </p>
     * <p>
     * The image will no longer count against the subreddit's image limit. However, the actual image data may still be
     * accessible for an unspecified amount of time. If the image is currently referenced by the subreddit's stylesheet,
     * that stylesheet will no longer validate and won't be editable until the image reference is removed.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code img_name}</th><th>a valid subreddit image name</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_img">here</a>
     */
    POST_SUBREDDIT_DELETE_SUBREDDIT_IMAGE("r","{subreddit}","api","delete_sr_img"),
    /**
     * <p>
     * Remove an image from the subreddit's custom image set.
     * </p>
     * <p>
     * The image will no longer count against the subreddit's image limit. However, the actual image data may still be
     * accessible for an unspecified amount of time. If the image is currently referenced by the subreddit's stylesheet,
     * that stylesheet will no longer validate and won't be editable until the image reference is removed.
     * </p>
     * <p>
     * This endpoints returns a jQuery response.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code img_name}</th><th>a valid subreddit image name</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_delete_sr_img">here</a>
     */
    POST_DELETE_SUBREDDIT_IMAGE("api","delete_sr_img"),
    /**
     * <p>
     * Return subreddits recommended for the given subreddit(s).
     * </p>
     * <p>
     * Gets a list of subreddits recommended for {@code srnames}, filtering out any that appear in the optional
     * {@code omit} param.
     * </p>
     * <table>
     *     <tr><th>{@code omit}</th><th>comma-delimited list of subreddit names</th></tr>
     *     <tr><th>{@code over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code srnames}</th><th>comma-delimited list of subreddit names</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_recommend_sr_{srnames}">here</a>
     */
    @Deprecated
    GET_RECOMMEND_SUBREDDITS("api","recommend","sr","{srnames}"),
    /**
     * <p>
     * List subreddit names that begin with a query string.
     * </p>
     * <p>
     * Subreddits whose names begin with {@code query} will be returned. If {@code include_over_18} is false,
     * subreddits with over-18 content restrictions will be filtered from the results.
     * </p>
     * <p>
     * If {@code include_unadvertisable} is False, subreddits that have {@code hide_ad} set to true or are on the
     * {@code anti_ads_subreddits} list will be filtered.
     * </p>
     * <p>
     * If {@code exact} is true, only an exact match will be returned. Exact matches are inclusive of {@code over_18}
     * subreddits, but not {@code hide_ad} subreddits when {@code include_unadvertisable} is False.
     * </p>
     * <p>
     * Example response for this endpoint:
     * </p>
     * <pre>
     * {@code
     *      {
     *          "names": ["reddit.com", "redditmobile"]
     *      }
     * }
     * </pre>
     * <table>
     *     <tr><th>{@code exact}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_unadvertisable}</th><th>boolean value</th></tr>
     *     <tr><th>{@code query}</th><th>a string up to 50 characters long, consisting of printable characters</th></tr>
     *     <tr><th>{@code search_query_id}</th><th>a uuid</th></tr>
     *     <tr><th>{@code typeahead_active}</th><th>boolean value or {@code None}</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_search_reddit_names">here</a>
     */
    GET_SEARCH_REDDIT_NAMES("api","search_reddit_names"),
    /**
     * <p>
     * List subreddit names that begin with a query string.
     * </p>
     * <p>
     * Subreddits whose names begin with {@code query} will be returned. If {@code include_over_18} is false,
     * subreddits with over-18 content restrictions will be filtered from the results.
     * </p>
     * <p>
     * If {@code include_unadvertisable} is False, subreddits that have {@code hide_ad} set to true or are on the
     * {@code anti_ads_subreddits} list will be filtered.
     * </p>
     * <p>
     * If {@code exact} is true, only an exact match will be returned. Exact matches are inclusive of {@code over_18}
     * subreddits, but not {@code hide_ad} subreddits when {@code include_unadvertisable} is False.
     * </p>
     * <p>
     * Example response for this endpoint:
     * </p>
     * <pre>
     * {@code
     *      {
     *          "names": ["reddit.com", "redditmobile"]
     *      }
     * }
     * </pre>
     * <table>
     *     <tr><th>{@code exact}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_unadvertisable}</th><th>boolean value</th></tr>
     *     <tr><th>{@code query}</th><th>a string up to 50 characters long, consisting of printable characters</th></tr>
     *     <tr><th>{@code search_query_id}</th><th>a uuid</th></tr>
     *     <tr><th>{@code typeahead_active}</th><th>boolean value or {@code None}</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_search_reddit_names">here</a>
     */
    POST_SEARCH_REDDIT_NAMES("api","search_reddit_names"),
    /**
     * <p>
     * List subreddits that begin with a query string.
     * </p>
     * <p>
     * Subreddits whose names begin with {@code query} will be returned. If {@code include_over_18} is false,
     * subreddits with over-18 content restrictions will be filtered from the results.
     * </p>
     * <p>
     * If {@code include_unadvertisable} is False, subreddits that have {@code hide_ads} set to True or are on the
     * {@code anti_ads_subreddits} list will be filtered.
     * </p>
     * <p>
     * If {@code exact} is true, only an exact match will be returned. Exact matches are inclusive of over_18
     * subreddits, but not hide_ad subreddits when {@code include_unadvertisable} is False.
     * </p>
     * <p>
     * Example response for this endpoint:
     * </p>
     * <pre>
     * {@code
     * {
     *      "subreddits": [
     *          {
     *              "icon_img": null,
     *              "key_color": "",
     *              "active_user_count": 942,
     *              "subscriber_count": 877502,
     *              "is_chat_post_feature_enabled": false,
     *              "allow_images": true,
     *              "name": "reddit.com",
     *              "allow_chat_post_creation": false
     *          }
     *      ]
     * }
     * }
     * </pre>
     * <table>
     *     <tr><th>{@code exact}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_unadvertisable}</th><th>boolean value</th></tr>
     *     <tr><th>{@code query}</th><th>a string up to 50 characters long, consisting of printable characters</th></tr>
     *     <tr><th>{@code search_query_id}</th><th>a uuid</th></tr>
     *     <tr><th>{@code typeahead_active}</th><th>boolean value or {@code None}</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_search_subreddits">here</a>
     */
    POST_SEARCH_SUBREDDITS("api","search_subreddits"),
    /**
     * <p>
     * Create or configure a subreddit.
     * </p>
     * <p>
     * If {@code sr} is specified, the request will attempt to modify the specified subreddit. If not, a subreddit with
     * name {@code name} will be created.
     * </p>
     * <p>
     * This endpoint expects all values to be supplied on every request. If modifying a subset of options, it may be
     * useful to get the current settings from /about/edit.json first.
     * </p>
     * <p>
     * For backwards compatibility, {@code description} is the sidebar text and {@code public_description} is the
     * publicly visible subreddit description.
     * </p>
     * <p>
     * Most of the parameters for this endpoint are identical to options visible in the user interface and their
     * meanings are best explained there.
     * </p>
     * <p>
     * See also: /about/edit.json.
     * </p>
     * <table>
     *     <tr><th>{@code admin_override_spam_comments}</th><th>boolean value</th></tr>
     *     <tr><th>{@code admin_override_spam_links}</th><th>boolean value</th></tr>
     *     <tr><th>{@code admin_override_spam_selfposts}</th><th>boolean value</th></tr>
     *     <tr><th>{@code all_original_content}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_chat_post_creation}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_discovery}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_galleries}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_images}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_polls}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_post_crossposts}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_predictions}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_predictions_tournament}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_top}</th><th>boolean value</th></tr>
     *     <tr><th>{@code allow_videos}</th><th>boolean value</th></tr>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code automated_reporting_level_abuse}</th><th>an integer between 0 and 3</th></tr>
     *     <tr><th>{@code automated_reporting_level_hate}</th><th>an integer between 0 and 3</th></tr>
     *     <tr><th>{@code collapse_deleted_comments}</th><th>boolean value</th></tr>
     *     <tr><th>{@code comment_score_hide_mins}</th><th>an integer between 0 and 1440 (default: 0)</th></tr>
     *     <tr><th>{@code crowd_control_chat_level}</th><th>an integer between 0 and 3</th></tr>
     *     <tr><th>{@code crowd_control_level}</th><th>an integer between 0 and 3</th></tr>
     *     <tr><th>{@code crowd_control_mode}</th><th>boolean value</th></tr>
     *     <tr><th>{@code description}</th><th>raw markdown text</th></tr>
     *     <tr><th>{@code disable_contributor_requests}</th><th>boolean value</th></tr>
     *     <tr><th>{@code exclude_banned_modqueue}</th><th>boolean value</th></tr>
     *     <tr><th>{@code free_form_reports}</th><th>boolean value</th></tr>
     *     <tr><th>{@code g-recaptcha-response}</th><th></th></tr>
     *     <tr><th>{@code header-title}</th><th>a string no longer than 500 characters</th></tr>
     *     <tr><th>{@code hide_ads}</th><th>boolean value</th></tr>
     *     <tr><th>{@code key_color}</th><th>a 6-digit rgb hex color, e.g. #AABBCC</th></tr>
     *     <tr><th>{@code lang}</th><th>a valid IETF language tag (underscore separated)</th></tr>
     *     <tr><th>{@code link_type}</th><th>one of ({@code any}, {@code link}, {@code self})</th></tr>
     *     <tr><th>{@code name}</th><th>subreddit name</th></tr>
     *     <tr><th>{@code new_pinned_post_pns_enabled}</th><th>boolean value</th></tr>
     *     <tr><th>{@code original_content_tag_enabled}</th><th>boolean value</th></tr>
     *     <tr><th>{@code over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code prediction_leaderboard_entry_type}</th><th>an integer between 0 and 2</th></tr>
     *     <tr><th>{@code public_description}</th><th>raw markdown text</th></tr>
     *     <tr><th>{@code restrict_commenting}</th><th>boolean value</th></tr>
     *     <tr><th>{@code restrict_posting}</th><th>boolean value</th></tr>
     *     <tr><th>{@code show_media}</th><th>boolean value</th></tr>
     *     <tr><th>{@code show_media_preview}</th><th>boolean value</th></tr>
     *     <tr><th>{@code spam_comments}</th><th>one of ({@code low}, {@code high}, {@code all})</th></tr>
     *     <tr><th>{@code spam_links}</th><th>one of ({@code low}, {@code high}, {@code all})</th></tr>
     *     <tr><th>{@code spam_selfposts}</th><th>one of ({@code low}, {@code high}, {@code all})</th></tr>
     *     <tr><th>{@code spoilers_enabled}</th><th>boolean value</th></tr>
     *     <tr><th>{@code sr}</th><th>fullname of a thing</th></tr>
     *     <tr><th>{@code submit_link_label}</th><th>a string no longer than 60 characters</th></tr>
     *     <tr><th>{@code submit_text}</th><th>raw markdown text</th></tr>
     *     <tr><th>{@code submit_text_label}</th><th>a string no longer than 60 characters</th></tr>
     *     <tr><th>{@code suggested_comment_sort}</th><th>one of ({@code confidence}, {@code top}, {@code new}, {@code controversial}, {@code old}, {@code random}, {@code qa}, {@code live})</th></tr>
     *     <tr><th>{@code title}</th><th>a string no longer than 100 characters</th></tr>
     *     <tr><th>{@code toxicity_threshold_chat_level}</th><th>an integer between 0 and 1</th></tr>
     *     <tr><th>{@code type}</th><th>one of ({@code gold_restricted}, {@code archived}, {@code restricted}, {@code private}, {@code employees_only}, {@code gold_only}, {@code public}, {@code user})</th></tr>
     *     <tr><th>{@code uh / X-Modhash header	}</th><th>a <i>modhash</i></th></tr>
     *     <tr><th>{@code user_flair_pns_enabled}</th><th>boolean value</th></tr>
     *     <tr><th>{@code welcome_message_enabled}</th><th>boolean value</th></tr>
     *     <tr><th>{@code welcome_message_text}</th><th>raw markdown text</th></tr>
     *     <tr><th>{@code wiki_edit_age}</th><th>an integer between 0 and 36600 (default: 0)</th></tr>
     *     <tr><th>{@code wiki_edit_karma}</th><th>an integer between 0 and 1000000000 (default: 0)</th></tr>
     *     <tr><th>{@code wikimode}</th><th>one of ({@code disabled}, {@code modonly}, {@code anyone})</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_site_admin">here</a>
     */
    POST_SITE_ADMIN("api","site_admin"),
    /**
     * <p>
     * Get the submission text for the subreddit.
     * </p>
     * <p>
     * This text is set by the subreddit moderators and intended to be displayed on the submission form.<p>
     * See also: /api/site_admin.<p>
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_submit_text">here</a>
     */
    GET_SUBREDDIT_SUBMIT_TEXT("r","{subreddit}","api","submit_text"),
    /**
     * <p>
     * Get the submission text for the subreddit.
     * </p>
     * <p>
     * This text is set by the subreddit moderators and intended to be displayed on the submission form.<p>
     * See also: /api/site_admin.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_submit_text">here</a>
     */
    GET_SUBMIT_TEXT("api","submit_text"),
    /**
     * <p>
     * Return a list of subreddits and data for subreddits whose names start with 'query'.
     * </p>
     * <p>
     * Uses typeahead endpoint to recieve the list of subreddits names. Typeahead provides exact matches, typo
     * correction, fuzzy matching and boosts subreddits to the top that the user is subscribed to.
     * </p>
     * <p>
     * Example response for this endpoint:
     * </p>
     * <pre>
     * {@code
     * {
     *      "subreddits": [
     *          {
     *              "numSubscribers": 0,
     *              "name": "u_reddit",
     *              "allowedPostTypes": {"images": true, "text": true, "videos": true, "links": true, "spoilers": true},
     *              "id": "t5_hv5dz",
     *              "primaryColor": "",
     *              "communityIcon": "",
     *              "icon": "https://styles.redditmedia.com/t5_hv5dz/styles/profileIcon_snoo8658e16c-55fa-486f-b7c7-00726de2e742-headshot.png"
     *          }
     *      ]
     * }
     * }
     * </pre>
     * <table>
     *     <tr><th>{@code include_over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_profiles}</th><th>boolean value</th></tr>
     *     <tr><th>{@code query}</th><th>a string up to 25 characters long, consisting of printable characters.</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_subreddit_autocomplete">here</a>
     */
    GET_SUBREDDIT_AUTOCOMPLETE("api","subreddit_autocomplete"),
    /**
     * <p>
     * Return a list of subreddits and data for subreddits whose names start with 'query'.
     * </p>
     * <p>
     * Uses typeahead endpoint to recieve the list of subreddits names. Typeahead provides exact matches, typo
     * correction, fuzzy matching and boosts subreddits to the top that the user is subscribed to.
     * </p>
     * <p>
     * This endpoint is a listing of things. Those things can either be subreddits or accounts, i.e. personal
     * subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code include_over_18}</th><th>boolean value</th></tr>
     *     <tr><th>{@code include_profiles}</th><th>boolean value</th></tr>
     *     <tr><th>{@code limit}</th><th>an integer between 1 and 10 (default: 5)</th></tr>
     *     <tr><th>{@code query}</th><th>a string up to 25 characters long, consisting of printable characters.</th></tr>
     *     <tr><th>{@code search_query_id}</th><th>a uuid</th></tr>
     *     <tr><th>{@code typeahead_active}</th><th>boolean value or {@code None}</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_subreddit_autocomplete_v2">here</a>
     */
    GET_SUBREDDIT_AUTOCOMPLETE_V2("api","subreddit_autocomplete_v2"),
    /**
     * <p>
     * Update a subreddit's stylesheet.
     * </p>
     * <p>
     * {@code op} should be {@code save} to update the contents of the stylesheet.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code op}</th><th>one of ({@code save}, {@code preview})</th></tr>
     *     <tr><th>{@code reason}</th><th>a string up to 256 characters long, consisting of printable characters.</th></tr>
     *     <tr><th>{@code stylesheet_contents}</th><th>the new stylesheet content</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_subreddit_stylesheet">here</a>
     */
    POST_SUBREDDIT_SUBREDDIT_STYLESHEET("r","{subreddit}","api","subreddit_stylesheet"),
    /**
     * <p>
     * Update a subreddit's stylesheet.
     * </p>
     * <p>
     * {@code op} should be {@code save} to update the contents of the stylesheet.
     * </p>
     * <table>
     *     <tr><th>{@code api_type}</th><th>the string {@code json}</th></tr>
     *     <tr><th>{@code op}</th><th>one of ({@code save}, {@code preview})</th></tr>
     *     <tr><th>{@code reason}</th><th>a string up to 256 characters long, consisting of printable characters.</th></tr>
     *     <tr><th>{@code stylesheet_contents}</th><th>the new stylesheet content</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_subreddit_stylesheet">here</a>
     */
    POST_SUBREDDIT_STYLESHEET("api","subreddit_stylesheet"),
    /**
     * <p>
     * Subscribe to or unsubscribe from a subreddit.
     * </p>
     * <p>
     * To subscribe, {@code action} should be {@code sub}. To unsubscribe, {@code action} should be {@code unsub}. The
     * user must have access to the subreddit to be able to subscribe to it.
     * </p>
     * <p>
     * The {@code skip_initial_defaults} param can be set to True to prevent automatically subscribing the user to the
     * current set of defaults when they take their first subscription action. Attempting to set it for an unsubscribe
     * action will result in an error.
     * </p>
     * <p>
     * See also: /subreddits/mine/.
     * </p>
     * <table>
     *     <tr><th>{@code action}</th><th>one of ({@code sub}, {@code unsub})</th></tr>
     *     <tr><th>{@code action_source}</th><th>one of ({@code o}, {@code n}, {@code b}, {@code o}, {@code a}, {@code r}, {@code d}, {@code i}, {@code n}, {@code g})</th></tr>
     *     <tr><th>{@code skip_initial_defaults}</th><th>boolean value</th></tr>
     *     <tr><th>{@code sr / sr_name}</th><th>A comma-separated list of subreddit <i>fullnames</i> (when using the "sr" parameter), or of subreddit names (when using the "sr_name" parameter).</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_subscribe">here</a>
     */
    POST_SUBSCRIBE("api","subscribe"),
    /**
     * <p>
     * Add or replace a subreddit image, custom header logo, custom mobile icon, or custom mobile banner.
     * </p>
     * <ul>
     *     <li>If the {@code upload_type} value is {@code img}, an image for use in the subreddit stylesheet is uploaded with the name specified in {@code name}.</li>
     *     <li>If the {@code upload_type} value is {@code header} then the image uploaded will be the subreddit's new logo and {@code name} will be ignored.</li>
     *     <li>If the {@code upload_type} value is {@code icon} then the image uploaded will be the subreddit's new mobile icon and {@code name} will be ignored.</li>
     *     <li>If the {@code upload_type} value is {@code banner} then the image uploaded will be the subreddit's new mobile banner and {@code name} will be ignored.</li>
     * </ul>
     * <p>
     * For backwards compatibility, if {@code upload_type} is not specified, the {@code header} field will be used
     * instead:
     * </p>
     * <ul>
     *     <li>If the {@code header} field has value {@code 0}, then {@code upload_type} is {@code img}.</li>
     *     <li>If the {@code header} field has value {@code 1}, then {@code upload_type} is {@code header}.</li>
     * </ul>
     * <p>
     * The {@code img_type} field specifies whether to store the uploaded image as a PNG or JPEG.<p>
     * Subreddits have a limited number of images that can be in use at any given time. If no image with the specified
     * name already exists, one of the slots will be consumed.
     * </p>
     * <p>
     * If an image with the specified name already exists, it will be replaced. This does not affect the stylesheet
     * immediately, but will take effect the next time the stylesheet is saved.
     * </p>
     * <p>
     * See also: /api/delete_sr_img, /api/delete_sr_header, /api/delete_sr_icon, and /api/delete_sr_banner.<p>
     * </p>
     * <table>
     *     <tr><th>{@code file}</th><th>file upload with maximum size of 500 KiB</th></tr>
     *     <tr><th>{@code formid}</th><th>(optional) can be ignored</th></tr>
     *     <tr><th>{@code header}</th><th>an integer between 0 and 1</th></tr>
     *     <tr><th>{@code img_type}</th><th>one of png or jpg (default: png)</th></tr>
     *     <tr><th>{@code name}</th><th>a valid subreddit image name</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <tr><th>{@code upload_type}</th><th>one of ({@code img}, {@code header}, {@code icon}, {@code banner})</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_upload_sr_img">here</a>
     */
    POST_SUBREDDIT_UPLOAD_SUBREDDIT_IMAGE("r","{subreddit}","api","upload_sr_img"),
    /**
     * <p>
     * Add or replace a subreddit image, custom header logo, custom mobile icon, or custom mobile banner.
     * </p>
     * <ul>
     *     <li>If the {@code upload_type} value is {@code img}, an image for use in the subreddit stylesheet is uploaded with the name specified in {@code name}.</li>
     *     <li>If the {@code upload_type} value is {@code header} then the image uploaded will be the subreddit's new logo and {@code name} will be ignored.</li>
     *     <li>If the {@code upload_type} value is {@code icon} then the image uploaded will be the subreddit's new mobile icon and {@code name} will be ignored.</li>
     *     <li>If the {@code upload_type} value is {@code banner} then the image uploaded will be the subreddit's new mobile banner and {@code name} will be ignored.</li>
     * </ul>
     * <p>
     * For backwards compatibility, if {@code upload_type} is not specified, the {@code header} field will be used
     * instead:
     * </p>
     * <ul>
     *     <li>If the {@code header} field has value {@code 0}, then {@code upload_type} is {@code img}.</li>
     *     <li>If the {@code header} field has value {@code 1}, then {@code upload_type} is {@code header}.</li>
     * </ul>
     * <p>
     * The {@code img_type} field specifies whether to store the uploaded image as a PNG or JPEG.
     * </p>
     * <p>
     * Subreddits have a limited number of images that can be in use at any given time. If no image with the specified
     * name already exists, one of the slots will be consumed.
     * </p>
     * <p>
     * If an image with the specified name already exists, it will be replaced. This does not affect the stylesheet
     * immediately, but will take effect the next time the stylesheet is saved.
     * </p>
     * <p>
     * See also: /api/delete_sr_img, /api/delete_sr_header, /api/delete_sr_icon, and /api/delete_sr_banner.
     * </p>
     * <table>
     *     <tr><th>{@code file}</th><th>file upload with maximum size of 500 KiB</th></tr>
     *     <tr><th>{@code formid}</th><th>(optional) can be ignored</th></tr>
     *     <tr><th>{@code header}</th><th>an integer between 0 and 1</th></tr>
     *     <tr><th>{@code img_type}</th><th>one of png or jpg (default: png)</th></tr>
     *     <tr><th>{@code name}</th><th>a valid subreddit image name</th></tr>
     *     <tr><th>{@code uh / X-Modhash header}</th><th>a <i>modhash</i></th></tr>
     *     <tr><th>{@code upload_type}</th><th>one of ({@code img}, {@code header}, {@code icon}, {@code banner})</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_upload_sr_img">here</a>
     */
    POST_UPLOAD_SUBREDDIT_IMAGE("api","upload_sr_img"),
    /**
     * <p>
     * Fetch moderator-designated requirements to post to the subreddit.
     * </p>
     * <p>
     * Moderators may enable certain restrictions, such as minimum title length, when making a submission to their
     * subreddit.
     * </p>
     * <p>
     * Clients may use the values returned by this endpoint to pre-validate fields before making a request to POST
     * /api/submit. This may allow the client to provide a better user experience to the user, for example by creating a
     * text field in their app that does not allow the user to enter more characters than the max title length.
     * </p>
     * <p>
     * A non-exhaustive list of possible requirements a moderator may enable:
     * </p>
     * <ul>
     *     <li>{@code body_blacklisted_strings}: List of strings. Users may not submit posts that contain these words.</li>
     *     <li>{@code body_restriction_policy}: String. One of "required", "notAllowed", or "none", meaning that a self-post body is required, not allowed, or optional, respectively.</li>
     *     <li>{@code domain_blacklist}: List of strings. Users may not submit links to these domains</li>
     *     <li>{@code domain_whitelist}: List of strings. Users submissions MUST be from one of these domains</li>
     *     <li>{@code is_flair_required}: Boolean. If True, flair must be set at submission time.</li>
     *     <li>{@code title_blacklisted_strings}: List of strings. Submission titles may NOT contain any of the listed strings.</li>
     *     <li>{@code title_required_strings}: List of strings. Submission title MUST contain at least ONE of the listed strings.</li>
     *     <li>{@code title_text_max_length}: Integer. Maximum length of the title field.</li>
     *     <li>{@code title_text_min_length}: Integer. Minimum length of the title field.</li>
     * </ul>
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_%28subreddit%29_post_requirements">here</a>
     */
    GET_SUBREDDIT_POST_REQUIREMENTS("api","v1","{subreddit}","post_requirements"),
    /**
     * <p>
     * Return information about the subreddit.
     * </p>
     * <p>
     * Data includes the subscriber count, description, and header image.
     * </p>
     * <p>
     * This endpoint is a {@link AbstractSubreddit Subreddit}.<p>
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_r_%28subreddit%29_about">here</a>
     */
    GET_SUBREDDIT_ABOUT("r", "{subreddit}", "about"),
    /**
     * <p>
     * Get the current settings of a subreddit.
     * </p>
     * <p>
     * In the API, this returns the current settings of the subreddit as used by /api/site_admin. On the HTML site, it
     * will display a form for editing the subreddit.
     * </p>
     * <table>
     *     <tr><th>{@code created}</th><th>one of ({@code true}, {@code false})</th></tr>
     *     <tr><th>{@code location}</th><th></th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_r_%28subreddit%29_about_edit">here</a>
     */
    GET_SUBREDDIT_ABOUT_EDIT("r", "{subreddit}", "about", "edit"),
    /**
     * Get the rules for the current subreddit.
     * @see <a href="https://www.reddit.com/dev/api#GET_r_%28subreddit%29_about_rules">here</a>
     */
    GET_SUBREDDIT_ABOUT_RULES("r", "{subreddit}", "about", "rules"),
    /**
     * <p>
     * Get the traffic of a subreddit. I.e. the number of submissions, comments and overall activity over time.
     * </p>
     * <table>
     *     <tr><th>{@code num}</th><th>an integer between 1 and 2 (default: 1)</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_r_%28subreddit%29_about_traffic">here</a>
     */
    GET_SUBREDDIT_ABOUT_TRAFFIC("r", "{subreddit}", "about", "traffic"),
    /**
     * <p>
     * Get the sidebar for the current subreddit.
     * </p>
     * <p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_sidebar">here</a>
     * @deprecated Deprecated in favor of {@link AbstractSubreddit#getDescription()}.
     */
    @Deprecated
    GET_SUBREDDIT_SIDEBAR("r", "{subreddit}", "about", "sidebar"),
    /**
     * <p>
     * Get the sidebar for the current subreddit.
     * </p>
     * <p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_sidebar">here</a>
     * @deprecated Deprecated in favor of {@link AbstractSubreddit#getDescription()}.
     */
    @Deprecated
    GET_SIDEBAR("about", "sidebar"),
    /**
     * <p>
     * Redirect to one of the posts stickied in the current subreddit.
     * </p>
     * <p>
     * The {@code num} argument can be used to select a specific sticky, and will default to 1 (the top sticky) if not
     * specified. Will 404 if there is not currently a sticky post in this subreddit.<p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * </p>
     * <table>
     *     <tr><th>{@code num}</th><th>an integer between 1 and 2 (default: 1)</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_sticky">here</a>
     */
    GET_SUBREDDIT_ABOUT_STICKY("r", "{subreddit}","about","sticky"),
    /**
     * <p>
     * Redirect to one of the posts stickied in the current subreddit.
     * </p>
     * <p>
     * The {@code num} argument can be used to select a specific sticky, and will default to 1 (the top sticky) if not
     * specified. Will 404 if there is not currently a sticky post in this subreddit.<p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * </p>
     * <table>
     *     <tr><th>{@code num}</th><th>an integer between 1 and 2 (default: 1)</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_sticky">here</a>
     */
    GET_ABOUT_STICKY("about","sticky"),
    /**
     * <p>
     * Get all subreddits.
     * </p>
     * <p>
     * The {@code where} parameter chooses the order in which the subreddits are displayed. {@code popular} sorts on the
     * activity of the subreddit and the position of the subreddits can shift around. {@code new} sorts the subreddits
     * based on their creation date, newest first.
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_{where}">here</a>
     */
    GET_SUBREDDITS_WHERE("subreddits","{where}"),
    /**
     * <p>
     * Get all subreddits.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_default">here</a>
     */
    GET_SUBREDDITS_DEFAULT(GET_SUBREDDITS_WHERE.getPath("default")),
    /**
     * <p>
     * Get all subreddits.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_gold">here</a>
     */
    GET_SUBREDDITS_GOLD(GET_SUBREDDITS_WHERE.getPath("gold")),
    /**
     * <p>
     * Get all subreddits.
     * </p>
     * <p>
     * {@code popular} sorts on the activity of the subreddit and the position of the subreddits can shift around.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_popular">here</a>
     */
    GET_SUBREDDITS_POPULAR(GET_SUBREDDITS_WHERE.getPath("popular")),
    /**
     * <p>
     * Get all subreddits.
     * </p>
     * <p>
     * {@code new} sorts the subreddits based on their creation date, newest first.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_new">here</a>
     */
    GET_SUBREDDITS_NEW(GET_SUBREDDITS_WHERE.getPath("new")),
    /**
     * <p>
     * Search subreddits by title and description.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_search">here</a>
     */
    GET_SUBREDDITS_SEARCH("subreddits","search"),
    /**
     * <p>
     * Get subreddits the user has a relationship with.
     * </p>
     * <p>
     * The where parameter chooses which subreddits are returned.
     * </p>
     * <p>
     * See also: /api/subscribe, /api/friend, and /api/accept_moderator_invite.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_mine_{where}">here</a>
     */
    GET_SUBREDDITS_MINE_WHERE("subreddits","mine","{where}"),
    /**
     * <p>
     * Get subreddits the user is an approved user in.
     * </p>
     * <p>
     * See also: /api/subscribe, /api/friend, and /api/accept_moderator_invite.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_mine_contributor">here</a>
     */
    GET_SUBREDDITS_MINE_CONTRIBUTOR(GET_SUBREDDITS_MINE_WHERE.getPath("contributor")),
    /**
     * <p>
     * Get subreddits the user is a moderator of.
     * </p>
     * <p>
     * See also: /api/subscribe, /api/friend, and /api/accept_moderator_invite.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_mine_moderator">here</a>
     */
    GET_SUBREDDITS_MINE_MODERATOR(GET_SUBREDDITS_MINE_WHERE.getPath("moderator")),
    /**
     * <p>
     * Get subreddits the user subscribed to subreddits that contain hosted video links.
     * </p>
     * <p>
     * See also: /api/subscribe, /api/friend, and /api/accept_moderator_invite.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_mine_streams">here</a>
     */
    GET_SUBREDDITS_MINE_STREAMS(GET_SUBREDDITS_MINE_WHERE.getPath("streams")),
    /**
     * <p>
     * Get subreddits the user is subscribed to.
     * </p>
     * <p>
     * See also: /api/subscribe, /api/friend, and /api/accept_moderator_invite.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_mine_subscriber">here</a>
     */
    GET_SUBREDDITS_MINE_SUBSCRIBER(GET_SUBREDDITS_MINE_WHERE.getPath("subscriber")),
    /**
     * <p>
     * Get all user subreddits.
     * </p>
     * <p>
     * The {@code where} parameter chooses the order in which the subreddits are displayed.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_users_{where}">here</a>
     */
    GET_USERS_WHERE("users","{where}"),
    /**
     * <p>
     * Get all user subreddits.
     * </p>
     * <p>
     * {@code new} sorts the user subreddits based on their creation date, newest first.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see #GET_USERS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_users_new">here</a>
     */
    GET_USERS_NEW(GET_USERS_WHERE.getPath("new")),
    /**
     * <p>
     * Get all user subreddits.
     * </p>
     * <p>
     * {@code popular} sorts on the activity of the subreddit and the position of the subreddits can shift around.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of subreddits.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see #GET_USERS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_users_popular">here</a>
     */
    GET_USERS_POPULAR(GET_USERS_WHERE.getPath("popular")),
    /**
     * <p>
     * Search user profiles by title and description.
     * </p>
     * <p>
     * This endpoint is a {@link Listing} of accounts.
     * </p>
     * <table>
     *     <tr><th>{@code after}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code before}</th><th><i>fullname</i> of a thing</th></tr>
     *     <tr><th>{@code count}</th><th>a positive integer (default:0)</th></tr>
     *     <tr><th>{@code limit}</th><th>the maximum number of items desired (default:25, maximum:100)</th></tr>
     *     <tr><th>{@code q}</th><th>a search query</th></tr>
     *     <tr><th>{@code search_query_id}</th><th>a uuid</th></tr>
     *     <tr><th>{@code show}</th><th>(optional) the string {@code all}</th></tr>
     *     <tr><th>{@code sort}</th><th>one of ({@code relevance}, {@code activity})</th></tr>
     *     <tr><th>{@code sr_detail}</th><th>(optional) expand subreddits</th></tr>
     *     <tr><th>{@code typeahead_active}</th><th>boolean value or {@code None}</th></tr>
     *     <caption>Supported Arguments</caption>
     * </table>
     * @see <a href="https://www.reddit.com/dev/api#GET_users_search">here</a>
     */
    GET_USERS_SEARCH("users","search"),

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    users                                                                                                        //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * For blocking a user.
     * @see <a href="https://www.reddit.com/dev/api#POST_api_block_user">here</a>
     */
    POST_BLOCK_USER("api", "block_user"),
    /**
     * <p>
     * Create a relationship between a user and another user or subreddit.
     * </p>
     * <p>
     * OAuth2 use requires appropriate scope based on the 'type' of the relationship:
     * </p>
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
     * <p>
     * Complement to #POST_SUBREDDIT_UNFRIEND
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_friend">here</a>
     * @see #POST_SUBREDDIT_UNFRIEND
     */
    POST_SUBREDDIT_FRIEND("r","{subreddit}","api","friend"),
    /**
     * <p>
     * Create a relationship between a user and another user or subreddit.
     * </p>
     * <p>
     * Create a relationship between a user and another user or subreddit.<br>
     * OAuth2 use requires appropriate scope based on the 'type' of the relationship:
     * </p>
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
     * <p>
     * Complement to #POST_UNFRIEND
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_friend">here</a>
     * @see #POST_UNFRIEND
     */
    POST_FRIEND("api", "friend"),
    /**
     * Report a user. Reporting a user brings it to the attention of a Reddit admin.
     * @see <a href="https://www.reddit.com/dev/api#POST_api_report_user">here</a>
     */
    POST_REPORT_USER("api", "report_user"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#POST_api_setpermissions">here</a>
     */
    POST_SUBREDDIT_SETPERMISSION("r","{subreddit}","api","setpermission"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#POST_api_setpermissions">here</a>
     */
    POST_SETPERMISSION("api","setpermission"),
    /**
     * <p>
     * Remove a relationship between a user and another user or subreddit.
     * </p>
     * <p>
     * The user can either be passed in by name (nuser) or by fullname (iuser). If type is friend or enemy, 'container'
     * MUST be the current user's fullname; for other types, the subreddit must be set via URL
     * (e.g., /r/funny/api/unfriend).
     * </p>
     * <p>
     * OAuth2 use requires appropriate scope based on the 'type' of the relationship:
     * </p>
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
     * <p>
     * Complement to #POST_SUBREDDIT_FRIEND
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#POST_api_unfriend">here</a>
     * @see #POST_SUBREDDIT_FRIEND
     */
    POST_SUBREDDIT_UNFRIEND("r","{subreddit}","api","unfriend"),
    /**
     * <p>
     * Remove a relationship between a user and another user or subreddit.
     * </p>
     * <p>
     * The user can either be passed in by name (nuser) or by fullname (iuser). If type is friend or enemy, 'container'
     * MUST be the current user's fullname; for other types, the subreddit must be set via URL
     * (e.g., /r/funny/api/unfriend).
     * </p>
     * <p>
     * The user can either be passed in by name (nuser) or by fullname (iuser). If type is friend or enemy, 'container'
     * MUST be the current user's fullname; for other types, the subreddit must be set via URL
     * (e.g., /r/funny/api/unfriend).
     * </p>
     * <p>
     * OAuth2 use requires appropriate scope based on the 'type' of the relationship:
     * </p>
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
     *
     * Complement to #POST_FRIEND
     * @see <a href="https://www.reddit.com/dev/api#POST_api_unfriend">here</a>
     * @see #POST_FRIEND
     */
    POST_UNFRIEND("api","unfriend"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_api_user_data_by_account_ids">here</a>
     */
    GET_USER_DATA_BY_ACCOUNT_IDS("api","user_data_by_account_ids"),
    /**
     * Check whether a username is available for registration.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_username_available">here</a>
     */
    GET_USERNAME_AVAILABLE("api","username_available"),
    /**
     * Stop being friends with a user.
     * @see <a href="https://www.reddit.com/dev/api#DELETE_api_v1_me_friends_%28username%29">here</a>
     */
    DELETE_ME_FRIENDS_USERNAME("api","v1","me","friends","{username}"),
    /**
     * Get information about a specific 'friend', such as notes.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_friends_%28username%29">here</a>
     */
    GET_ME_FRIENDS_USERNAME("api","v1","me","friends","{username}"),
    /**
     * <p>
     * Create or update a "friend" relationship.
     * </p>
     * <p>
     * This operation is idempotent. It can be used to add a new friend, or update an existing friend
     * (e.g., add/change the note on that friend).
     * </p>
     * @see <a href="https://www.reddit.com/dev/api#PUT_api_v1_me_friends_%28username%29">here</a>
     */
    PUT_ME_FRIENDS_USERNAME("api","v1","me","friends","{username}"),
    /**
     * Return a list of trophies for the a given user.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_user_%28username%29_trophies">here</a>
     */
    GET_USER_USERNAME_TROHPIES("api","v1","user","{username}","trophies"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_%28where%29">here</a>
     */
    GET_USER_USERNAME_WHERE("user","{username}","{where}"),
    /**
     * Return information about the user, including karma and gold status.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_user_%28username%29_about">here</a>
     */
    GET_USER_USERNAME_ABOUT(GET_USER_USERNAME_WHERE.getPath("{username}","about")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_comments">here</a>
     */
    GET_USER_USERNAME_COMMENTS(GET_USER_USERNAME_WHERE.getPath("{username}","comments")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_downvoted">here</a>
     */
    GET_USER_USERNAME_DOWNVOTED(GET_USER_USERNAME_WHERE.getPath("{username}","downvoted")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_gilded">here</a>
     */
    GET_USER_USERNAME_GILDED(GET_USER_USERNAME_WHERE.getPath("{username}","gilded")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_hidden">here</a>
     */
    GET_USER_USERNAME_HIDDEN(GET_USER_USERNAME_WHERE.getPath("{username}","hidden")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_overview">here</a>
     */
    GET_USER_USERNAME_OVERVIEW(GET_USER_USERNAME_WHERE.getPath("{username}","overview")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_saved">here</a>
     */
    GET_USER_USERNAME_SAVED(GET_USER_USERNAME_WHERE.getPath("{username}","saved")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_submitted">here</a>
     */
    GET_USER_USERNAME_SUBMITTED(GET_USER_USERNAME_WHERE.getPath("{username}","submitted")),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_%28username%29_upvoted">here</a>
     */
    GET_USER_USERNAME_UPVOTED(GET_USER_USERNAME_WHERE.getPath("{username}","upvoted"));

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    widgets                                                                                                        //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    wiki                                                                                                        //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//


    private final List<String> path;

    Endpoint(String... path){
        this(Arrays.asList(path));
    }

    Endpoint(List<String> path){
        this.path = Collections.unmodifiableList(path);
    }

    public List<String> getPath(Object... args){
        String substitution = "\\{\\w*}";
        List<String> result = new ArrayList<>();
        int i = 0;

        for(String entry : path)
            if (entry.matches(substitution))
                result.add(args[i++].toString());
            else
                result.add(entry);

        //All arguments have to be consumed
        assert i == args.length;

        return Collections.unmodifiableList(result);
    }
}
