package vartas.jra.models;

import vartas.jra.Subreddit;
import vartas.jra.query.QueryOne;

public class FakeSubreddit extends FakeSubredditTOP{
    @Override
    public QueryOne<Subreddit> expand() {
        return getClient().getSubreddit(getName());
    }

    @Override
    public FakeSubreddit getRealThis() {
        return this;
    }
}
