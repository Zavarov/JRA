package vartas.reddit;

public abstract class Subreddit extends SubredditTOP{
    @Override
    public Subreddit getRealThis() {
        return this;
    }
}
