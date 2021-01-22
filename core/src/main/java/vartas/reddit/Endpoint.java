package vartas.reddit;

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
     * Returns the identity of the user.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me">here</a>
     */
    GET_ME("api","v1","me"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_blocked">here</a>
     * @deprecated This endpoint seems to be no longer supported and throws an 404. Use {@link #GET_PREFS_BLOCKED} if
     * you want a list of all blocked users.
     */
    @Deprecated
    GET_ME_BLOCKED("api","v1","me","blocked"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_friends">here</a>
     */
    GET_ME_FRIENDS("api","v1","me","friends"),
    /**
     * Return a breakdown of subreddit karma.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_karma">here</a>
     */
    GET_ME_KARMA("api","v1","me","karma"),
    /**
     * Return the preference settings of the logged in user.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_prefs">here</a>
     */
    GET_ME_PREFS("api","v1","me","prefs"),
    /**
     * Return a list of trophies for the current user.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_trophies">here</a>
     */
    GET_ME_TROPHIES("api","v1","me","trophies"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_blocked">here</a>
     */
    GET_PREFS_BLOCKED("prefs","blocked"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_friends">here</a>
     */
    GET_PREFS_FRIENDS("prefs","friends"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_messaging">here</a>
     */
    GET_PREFS_MESSAGING("prefs","messaging"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_trusted">here</a>
     */
    GET_PREFS_TRUSTED("prefs","trusted"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_prefs_{where}">here</a>
     */
    GET_PREFS_WHERE("prefs","{where}"),

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    captcha                                                                                                     //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Check whether ReCAPTCHAs are needed for API methods
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
     * Return a list of trending subreddits, link to the comment in r/trendingsubreddits, and the comment count of that
     * link.<p>
     * Note: The actual endpoint deviates from the documentation as in that it ends with {@code .json}. It also seems to
     * be one of the few endpoints that can't be accessed via OAuth2.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_trending_subreddits">here</a>
     */
    GET_API_TRENDING_SUBREDDITS("api","trending_subreddits.json"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_best">here</a>
     */
    GET_BEST("best"),
    /**
     * Get a listing of links by fullname.<p>>
     *
     * {@code names} is a list of fullnames for links separated by commas or spaces.
     * @see <a href="https://www.reddit.com/dev/api#GET_by_id_{names}">here</a>
     */
    GET_BY_ID("by_id","{names}"),
    /**
     * Get the comment tree for a given Link {@code article}.<p>
     *
     * If supplied, {@code comment} is the ID36 of a comment in the comment tree for {@code article}. This comment will
     * be the (highlighted) focal point of the returned view and context will be the number of parents shown.<p>
     *
     * {@code depth} is the maximum depth of subtrees in the thread.<p>
     * {@code limit} is the maximum number of comments to return.<p>
     *
     * TODO see also /api/morechildren
     * TODO see also /api/comment
     * @see #GET_SUBREDDIT_COMMENTS
     * @see <a href="https://www.reddit.com/dev/api#GET_comments_{article}">here</a>
     */
    GET_COMMENTS("comments","{article}"),
    /**
     * Get the comment tree for a given Link {@code article}.<p>
     *
     * If supplied, {@code comment} is the ID36 of a comment in the comment tree for {@code article}. This comment will
     * be the (highlighted) focal point of the returned view and context will be the number of parents shown.<p>
     *
     * {@code depth} is the maximum depth of subtrees in the thread.<p>
     * {@code limit} is the maximum number of comments to return.<p>
     *
     * TODO see also /api/morechildren
     * TODO see also /api/comment
     * @see #GET_COMMENTS
     * @see <a href="https://www.reddit.com/dev/api#GET_comments_{article}">here</a>
     */
    GET_SUBREDDIT_COMMENTS("r","{subreddit}","comments","{article}"),
    /**
     * TODO What does it do?
     * @see #GET_SORT
     * @see <a href="https://www.reddit.com/dev/api#GET_controversial">here</a>
     */
    GET_CONTROVERSIAL("controversial"),
    /**
     * TODO What does it do?
     * @see #GET_SUBREDDIT_SORT
     * @see <a href="https://www.reddit.com/dev/api#GET_controversial">here</a>
     */
    GET_SUBREDDIT_CONTROVERSIAL("r","{subreddit}","controversial"),
    /**
     * Return a list of other submissions of the same URL
     *
     * @see <a href="https://www.reddit.com/dev/api#GET_duplicates_{article}">here</a>
     */
    GET_DUPLICATES("duplicates","{article}"),
    /**
     * Get all subreddits.
     *
     * @see <a href="https://www.reddit.com/dev/api#GET_hot">here</a>
     */
    GET_HOT("hot"),
    /**
     * Get all submissions.
     *
     * @see <a href="https://www.reddit.com/dev/api#GET_hot">here</a>
     */
    GET_SUBREDDIT_HOT("r","{subreddit}","hot"),
    /**
     * Get all subreddits.<p>
     *
     * {@code new} sorts the subreddits based on their creation date, newest first.
     * @see <a href="https://www.reddit.com/dev/api#GET_new">here</a>
     */
    GET_NEW("new"),
    /**
     * Get all submissions.<p>
     *
     * {@code new} sorts the submissions based on their creation date, newest first.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_new">here</a>
     */
    GET_SUBREDDIT_NEW("r", "{subreddit}","new"),
    /**
     * The Serendipity button. I.e. it fetches a random {@link Subreddit}.
     * @see <a href="https://www.reddit.com/dev/api#GET_random">here</a>
     */
    GET_RANDOM("random"),
    /**
     * The Serendipity button. I.e. it fetches a random {@link Link} from the {@link Subreddit}.
     * @see <a href="https://www.reddit.com/dev/api#GET_random">here</a>
     */
    GET_SUBREDDIT_RANDOM("r", "{subreddit}", "random"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_rising">here</a>
     */
    GET_RISING("rising"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_rising">here</a>
     */
    GET_SUBREDDIT_RISING("r", "{subreddit}", "rising"),
    /**
     * TODO What does it do?
     * @see #GET_SORT
     * @see <a href="https://www.reddit.com/dev/api#GET_top">here</a>
     */
    GET_TOP("top"),
    /**
     * TODO What does it do?
     * @see #GET_SUBREDDIT_SORT
     * @see <a href="https://www.reddit.com/dev/api#GET_top">here</a>
     */
    GET_SUBREDDIT_TOP("r","{subreddit}","top"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_{sort}">here</a>
     */
    GET_SORT("{sort}"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_{sort}">here</a>
     */
    GET_SUBREDDIT_SORT("r","{subreddit}","{sort}"),

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

// /about/banned
// /about/contributors
// /about/moderators
// /about/muted
// /about/wikibanned
// /about/wikicontributors
// /about/where
// /api/delete_sr_banner
// /api/delete_sr_header
// /api/delete_sr_icon
// /api/delete_sr_img
// /api/recommended/sr/srnames
// /api/search_reddit_names
// /api/site_admin
// /api/submit_text
// /api/subreddit_autocomplete
// /api/subreddit_autocomplete_v2
// /api/subreddit_stylesheet
// /api/subscribe
// /api/upload_sr_img
// /api/v1_subreddit
// /api/post_requirements
    /**
     * Return information about the subreddit.<p>
     *
     * Data includes the subscriber count, description, and header image.
     * @see <a href="https://www.reddit.com/dev/api#GET_r_{subreddit}_about">here</a>
     */
    GET_SUBREDDIT_ABOUT("r", "{subreddit}", "about"),
// /r/subreddit/about/edit
    /**
     * Get the rules for the current subreddit.
     * @see <a href="https://www.reddit.com/dev/api#GET_r_{subreddit}_about_rules">here</a>
     */
    GET_SUBREDDIT_ABOUT_RULES("r", "{subreddit}", "about", "rules"),
// /r/subreddit/about/traffic
    /**
     * Get the sidebar for the current subreddit.<p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * @see <a href="https://www.reddit.com/dev/api#GET_sidebar">here</a>
     * @deprecated This endpoints seems to be deprecated in favor of {@link Subreddit#getDescription()}.
     */
    @Deprecated
    GET_SUBREDDIT_SIDEBAR("r", "{subreddit}", "about", "sidebar"),
    /**
     * Redirect to one of the posts stickied in the current subreddit.<p>
     *
     * The {@code num} argument can be used to select a specific sticky, and will default to 1 (the top sticky) if not
     * specified. Will 404 if there is not currently a sticky post in this subreddit.<p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * @see <a href="https://www.reddit.com/dev/api#GET_sticky">here</a>
     */
    GET_SUBREDDIT_ABOUT_STICKY("r", "{subreddit}","about","sticky"),
    /**
     * Get all subreddits.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_default">here</a>
     */
    GET_SUBREDDITS_DEFAULT("subreddits", "default"),
    /**
     * Get all subreddits.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_gold">here</a>
     */
    GET_SUBREDDITS_GOLD("subreddits", "gold"),
// /subreddits/mine/contributor
// /subreddits/mine/moderator
// /subreddits/mine/streams
// /subreddits/mine/subscriber
// /subreddits/mine/where
// /subreddits/new
    /**
     * Get all subreddits.<p>
     *
     * {@code popular} sorts on the activity of the subreddit and the position of the subreddits can shift around.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_popular">here</a>
     */
    GET_SUBREDDITS_POPULAR("subreddits","popular"),
    /**
     * Search subreddits by title and description.
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_search">here</a>
     */
    GET_SUBREDDITS_SEARCH("subreddits","search"),
    /**
     * Get all subreddits.<p>
     *
     * The {@code where} parameter chooses the order in which the subreddits are displayed. {@code popular} sorts on the
     * activity of the subreddit and the position of the subreddits can shift around. {@code new} sorts the subreddits
     * based on their creation date, newest first.
     * @see <a href="https://www.reddit.com/dev/api#GET_subreddits_{where}">here</a>
     */
    GET_SUBREDDITS_WHERE("subreddits","{where}"),
    /**
     * Get all user subreddits.<p>
     *
     * {@code new} sorts the user subreddits based on their creation date, newest first.
     * @see #GET_USERS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_users_new">here</a>
     */
    GET_USERS_NEW("users","new"),
    /**
     * Get all user subreddits.<p>
     *
     * {@code popular} sorts on the activity of the subreddit and the position of the subreddits can shift around.
     * @see #GET_USERS_WHERE
     * @see <a href="https://www.reddit.com/dev/api#GET_users_popular">here</a>
     */
    GET_USERS_POPULAR("users","popular"),
    /**
     * Search user profiles by title and description.
     * @see <a href="https://www.reddit.com/dev/api#GET_users_search">here</a>
     */
    GET_USERS_SEARCH("users","search"),
    /**
     * Get all user subreddits.<p>
     *
     * The {@code where} parameter chooses the order in which the subreddits are displayed. {@code popular} sorts on the
     * activity of the subreddit and the position of the subreddits can shift around. {@code new} sorts the user
     * subreddits based on their creation date, newest first.
     * @see <a href="https://www.reddit.com/dev/api#GET_users_{where}">here</a>
     */
    GET_USERS_WHERE("users","{where}"),

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
     *
     * Complement to #POST_SUBREDDIT_UNFRIEND
     * @see <a href="https://www.reddit.com/dev/api#POST_api_friend">here</a>
     * @see #POST_SUBREDDIT_UNFRIEND
     */
    POST_SUBREDDIT_FRIEND("r","{subreddit}","api","friend"),
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
     *
     * Complement to #POST_UNFRIEND
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
     *
     * Complement to #POST_SUBREDDIT_FRIEND
     * @see <a href="https://www.reddit.com/dev/api#POST_api_unfriend">here</a>
     * @see #POST_SUBREDDIT_FRIEND
     */
    POST_SUBREDDIT_UNFRIEND("r","{subreddit}","api","unfriend"),
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
     * @see <a href="https://www.reddit.com/dev/api#DELETE_api_v1_me_friends_{username}">here</a>
     */
    DELETE_ME_FRIENDS_USERNAME("api","v1","me","friends","{username}"),
    /**
     * Get information about a specific 'friend', such as notes.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_me_friends_{username}">here</a>
     */
    GET_ME_FRIENDS_USERNAME("api","v1","me","friends","{username}"),
    /**
     * Create or update a "friend" relationship.<p>
     * This operation is idempotent. It can be used to add a new friend, or update an existing friend
     * (e.g., add/change the note on that friend).
     * @see <a href="https://www.reddit.com/dev/api#PUT_api_v1_me_friends_{username}">here</a>
     */
    PUT_ME_FRIENDS_USERNAME("api","v1","me","friends","{username}"),
    /**
     * Return a list of trophies for the a given user.
     * @see <a href="https://www.reddit.com/dev/api#GET_api_v1_user_{username}_trophies">here</a>
     */
    GET_USER_USERNAME_TROHPIES("api","v1","user","{username}","trophies"),
    /**
     * Return information about the user, including karma and gold status.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_user_{username}_about">here</a>
     */
    GET_USER_USERNAME_ABOUT("user","{username}","about"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_comments">here</a>
     */
    GET_USER_USERNAME_COMMENTS("user","{username}","comments"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_downvoted">here</a>
     */
    GET_USER_USERNAME_DOWNVOTED("user","{username}","downvoted"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_gilded">here</a>
     */
    GET_USER_USERNAME_GILDED("user","{username}","gilded"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_hidden">here</a>
     */
    GET_USER_USERNAME_HIDDEN("user","{username}","hidden"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_overview">here</a>
     */
    GET_USER_USERNAME_OVERVIEW("user","{username}","overview"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_saved">here</a>
     */
    GET_USER_USERNAME_SAVED("user","{username}","saved"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_submitted">here</a>
     */
    GET_USER_USERNAME_SUBMITTED("user","{username}","submitted"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_upvoted">here</a>
     */
    GET_USER_USERNAME_UPVOTED("user","{username}","upvoted"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api#GET_user_{username}_{where}">here</a>
     */
    GET_USER_USERNAME_WHERE("user","{username}","{where}");

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
        this.path = Arrays.asList(path);
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
