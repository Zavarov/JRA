package vartas.reddit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Endpoint {

    //----------------------------------------------------------------------------------------------------------------//
    //                                                                                                                //
    //    read                                                                                                        //
    //                                                                                                                //
    //----------------------------------------------------------------------------------------------------------------//
    //BANNED
    //CONTRIBUTORS
    //EDITED
    //MODERATORS
    //MODQUEUE
    //MUTED
    //REPORTS
    //SPAM
    //UNMODERATED
    //WIKIBANNED
    //WIKICONTRIBUTORS
    //LOCATION
    //WHERE
    //FILTERPATH
    //FILTERPATH_R_SRNAME
    //INFO
    //LIVE_BY_ID
    //LIVE_HAPPENING_NOW
    //MORECHILDREN
    //MULTI_MINE
    //MULTI_USER
    //MULTI_PATH
    //MULTI_PATH_DESCRIPTION
    //MULTI_PATH_DESCRIPTION
    //MULTI_PATH_SRNAME
    //RECOMMEND_SRNAME
    //SEARCH_REDDIT_NAMES
    //SEARCH_REDDIT_NAMES
    //SEARCH_SUBREDDITS
    //SUBREDDIT_AUTOCOMPLETE
    //SUBREDDIT_AUTOCOMPLETE_V2
    //COLLECTIONS_COLLECTION
    //COLLECTIONS
    //SUBREDDIT_COLLECTIONS
    //ME_BLOCKED
    //ME_FRIENDS
    //USER_TROPHIES
    //SUBREDDIT_EMOJIS
    //BEST
    //BY_ID
    //COMMENTS
    //CONTROVERISAL
    //DUPLICATES
    //HOT
    //LIVE
    //LIVE_ABOUT
    //LIVE_CONTRIBUTORS
    //LIVE_DISCUSSIONS
    //LIVE_UPDATES
    //NEW
    //PREFS_BLOCKED
    //PREFS_FRIENDS
    //PREFS_MESSAGING
    //PREFS_TRUSTED
    //PREFS_WHERE

    /**
     * Return information about the subreddit.<p>
     *
     * Data includes the subscriber count, description, and header image.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_r_{subreddit}_about">here</a>
     */
    GET_SUBREDDIT_ABOUT("r", "{subreddit}", "about"),

    /**
     * Get the rules for the current subreddit.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_r_{subreddit}_about_rules">here</a>
     */
    GET_SUBREDDIT_ABOUT_RULES("r", "{subreddit}", "about", "rules"),
    /**
     * The Serendipity button. I.e. it fetches a random link from the subreddit.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_random">here</a>
     */
    GET_SUBREDDIT_RANDOM("r", "{subreddit}", "random"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_rising">here</a>
     */
    GET_SUBREDDIT_RISING("r", "{subreddit}", "rising"),
    /**
     * Search links page.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_search">here</a>
     */
    GET_SUBREDDIT_SEARCH("r", "{subreddit}", "search"),
    /**
     * Get the sidebar for the current subreddit.<p>
     * Note: The endpoint according to the <b>official API</b> is inaccurate and the endpoint has been moved.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_sidebar">here</a>
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
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_sticky">here</a>
     */
    GET_SUBREDDIT_STICKY("r", "{subreddit}","about","sticky"),
    /**
     * Get all subreddits.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_subreddits_default">here</a>
     */
    GET_SUBREDDITS_DEFAULT("subreddits", "default"),
    /**
     * Get all subreddits.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_subreddits_gold">here</a>
     */
    GET_SUBREDDITS_GOLD("subreddits", "gold"),
    /**
     * Get all subreddits.<p>
     *
     * {@code new} sorts the subreddits based on their creation date, newest first.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_subreddits_new">here</a>
     */
    GET_SUBREDDITS_NEW("subreddits","new"),
    /**
     * Get all subreddits.<p>
     *
     * {@code popular} sorts on the activity of the subreddit and the position of the subreddits can shift around.
     * @see #GET_SUBREDDITS_WHERE
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_subreddits_popular">here</a>
     */
    GET_SUBREDDITS_POPULAR("subreddits","popular"),
    /**
     * Search subreddits by title and description.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_subreddits_search">here</a>
     */
    GET_SUBREDDITS_SEARCH("subreddits","search"),
    /**
     * Get all subreddits.<p>
     *
     * The {@code where} parameter chooses the order in which the subreddits are displayed. {@code popular} sorts on the
     * activity of the subreddit and the position of the subreddits can shift around. {@code new} sorts the subreddits
     * based on their creation date, newest first.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_subreddits_{where}">here</a>
     */
    GET_SUBREDDITS_WHERE("subreddits","{where}"),
    /**
     * TODO What does it do?
     * @see #GET_SUBREDDIT_SORT
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_top">here</a>
     */
    GET_SUBREDDIT_TOP("r","{subreddit}","top"),
    /**
     * Return information about the user, including karma and gold status.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_user_{username}_about">here</a>
     */
    GET_USER_ABOUT("user","{username}","about"),
    /**
     * Get all user subreddits.<p>
     *
     * {@code new} sorts the user subreddits based on their creation date, newest first.
     * @see #GET_USERS_WHERE
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_users_new">here</a>
     */
    GET_USERS_NEW("users","new"),
    /**
     * Get all user subreddits.<p>
     *
     * {@code popular} sorts on the activity of the subreddit and the position of the subreddits can shift around.
     * @see #GET_USERS_WHERE
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_users_popular">here</a>
     */
    GET_USERS_POPULAR("users","popular"),
    /**
     * Search user profiles by title and description.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_users_search">here</a>
     */
    GET_USERS_SEARCH("users","search"),
    /**
     * Get all user subreddits.<p>
     *
     * The {@code where} parameter chooses the order in which the subreddits are displayed. {@code popular} sorts on the
     * activity of the subreddit and the position of the subreddits can shift around. {@code new} sorts the user
     * subreddits based on their creation date, newest first.
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_users_{where}">here</a>
     */
    GET_USERS_WHERE("users","{where}"),
    /**
     * TODO What does it do?
     * @see <a href="https://www.reddit.com/dev/api/oauth#GET_{sort}">here</a>
     */
    GET_SUBREDDIT_SORT("r","{subreddit}","{sort}");

    private List<String> path;

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

        return Collections.unmodifiableList(result);
    }
}
