package vartas.reddit;

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
