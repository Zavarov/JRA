package vartas.reddit.query;

import vartas.reddit.Client;
import vartas.reddit.Endpoint;
import vartas.reddit.Subreddit;
import vartas.reddit.types.Thing;

import javax.annotation.Nonnull;
import java.util.function.Function;

public abstract class QuerySort<T, Q extends QuerySort<T,Q>> extends QueryMany<T,Q> {
    /**
     * The time period limiting the age of the things. The age is relative to the time the query is made.
     * @see Subreddit.Query.TimePeriod
     */
    protected static final String TIME_PERIOD = "t";

    public QuerySort(Function<Thing, T> mapper, Client client, Endpoint endpoint, Object... args) {
        super(mapper, client, endpoint, args);
        super.params.put(TIME_PERIOD, TimePeriod.ALL);
    }

    public Q setTimePeriod(@Nonnull TimePeriod timePeriod){
        params.put(TIME_PERIOD, timePeriod);
        return getRealThis();
    }
}
