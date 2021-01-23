package vartas.jra.query;

import vartas.jra.Client;
import vartas.jra.Endpoint;
import vartas.jra.types.Thing;

import javax.annotation.Nullable;

public abstract class QueryBase<T, Q extends QueryBase<T,Q>> extends Query<T, Q> {
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
     * The number of items already seen in this query. on the html site, the builder uses this to determine when to give
     * values for {@code before} and {@code after} in the response.
     * Default is {@code 0}.
     */
    private static final String COUNT = "count";
    /**
     * The total number of requested things. Has to be a number between [0,100].<p>
     * Default is {@code 25}.
     */
    private static final String LIMIT = "limit";
    /**
     * Either null or the String "all".<p>
     * If {@code all} is passed, filters such as "hide links that I have voted on" will be disabled.<p>
     * Default is {@code all}.
     */
    private static final String SHOW = "show";
    /**
     * Expand subreddits.<p>
     * Default is {@code false}.
     */
    protected static final String EXPAND_SUBREDDIT = "sr_detail";

    public QueryBase(Client client, Endpoint endpoint, Object... args){
        super(client, endpoint, args);

        //Initialize arguments with default values
        params.put(AFTER, null);
        params.put(BEFORE, null);
        params.put(COUNT, 0);
        params.put(LIMIT, 25);
        params.put(SHOW, "all");
    }

    public Q setAfter(@Nullable String after){
        params.put(AFTER, after);
        return getRealThis();
    }

    public Q setBefore(@Nullable String before){
        params.put(BEFORE, before);
        return getRealThis();
    }

    public Q setCount(int count){
        assert count >= 0;
        params.put(COUNT, count);
        return getRealThis();
    }

    public Q setLimit(int limit){
        assert limit >= 0 && limit < 100;
        params.put(LIMIT, limit);
        return getRealThis();
    }

    public Q setShow(@Nullable String show){
        assert show == null || show.equals("all");
        params.put(SHOW, show);
        return getRealThis();
    }

    public Q setExpandSubreddits(boolean state){
        params.put(EXPAND_SUBREDDIT, state);
        return getRealThis();
    }
}
