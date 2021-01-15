package vartas.reddit.query;

import vartas.reddit.Link;
import vartas.reddit.Query;
import vartas.reddit.types.Thing;

import javax.annotation.Nullable;
import java.util.List;

public abstract class QueryLink<Q extends QueryLink<Q>> extends Query<Link, Q> {
    /**
     * The full name of the last {@link Thing} before the first element in the query.<p>
     * Default is {@code null}.
     */
    private static final String AFTER = "after";
    /**
     * The full name of the first {@link Thing} after the last element in the query.<p>
     * Default is {@code null}.
     */
    private static final String BEFORE = "before";
    /**
     * A non-negative integer.<p>
     * Default is {@code 0}.
     */
    private static final String COUNT = "count";
    /**
     * The total number of requested things. Has to be a number between [0,100].<p>
     * Default is {@code 25}.
     */
    private static final String LIMIT = "limit";
    /**
     * The String "all".
     */
    private static final String SHOW = "show";
    /**
     * Expand subreddits.<p>
     * Default is {@code false}.
     */
    private static final String EXPAND_SUBREDDIT = "sr_detail";

    public Q setAfter(@Nullable String after){
        args.put(AFTER, after);
        return getRealThis();
    }

    public Q setBefore(@Nullable String before){
        args.put(BEFORE, before);
        return getRealThis();
    }

    public Q setCount(int count){
        assert count >= 0;
        args.put(COUNT, count);
        return getRealThis();
    }

    public Q setLimit(int limit){
        assert limit >= 0 && limit < 100;
        args.put(LIMIT, limit);
        return getRealThis();
    }

    public Q show(@Nullable String show){
        assert show == null || show.equals("all");
        args.put(SHOW, show);
        return getRealThis();
    }

    public Q expandSubreddits(boolean state){
        args.put(EXPAND_SUBREDDIT, state);
        return getRealThis();
    }

    @Override
    public List<Link> query() {
        throw new UnsupportedOperationException();
    }
}
